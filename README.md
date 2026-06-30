# PUCRS-TF-PROJARC — Sistema de Tele Pizza (Parte 2)

Trabalho Final de Projeto e Arquitetura de Software — Prof. Bernardo Copstein.

Arquitetura de microsserviços para sistema de gestão de pedidos de pizzaria, com autenticação via gateway, fila de mensagens assíncrona e service discovery.

## Arquitetura

```
                         ┌─────────────────┐
  Cliente / Admin ──────►│  Spring Gateway  │:8765
                         │  (JWT Auth)      │
                         └────────┬─────────┘
                                  │ roteia via Eureka
               ┌──────────────────┼────────────────┐
               ▼                  ▼                ▼
        ┌─────────────┐   ┌──────────────┐   ┌──────────────┐
        │   pizzaria  │   │estoque-service│   │  name-server │
        │  (monolito) │──►│   :8001 JPA  │   │  Eureka:8761 │
        │   :8080 x2  │   └──────────────┘   └──────────────┘
        └──────┬──────┘
               │ RabbitMQ (async)
               ▼
        ┌─────────────────────────────────┐
        │       entrega-service x3        │
        │  (competing consumers, :8002)   │
        └─────────────────────────────────┘
```

### Microserviços

| Serviço | Porta | Réplicas | Descrição |
|---------|-------|----------|-----------|
| `name-server` | 8761 | 1 | Eureka Server — service discovery |
| `gateway` | 8765 | 1 | Spring Cloud Gateway com autenticação JWT |
| `pizzaria` | 8080 | 2 | Monolito principal (DDD, 4 camadas) |
| `estoque-service` | 8001 | 1 | Microsserviço de estoque com JPA + PostgreSQL próprio |
| `entrega-service` | 8002 | 3 | Consuming competing de fila RabbitMQ |
| `rabbitmq` | 5672/15672 | 1 | Message broker |
| `postgres-pizzaria` | — | 1 | Banco da pizzaria |
| `postgres-estoque` | — | 1 | Banco do estoque |

## Como Executar

### Pré-requisitos
- Docker + Docker Compose

### Subir tudo com um comando

```bash
docker compose up --build
```

O compose garante a ordem de inicialização via `healthcheck` e `depends_on`. Aguarde todos os serviços ficarem healthy (~2 min na primeira execução).

### Verificar a saúde dos serviços

```bash
docker compose ps
```

### Parar e limpar

```bash
docker compose down -v   # remove containers e volumes
```

## Autenticação

Todas as rotas (exceto `POST /auth/login` e `POST /auth/register`) exigem JWT no header:

```
Authorization: Bearer <token>
```

### Cadastrar usuário

```http
POST http://localhost:8765/auth/register
Content-Type: application/json

{
  "email": "cliente@email.com",
  "password": "senha123",
  "nome": "Cliente Teste",
  "cpf": "12345678901",
  "celular": "51999999999",
  "endereco": "Rua Teste, 100"
}
```

### Obter token

```http
POST http://localhost:8765/auth/login
Content-Type: application/json

{
  "email": "cliente@email.com",
  "password": "senha123"
}
```

## Principais Endpoints (via Gateway :8765)

### Cardápio (público via JWT)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/cardapios` | Lista todos os cardápios |
| GET | `/cardapios/{id}` | Carrega cardápio com produtos (UC5) |

### Pedidos

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/pedidos` | Submete pedido para aprovação (UC6) |
| GET | `/pedidos/{id}/status` | Acompanha status do pedido (UC7) |
| DELETE | `/pedidos/{id}` | Cancela pedido APROVADO (UC8) |
| POST | `/pedidos/{id}/pagamento` | Paga pedido → inicia cozinha (UC9) |
| GET | `/pedidos/entregues` | Lista pedidos entregues entre datas (UC10) |

### Administração

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/admin/cardapios` | Lista cardápios (UC1) |
| PUT | `/admin/cardapios/{id}` | Define cardápio corrente (UC2) |
| GET | `/admin/descontos` | Lista políticas de desconto (UC3) |
| PUT | `/admin/descontos/{codigo}` | Define política de desconto corrente (UC4) |

## Serviços de Domínio

