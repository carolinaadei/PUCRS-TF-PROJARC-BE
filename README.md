# Backend — Tele Pizza PUCRS

Sistema de gestão de pedidos de uma pizzaria online, desenvolvido com **Spring Boot 3** seguindo **Arquitetura Limpa (Clean Architecture)** e princípios de **Domain-Driven Design (DDD)**.

---

## Cronograma de entregas

| Data       | Casos de uso entregues        |
|------------|-------------------------------|
| 11/05/2026 | UC3 (Cardápio) e UC4 (Pedido) |
| 13/05/2026 | UC5 (Status) e UC6 (Cancelar) |
| 18/05/2026 | UC1 (Registro), UC2 (Login) e UC7 (Pagar) |
| 20/05/2026 | UC8 e UC9 (Relatórios)        |

---

## Estrutura de pacotes

```
src/main/java/com/bcopstein/ex4_lancheriaddd_v1/
│
├── Dominio/                        # Camada de Domínio — núcleo da aplicação
│   ├── Entidades/                  # Entidades JPA e Enum de status
│   │   ├── Cardapio.java
│   │   ├── ItemCardapio.java       # Item de cardápio (MenuItem)
│   │   ├── Ingrediente.java
│   │   ├── Receita.java            # Relação ingrediente ↔ item
│   │   ├── ItemEstoque.java
│   │   ├── Cliente.java
│   │   ├── Pedido.java             # Aggregate root do pedido
│   │   ├── ItemPedido.java
│   │   └── StatusPedido.java       # Enum: NOVO→APROVADO→PAGO→...→ENTREGUE
│   ├── Dados/                      # Interfaces de repositório (portas de saída)
│   │   ├── CardapioRepository.java
│   │   ├── ClienteRepository.java
│   │   ├── PedidoRepository.java
│   │   └── EstoqueRepository.java
│   └── Servicos/                   # Serviços de domínio e suas interfaces
│       ├── IImpostoService.java    # Interface — facilita troca da política
│       ├── ImpostoService.java     # Implementação atual: 10% fixo
│       ├── IDescontoService.java   # Interface — facilita troca da política
│       ├── DescontoService.java    # Implementação atual: 7% para clientes fiéis
│       ├── IEstoqueService.java    # Interface — permite implementação fake
│       └── EstoqueService.java     # Implementação real com verificação e consumo
│
├── Aplicacao/                      # Camada de Aplicação — orquestra use cases
│   ├── UseCases/                   # Interfaces dos casos de uso (portas de entrada)
│   │   ├── CarregarCardapioUC.java
│   │   └── SubmeterPedidoUC.java
│   ├── Requests/                   # DTOs de entrada
│   │   └── SubmeterPedidoRequest.java
│   ├── Responses/                  # DTOs de saída
│   │   ├── CardapioResponse.java
│   │   └── PedidoResponse.java
│   ├── CarregarCardapioUCImpl.java # UC3
│   └── SubmeterPedidoUCImpl.java   # UC4
│
└── Adaptadores/                    # Camada de Adaptadores — detalhes externos
    ├── Dados/                      # Implementações JPA dos repositórios
    │   ├── CardapioJpaRepository.java
    │   ├── CardapioRepositoryImpl.java
    │   ├── ClienteJpaRepository.java
    │   ├── ClienteRepositoryImpl.java
    │   ├── PedidoJpaRepository.java
    │   ├── PedidoRepositoryImpl.java
    │   └── EstoqueRepositoryImpl.java
    └── Apresentacao/               # Controllers REST + Segurança
        ├── CardapioController.java
        ├── PedidoController.java
        ├── AuthController.java
        ├── GlobalExceptionHandler.java
        └── Security/
            ├── ClienteUserDetails.java
            ├── JwtService.java
            └── SecurityConfig.java
```

---

## Decisões de arquitetura

### Por que as interfaces de serviço foram criadas no Domínio?

O enunciado pede que imposto e desconto sejam **fáceis de trocar**. Seguindo Clean Architecture, as interfaces (`IImpostoService`, `IDescontoService`, `IEstoqueService`) vivem no Domínio e as implementações concretas são injetadas via Spring. Para trocar a fórmula de imposto, basta criar uma nova classe que implemente `IImpostoService` e anotá-la com `@Primary`.

