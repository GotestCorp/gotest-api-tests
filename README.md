# goTest API Tests

Suite de testes de API para a plataforma **goTest**, cobrindo os domínios de Auth, Users, Projects (membros, nós, suites, casos de teste, planos) e AccessManagement.

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java (JDK) | 11 |
| Maven | 3.8+ |
| Acesso à API goTest | Token JWT válido |

---

## Configuração

Edite `src/test/resources/config.properties` com as suas credenciais:

```properties
baseUrl=https://gotest.com.br/api/api
auth.token=eyJhbGci...    # Token JWT obtido pelo fluxo Google OAuth

# IDs padrão usados pelos comandos de DataSetup e pelos testes
default.project.id=seu-project-uuid
default.suite.id=seu-suite-uuid
```

> Qualquer propriedade pode ser sobrescrita via argumento Maven sem alterar o arquivo:
> ```
> mvn test -DbaseUrl=https://staging.gotest.com.br/api/api -Dauth.token=eyJ...
> ```

---

## Rodando os Testes

### Suite completa
```
mvn test
```

### Suite específica
```bash
# Apenas health checks
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng/testng-healthcheck.xml

# Apenas testes funcionais
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng/testng-functional.xml

# Apenas testes de contrato
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng/testng-contract.xml
```

### Relatório Allure
```bash
# Após rodar os testes
mvn allure:serve
```

---

## DataSetup — Criação e Exclusão de Dados em Bulk

O `DataSetup` é uma ferramenta CLI independente — **não executa a suite de testes**. Use para popular o ambiente com dados de teste ou limpar dados criados anteriormente.

### Sintaxe
```
mvn compile exec:java -Dexec.args="<comando> [argumentos]"
```

---

### Projetos

```bash
# Criar 5 projetos com dados aleatórios
mvn compile exec:java -Dexec.args="create-projects 5"

# Excluir projetos pelos IDs retornados
mvn compile exec:java -Dexec.args="delete-projects uuid1,uuid2,uuid3"
```

---

### Suites e Pastas

> Estes comandos usam o `default.project.id` definido em `config.properties`.
> Você pode sobrescrever: `-Ddefault.project.id=outro-uuid`

```bash
# Criar 3 pastas no projeto padrão
mvn compile exec:java -Dexec.args="create-folders 3"

# Criar 2 suites no projeto padrão
mvn compile exec:java -Dexec.args="create-suites 2"

# Excluir suites / pastas pelos IDs de nó
mvn compile exec:java -Dexec.args="delete-suites uuid1,uuid2"
mvn compile exec:java -Dexec.args="delete-folders uuid1,uuid2"
```

---

### Planos

```bash
# Criar 3 planos no projeto padrão
mvn compile exec:java -Dexec.args="create-plans 3"

# Excluir planos
mvn compile exec:java -Dexec.args="delete-plans uuid1,uuid2"
```

---

### Casos de Teste

#### Criação simples (nomes aleatórios)

> Usa `default.project.id` + `default.suite.id` de `config.properties`.

```bash
mvn compile exec:java -Dexec.args="create-test-cases 10"
```

#### Criação inline com títulos definidos

Informe `projectId`, `suiteId` e os títulos separados por `|`.
Atributos opcionais por caso são adicionados após vírgula no formato `chave=valor`.

```bash
mvn compile exec:java -Dexec.args="create-test-cases-bulk proj-uuid suite-uuid \"Login com sucesso|Login invalido,priority=HIGH|Login sem senha,status=DRAFT\""
```

**Atributos disponíveis por caso:**

| Atributo | Valores aceitos |
|---|---|
| `priority` | `HIGH`, `MEDIUM`, `LOW` |
| `status` | `IN_PROGRESS`, `DRAFT`, `DEPRECATED` |
| `testType` | `SCENARIO`, `STEP_BY_STEP` |
| `executionType` | `MANUAL`, `AUTOMATED` |

#### Criação via arquivo JSON (recomendado para muitos casos)

```bash
mvn compile exec:java -Dexec.args="create-test-cases-json src/test/resources/bulk-test-cases-sample.json"

# Ou com caminho absoluto
mvn compile exec:java -Dexec.args="create-test-cases-json C:/meus-dados/casos-sprint-12.json"
```

**Estrutura do arquivo JSON:**

