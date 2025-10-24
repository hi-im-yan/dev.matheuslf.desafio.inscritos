# 🧠 Desafio Técnico – Sistema de Gestão de Projetos e Demandas

## 🚀 Como Começar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6.3 ou superior
- Docker e Docker Compose
- (Opcional) Sua IDE favorita (IntelliJ IDEA, Eclipse, etc.)

### 🛠️ Construindo o Projeto

1. **Clone o repositório**
   ```bash
   git clonehttps://github.com/hi-im-yan/dev.matheuslf.desafio.inscritos
   cd dev_matheuslf_desafio_inscritos
   ```

2. **Construa o projeto**
   ```bash
   # Usando Maven Wrapper (recomendado)
   ./mvnw clean install
   ```

### 🏃 Executando a Aplicação

#### Opção 1: Usando Banco de Dados H2 em Memória (Padrão)
```bash
docker-compose up -d
# Rodar a aplicação com o profile 'local' para fazer uso do container do postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
A aplicação será iniciada em `http://localhost:8080`

### 🧪 Testes

O projeto utiliza uma estratégia abrangente de testes, incluindo testes de integração que são executados em um banco de dados H2 em memória.

#### 🔧 Tecnologias Utilizadas
- **RestAssured**: Para testes de API de forma fluente e legível
- **JUnit 5**: Framework de testes
- **H2 Database**: Banco de dados em memória para testes de integração

#### 🏗️ Configuração de Teste
Os testes de integração utilizam o perfil `test`, que configura automaticamente:
- Banco de dados H2 em memória
- Schema gerado automaticamente a partir das entidades

#### ▶️ Executando os Testes

```bash
# Executa todos os testes (unidade e integração)
./mvnw test

# Executa apenas testes de integração
./mvnw test -Dgroups=integration

# Executa testes com relatório de cobertura (Jacoco)
./mvnw clean test jacoco:report
```

#### 🧩 Estrutura de Testes
```
src/test/java/
└── integration/              # Testes de integração
    ├── ProjectIntegrationTest.java
    └── TaskIntegrationTest.java
```

### 🌐 Documentação da API
Com a aplicação em execução, você pode acessar:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 🏗️ Arquitetura do Projeto

O projeto segue os princípios da **Clean Architecture**, organizado em camadas bem definidas:

```
src/main/java/dev/matheuslf/desafio/inscritos/
├── domain/                    # Camada de Domínio
│   ├── exceptions/           # Exceções de domínio
│   └── models/               # Entidades de domínio (Task, Project, etc.)
│
├── application/              # Camada de Aplicação
│   ├── exceptions/           # Exceções da camada de aplicação
│   ├── interfaces/           # Portas de entrada/saída
│   └── usecases/             # Casos de uso do sistema
│
└── infra/                    # Camada de Infraestrutura
    ├── configs/              # Configurações do Spring
    ├── controllers/          # Controladores REST
    ├── mappers/              # Mapeadores entre entidades e DTOs
    └── repositories/         # Implementações de repositórios JPA
```

### 🔹 Camada de Domínio
- Contém as entidades de negócio e suas regras
- Define os agregados e value objects
- Inclui exceções específicas do domínio
- Totalmente independente de frameworks e bibliotecas externas

### 🔹 Camada de Aplicação
- Implementa os casos de uso (use cases) do sistema
- Define interfaces (portas) para comunicação com o mundo externo
- Gerencia o fluxo de dados entre as camadas
- Contém a lógica de aplicação específica

### 🔹 Camada de Infraestrutura
- Implementa as interfaces definidas na camada de aplicação
- Lida com frameworks e bibliotecas externas (Spring, JPA, etc.)
- Contém controladores para expor a API REST
- Gerencia a persistência de dados
- Responsável pelo mapeamento entre entidades e DTOs

### Benefícios da Arquitetura
- **Desacoplamento**: Baixo acoplamento entre camadas
- **Testabilidade**: Fácil de testar cada componente isoladamente
- **Manutenibilidade**: Código organizado e de fácil manutenção
- **Escalabilidade**: Facilita a adição de novos recursos
- **Substituibilidade**: Componentes podem ser facilmente substituídos
- **Independência**: Domínio protegido de mudanças em frameworks