### Por que Spring Data JPA em vez de JDBC manual?

A task pede mapeamento ORM. O JPA elimina SQL manual para CRUDs básicos, garante gerenciamento de transações e permite queries JPQL legíveis. O schema ainda é controlado via `schema.sql` para total visibilidade do DDL.

### Cardápio corrente

O campo `corrente = true` na tabela `cardapios` controla qual cardápio é exibido. Apenas um deve ter `corrente = true` por vez. Para trocar, basta atualizar o registro no banco.

---

## Banco de dados

- **H2 em memória** durante desenvolvimento (`jdbc:h2:mem:pizzaria`)
- Console H2 disponível em `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:pizzaria`
  - Usuário: `sa` / Senha: *(vazio)*
- Schema gerenciado em `src/main/resources/db/schema.sql`
- Dados iniciais em `src/main/resources/db/data.sql`

### Entidades e relacionamentos

```
Cardapio (1) ──< (N) ItemCardapio
ItemCardapio (1) ──< (N) Receita >── (1) Ingrediente
Ingrediente (1) ──── (1) ItemEstoque
Cliente (1) ──< (N) Pedido
Pedido (1) ──< (N) ItemPedido >── (1) ItemCardapio
```

---

## Segurança

Autenticação via **JWT (Bearer Token)**.

- Endpoints públicos: `POST /api/auth/registrar` e `POST /api/auth/login`
- Todos os demais endpoints exigem `Authorization: Bearer <token>` no header
- Token válido por 24 horas (configurável em `application.yaml`)

---

## Como executar

```bash
# Na raiz do projeto
./mvnw clean spring-boot:run
```

Requisitos: Java 21, Maven (ou use o wrapper `mvnw` incluso).

---

## Endpoints disponíveis (entrega 11/05)

### UC3 — Carregar cardápio (requer token)

```
GET /api/cardapio
Authorization: Bearer <token>
```

**Resposta 200:**
```json
{
  "id": 1,
  "descricao": "Cardápio Outubro 2025",
  "itens": [
    { "id": 1, "descricao": "Pizza Margherita (30cm)", "precoUnit": 42.90, "disponivel": true }
  ]
}
```

---

### UC4 — Submeter pedido para aprovação (requer token)

```
POST /api/pedidos
Authorization: Bearer <token>
Content-Type: application/json

{
  "enderecoEntrega": "Rua das Flores, 100",
  "itens": [
    { "itemCardapioId": 1, "quantidade": 2 },
    { "itemCardapioId": 6, "quantidade": 1 }
  ]
}
```

**Resposta 200 — pedido aprovado:**
```json
{
  "id": 1,
  "status": "APROVADO",
  "custoItens": 92.70,
  "desconto": 0.00,
  "imposto": 9.27,
  "custoFinal": 101.97,
  "itens": [...],
  "itensSemEstoque": []
}
```

**Resposta 200 — pedido negado por falta de estoque:**
```json
{
  "id": 2,
  "status": "NOVO",
  "itensSemEstoque": [3, 5],
  ...
}
```

---

### UC1 e UC2 — Registro e Login (previstos para 18/05, infraestrutura já disponível)

```
POST /api/auth/registrar
{ "nome": "...", "cpf": "...", "email": "...", "senha": "..." }

POST /api/auth/login
{ "email": "...", "senha": "..." }
→ { "token": "eyJ...", "email": "...", "clienteId": 1 }
```

**Usuário de teste (disponível no data.sql):**
- Email: `joao@email.com`
- Senha: `senha123`

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem |
| Spring Boot | 3.3.4 | Framework principal |
| Spring Data JPA | (via Boot) | ORM / repositórios |
| Spring Security | (via Boot) | Autenticação e autorização |
| jjwt | 0.11.5 | Geração e validação de JWT |
| H2 Database | (via Boot) | Banco em memória (dev) |
| Lombok | (via Boot) | Redução de boilerplate |
| Bean Validation | (via Boot) | Validação de DTOs |