### Impostos (configurável via env var)

| ID (lei) | Taxa | Variável |
|----------|------|----------|
| `LC 123/2006` | 10% simples | `TAX_STRATEGY=LC 123/2006` (padrão) |
| `Lucro Presumido` | alíquota diferenciada | `TAX_STRATEGY=Lucro Presumido` |

### Descontos (trocável via endpoint `PUT /admin/descontos/{codigo}`)

| Código | Desconto | Critério |
|--------|----------|----------|
| `SemDesconto` | 0% | padrão |
| `ClienteFrequente` | 7% | mais de 3 pedidos nos últimos 20 dias |
| `PromocaoVerao` | 15% | promoção sazonal |
| `PromocaoDiaDosPais` | 10% | promoção especial |

## Fluxo de Status do Pedido

```
NOVO → APROVADO → PAGO → AGUARDANDO → PREPARACAO → PRONTO → TRANSPORTE → ENTREGUE
                ↓
            CANCELADO (só de APROVADO)
```

## Arquitetura do Monolito (Clean Architecture / DDD)

```
pizzaria/src/main/java/.../
├── Dominio/          # Entidades, interfaces de repositório, serviços de domínio
│   ├── Dados/        # Interfaces (PedidoRepository, ProdutosRepository…)
│   ├── Entidades/    # Pedido, Cliente, Produto, Receita, Ingrediente…
│   └── Servicos/     # PedidoService, CozinhaService, CalculadoraPreco…
├── Aplicacao/        # Use Cases (SubmeterPedidoUC, AcompanharPedidoUC…)
├── Adaptadores/      # Implementações concretas
│   ├── Apresentacao/ # Controllers REST
│   ├── Dados/        # JPA repositories
│   └── Estoque/      # StockHttpAdapter (HTTP → estoque-service)
└── Infra/            # Configurações Spring (RabbitMQ, Imposto, Security…)
```

## Padrões Implementados

- **Strategy** — políticas de imposto (`IImpostoService`) e desconto (`DescontoPolicy`)
- **Competing Consumer** — 3 instâncias do `entrega-service` consumindo a mesma fila RabbitMQ
- **Load Balancing** — 2 réplicas de `pizzaria` com header `X-Served-By` identificando a instância
- **Clean Architecture** — dependências sempre apontando para o domínio (nunca para fora)
- **@Transactional** — atomicidade em todas as operações de escrita do `PedidoService`

## Executar Testes

```bash
# Requer JDK 21-25 e Maven Wrapper
.\mvnw.cmd test
```

Os testes unitários rodam com H2 em memória (sem necessidade de infraestrutura externa).

## Variáveis de Ambiente

| Variável | Padrão | Serviço |
|----------|--------|---------|
| `DATABASE_URL` | — | pizzaria, gateway, estoque-service |
| `DATABASE_USERNAME` | — | pizzaria, gateway, estoque-service |
| `DATABASE_PASSWORD` | — | pizzaria, gateway, estoque-service |
| `JWT_SECRET` | — | gateway |
| `RABBITMQ_HOST` | `rabbitmq` | pizzaria, entrega-service |
| `EUREKA_URI` | `http://name-server:8761/eureka/` | todos |
| `TAX_STRATEGY` | `LC 123/2006` | pizzaria |
| `POLITICA_DESCONTO` | `SemDesconto` | pizzaria |
| `INTERNAL_SECRET` | `pizzaria-delivery-secret` | pizzaria, entrega-service |
| `ESTOQUE_SERVICE_URL` | `http://estoque-service:8001` | pizzaria |
| `PIZZARIA_URL` | `http://pizzaria:8080` | entrega-service |

## Tecnologias

- Java 21 / Spring Boot 3.4.3 / Spring Cloud 2024.0.0
- Spring Cloud Gateway (roteamento + JWT filter)
- Spring Cloud Netflix Eureka (service discovery)
- Spring Data JPA + PostgreSQL
- Spring AMQP + RabbitMQ
- JJWT 0.11.5 (HS256)
- BCrypt (senhas)
- Docker Compose (orquestração)
- JUnit 5 + Mockito + MockRestServiceServer (testes)