```json
[
  {
    "projectId": "uuid-do-projeto",
    "suiteId": "uuid-da-suite",
    "defaults": {
      "testType": "SCENARIO",
      "status": "IN_PROGRESS",
      "priority": "MEDIUM",
      "executionType": "MANUAL"
    },
    "cases": [
      { "title": "Login com credenciais validas", "priority": "HIGH" },
      { "title": "Login com senha incorreta",     "priority": "HIGH" },
      { "title": "Login com e-mail invalido" },
      { "title": "Login com campos em branco",    "status": "DRAFT"  }
    ]
  },
  {
    "projectId": "uuid-do-projeto",
    "suiteId": "uuid-de-outra-suite",
    "defaults": {
      "status": "DRAFT",
      "priority": "MEDIUM"
    },
    "cases": [
      { "title": "Cadastro com dados completos", "status": "IN_PROGRESS", "priority": "HIGH" },
      { "title": "Cadastro com e-mail duplicado" }
    ]
  }
]
```

**Como funciona:**
- Cada elemento do array é um **grupo** (um destino: `projectId` + `suiteId`).
- O bloco `defaults` define valores padrão para **todos** os casos do grupo.
- Campos definidos individualmente em cada caso **sobrescrevem** os defaults.
- Você pode ter **múltiplos grupos** apontando para projetos/suites diferentes no mesmo arquivo.
- O arquivo de exemplo está em `src/test/resources/bulk-test-cases-sample.json`.

---

### Estrutura Completa de Projeto

Cria de uma vez: 1 projeto + 3 pastas + 2 suites por pasta + 5 casos por suite + 2 planos.

```bash
mvn compile exec:java -Dexec.args="create-full-project"
```

---

## Estrutura do Projeto

```
src/
├── main/java/br/com/gotest/apitests/
│   ├── builder/          # Builders de request (cenários válidos e inválidos)
│   │   ├── ProjectRequestBuilder.java
│   │   ├── FolderRequestBuilder.java
│   │   ├── TestCaseRequestBuilder.java
│   │   └── PlanRequestBuilder.java
│   ├── client/           # Clientes REST por domínio (um por recurso da API)
│   │   ├── BaseApiClient.java
│   │   ├── ProjectClient.java
│   │   ├── NodeClient.java
│   │   ├── TestCaseClient.java
│   │   ├── PlanClient.java
│   │   ├── ExecutionClient.java
│   │   ├── UserClient.java
│   │   └── AccessManagementClient.java
│   ├── config/           # Configuração centralizada
│   │   ├── ConfigManager.java
│   │   └── RestAssuredSpecs.java
│   ├── endpoint/         # Constantes e métodos de path da API
│   │   └── Endpoints.java
│   ├── fixture/          # Helpers para criação/exclusão de dados em bulk
│   │   ├── DataSetup.java          <- ponto de entrada CLI
│   │   ├── ProjectFixture.java
│   │   ├── FolderFixture.java
│   │   ├── SuiteFixture.java
│   │   ├── TestCaseFixture.java    <- suporta bulk e JSON
│   │   ├── PlanFixture.java
│   │   └── FixtureException.java
│   ├── model/
│   │   ├── bulk/         # Modelos para criação bulk de casos de teste
│   │   │   ├── BulkTestCaseGroup.java
│   │   │   ├── BulkTestCaseDefinition.java
│   │   │   └── BulkTestCaseDefaults.java
│   │   ├── request/      # Payloads de requisição
│   │   └── response/     # Modelos de resposta
│   └── util/             # Utilitários (DataFaker, etc.)
│
└── test/java/br/com/gotest/apitests/
    ├── base/             # BaseTest com todos os clients injetados
    ├── healthcheck/      # Smoke tests — verifica se os endpoints respondem
    ├── contract/         # Testes de contrato — valida campos e tipos da resposta
    ├── functional/       # Testes funcionais — fluxos completos de CRUD
    └── listener/         # TestListener para log e Allure
```

---

## Adicionando Novos Testes

1. **Crie o client** em `client/` estendendo `BaseApiClient` se o domínio ainda não existir.
2. **Crie o endpoint** em `Endpoints.java` como método estático.
3. **Crie o builder** de request em `builder/` com factory methods `valid()`, `invalidXxx()`.
4. **Crie a classe de teste** no pacote adequado (`healthcheck`, `contract` ou `functional`) estendendo `BaseTest`.
5. **Registre** a nova classe (ou seu pacote) no XML de suite correspondente em `src/test/resources/testng/`.

---

## Stack Tecnológica

| Biblioteca | Uso |
|---|---|
| RestAssured 5.4.0 | HTTP client para testes de API |
| TestNG 7.10.2 | Framework de testes |
| Allure 2.27.0 | Relatórios de execução |
| Jackson | Serialização/deserialização JSON |
| Lombok | Redução de boilerplate |
| JavaFaker | Geração de dados aleatórios |
| AssertJ | Asserções fluentes |
| Logback / SLF4J | Logging |
