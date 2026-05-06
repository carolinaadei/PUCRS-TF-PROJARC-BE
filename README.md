# PUCRS-TF-PROJARC - Lancheria DDD

Projeto de exemplo de arquitetura em camadas com **Domain-Driven Design (DDD)** usando Spring Boot.

## Estrutura do Projeto

```
PUCRS-TF-PROJARC/
├── docs/                          # Documentação do projeto
│   └── architecture/              # Diagramas de arquitetura (PlantUML)
│       ├── Atores.puml
│       ├── ContextoC1.puml
│       ├── DestaqueAdaptadores.puml
│       ├── DestaqueDominio.puml
│       ├── Dominio.puml
│       ├── DrgClasses4camadas.puml
│       └── OrganizacaoEmPacotes.puml
│
├── src/
│   ├── main/
│   │   ├── java/com/bcopstein/ex4_lancheriaddd_v1/
│   │   │   ├── Adaptadores/           # Camada de Adaptação (Apresentação/Dados)
│   │   │   │   ├── Apresentacao/
│   │   │   │   │   ├── CardapioController.java
│   │   │   │   │   ├── Controller.java
│   │   │   │   │   └── Presenters/
│   │   │   │   └── Dados/
│   │   │   │       ├── CardapioRepositoryJDBC.java
│   │   │   │       ├── IngredientesRepositoryJDBC.java
│   │   │   │       ├── ProdutosRepositoryJDBC.java
│   │   │   │       └── ReceitasRepositoryJDBC.java
│   │   │   │
│   │   │   ├── Aplicacao/             # Camada de Aplicação (Use Cases)
│   │   │   │   ├── RecuperaListaCardapiosUC.java
│   │   │   │   ├── RecuperarCardapioUC.java
│   │   │   │   └── Responses/
│   │   │   │       ├── CabecalhoCardapioResponse.java
│   │   │   │       └── CardapioResponse.java
│   │   │   │
│   │   │   ├── Dominio/               # Camada de Domínio (Lógica de Negócio)
│   │   │   │   ├── Dados/
│   │   │   │   │   ├── CardapioRepository.java
│   │   │   │   │   ├── IngredientesRepository.java
│   │   │   │   │   ├── ProdutosRepository.java
│   │   │   │   │   └── ReceitasRepository.java
│   │   │   │   ├── Entidades/
│   │   │   │   │   ├── CabecalhoCardapio.java
│   │   │   │   │   ├── Cardapio.java
│   │   │   │   │   ├── Cliente.java
│   │   │   │   │   ├── Ingrediente.java
│   │   │   │   │   ├── ItemEstoque.java
│   │   │   │   │   ├── ItemPedido.java
│   │   │   │   │   ├── Pedido.java
│   │   │   │   │   ├── Produto.java
│   │   │   │   │   └── Receita.java
│   │   │   │   └── Servicos/
│   │   │   │       ├── CardapioService.java
│   │   │   │       ├── CozinhaService.java
│   │   │   │       └── ICozinhaService.java
│   │   │   │
│   │   │   └── Ex4LancheriadddV1Application.java
│   │   │
│   │   └── resources/
│   │       ├── application.yaml       # Configurações da aplicação
│   │       └── db/                    # Scripts de banco de dados
│   │           ├── schema.sql         # Definição do schema
│   │           └── data.sql           # Dados iniciais
│   │
│   └── test/
│       └── java/com/bcopstein/ex4_lancheriaddd_v1/
│           └── Ex4LancheriadddV1ApplicationTests.java
│
├── pom.xml                            # Configuração Maven
├── mvnw / mvnw.cmd                    # Maven Wrapper
└── README.md
```

## Camadas da Arquitetura DDD

- **Dominio**: Contém as entidades, agregados, repositórios (interfaces) e serviços de domínio
- **Aplicacao**: Orquestra os use cases (casos de uso), coordena fluxos de negócio
- **Adaptadores**: Implementações de repositórios (JDBC) e controladores (REST)

## Como executar

```bash
mvn clean install
mvn spring-boot:run
```

## Tecnologias

- Java 21
- Spring Boot 3.5.4
- Maven
- H2 Database
- PlantUML (para diagramas de arquitetura)
