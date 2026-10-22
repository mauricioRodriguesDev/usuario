# 👤 Microsserviço de Gerenciamento de Usuários (API Usuário)

Uma API RESTful desenvolvida com **Java 17** e **Spring Boot 3**, responsável pelo gerenciamento de usuários, incluindo fluxo de cadastro, autenticação com segurança via **JWT (JSON Web Token)**, controle de credenciais criptografadas com **BCrypt**, e gestão de múltiplos endereços e telefones associados.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.5.7
- **Segurança & Autenticação:** Spring Security + JWT (`io.jsonwebtoken:jjwt`) + BCrypt
- **Persistência de Dados:** Spring Data JPA / Hibernate
- **Banco de Dados:** PostgreSQL
- **Build Tool:** Gradle (com Wrapper `gradlew`)
- **Produtividade:** Lombok
- **CI/CD:** GitHub Actions (Workflow de build com Gradle)

---

## 📌 Funcionalidades

- **Cadastro de Usuário:** Criação de novos usuários com verificação de duplicidade de e-mail e hash de senha.
- **Autenticação (Login):** Endpoint de login que autentica o usuário e gera token JWT Bearer.
- **Segurança Stateless:** Proteção dos endpoints privados exigindo o envio do cabeçalho `Authorization: Bearer <token>`.
- **Busca e Exclusão:** Consulta e remoção de usuários através do e-mail.
- **Atualização de Perfil:** Atualização de dados cadastrais autenticados com extração automática do usuário pelo token JWT.
- **Gestão de Endereços:** Cadastro e atualização de múltiplos endereços vinculados ao usuário.
- **Gestão de Telefones:** Cadastro e atualização de múltiplos telefones de contato vinculados ao usuário.

---

## 🏛️ Arquitetura do Projeto

O projeto adota uma separação em camadas bem estruturada:

```text
com.javanauta.usuario
├── business/               # Regras de negócio, DTOs e Conversores (Mappers)
│   ├── converter/          # Mapeamento entre Entidades e DTOs
│   ├── dto/                # Data Transfer Objects (UsuarioDTO, EnderecoDTO, TelefoneDTO)
│   └── UsuarioService.java # Lógica de negócio e orquestração dos repositórios
├── controller/             # Controladores REST expostos
│   └── UsuarioController.java
└── infrastructure/         # Infraestrutura técnica e acesso a dados
    ├── entity/             # Entidades JPA (Usuario, Endereco, Telefone)
    ├── exceptions/         # Tratamento de exceções (ConflictException, ResourceNotFoundException)
    ├── repository/         # Interfaces Spring Data JPA
    └── security/           # Filtros JWT, Provedor de Autenticação e Configurações de Segurança
```

---

## 📊 Modelo de Dados

### Entidade `Usuario`

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` (PK, auto-increment) | Identificador único |
| `nome` | `String` (max 100) | Nome completo do usuário |
| `email` | `String` (max 100) | E-mail (usado como username para autenticação) |
| `senha` | `String` | Senha criptografada com BCrypt |
| `telefones` | `List<Telefone>` | Relacionamento 1:N com Telefone |
| `enderecos` | `List<Endereco>` | Relacionamento 1:N com Endereco |

### Entidade `Endereco`

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` (PK) | Identificador único |
| `rua` | `String` | Logradouro |
| `numero` | `Long` | Número |
| `complemento` | `String` | Complemento (apto, bloco, etc.) |
| `cidade` | `String` | Cidade |
| `estado` | `String` | Estado / UF |
| `cep` | `String` | CEP |

### Entidade `Telefone`

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` (PK) | Identificador único |
| `ddd` | `String` | Código DDD |
| `numero` | `String` | Número do telefone |

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [PostgreSQL](https://www.postgresql.org/) (em execução na porta padrão `5432`)
- [Git](https://git-scm.com/)

---

## ⚙️ Configuração do Banco de Dados

Crie a base de dados no PostgreSQL:

```sql
CREATE DATABASE db_usuario;
```

As configurações devem ser definidas preferencialmente via variáveis de ambiente ou ajustadas no arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=usuario
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/db_usuario}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 🔒 Variáveis de Ambiente Recomendadas

Para evitar a exposição de credenciais em texto claro nos arquivos de configuração, defina as variáveis no seu terminal antes de executar:

- **Linux / macOS:**
  ```bash
  export DB_URL=jdbc:postgresql://localhost:5432/db_usuario
  export DB_USERNAME=postgres
  export DB_PASSWORD=sua_senha_segura
  ```

- **Windows (PowerShell):**
  ```powershell
  $env:DB_URL="jdbc:postgresql://localhost:5432/db_usuario"
  $env:DB_USERNAME="postgres"
  $env:DB_PASSWORD="sua_senha_segura"
  ```

> ⚠️ **Importante:** Nunca versione credenciais de banco de dados no seu repositório. A propriedade `ddl-auto=update` cria/atualiza as tabelas automaticamente ao iniciar a aplicação.

---

## 🛠️ Como Executar a Aplicação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/mauricioRodriguesDev/usuario.git
   cd usuario
   ```

2. **Compile e execute com o Gradle Wrapper:**
    - **Linux / macOS:**
      ```bash
      ./gradlew bootRun
      ```
    - **Windows:**
      ```powershell
      .\gradlew.bat bootRun
      ```

A aplicação estará acessível por padrão em: `http://localhost:8080`

---

## 🛣️ Endpoints da API

### 🔓 Endpoints Públicos (não requerem autenticação)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/usuario` | Realiza o cadastro de um novo usuário |
| `POST` | `/usuario/login` | Realiza autenticação com e-mail/senha e devolve o Token JWT |

#### Exemplo de Payload — Cadastro (`POST /usuario`)

```json
{
  "nome": "João da Silva",
  "email": "joao@email.com",
  "senha": "senhaSegura123",
  "enderecos": [
    {
      "rua": "Av. Brasil",
      "numero": 1000,
      "complemento": "Apto 101",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01000-000"
    }
  ],
  "telefones": [
    {
      "ddd": "11",
      "numero": "999999999"
    }
  ]
}
```

#### Exemplo de Payload — Login (`POST /usuario/login`)

```json
{
  "email": "joao@email.com",
  "senha": "senhaSegura123"
}
```

**Resposta:**
```
Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 🔒 Endpoints Privados (requerem cabeçalho `Authorization: Bearer <token>`)

| Método | Endpoint | Parâmetros / Headers | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/usuario` | `?email=joao@email.com` | Busca usuário por e-mail |
| `PUT` | `/usuario` | Header `Authorization` | Atualiza dados cadastrais do usuário logado |
| `DELETE` | `/usuario/{email}` | Path variable `{email}` | Exclui usuário pelo e-mail |
| `POST` | `/usuario/endereco` | Header `Authorization` | Cadastra um novo endereço para o usuário logado |
| `PUT` | `/usuario/endereco` | `?id={idEndereco}` | Atualiza um endereço existente pelo ID |
| `POST` | `/usuario/telefone` | Header `Authorization` | Cadastra um novo telefone para o usuário logado |
| `PUT` | `/usuario/telefone` | `?id={idTelefone}` | Atualiza um telefone existente pelo ID |

---

## 🔐 Fluxo de Autenticação

```text
┌──────────┐     POST /usuario/login      ┌──────────────┐
│  Cliente  │ ──────────────────────────▶  │   API REST   │
│           │  { email, senha }            │              │
│           │                              │  Valida com  │
│           │  ◀──────────────────────────  │  AuthManager │
│           │  Bearer eyJhbGciOi...         │  + BCrypt    │
└──────────┘                               └──────────────┘
      │
      │  Requisições subsequentes
      │  Header: Authorization: Bearer <token>
      ▼
┌──────────────┐
│ JwtRequest   │  Extrai e valida o token JWT
│ Filter       │  em cada requisição protegida
└──────────────┘
```

1. O cliente envia `email` e `senha` para `/usuario/login`.
2. O `AuthenticationManager` valida as credenciais contra o banco (senha verificada via BCrypt).
3. Se válido, a API retorna um token JWT (`Bearer ...`).
4. Para acessar endpoints protegidos, o cliente envia o token no header `Authorization`.
5. O `JwtRequestFilter` intercepta cada requisição, extrai/valida o token e autentica o usuário na `SecurityContext`.

---

## 🧪 Testes

Para executar a suíte de testes:

```bash
# Linux / macOS
./gradlew test

# Windows
.\gradlew.bat test
```

---

## 📂 Estrutura Completa de Arquivos

```text
usuario/
├── .github/
│   └── workflows/
│       └── gradle.yml                        # CI/CD com GitHub Actions
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   └── main/
│       ├── java/com/javanauta/usuario/
│       │   ├── UsuarioApplication.java       # Classe principal Spring Boot
│       │   ├── business/
│       │   │   ├── UsuarioService.java       # Serviço com regras de negócio
│       │   │   ├── converter/
│       │   │   │   └── UsuarioConverter.java  # Mapper Entidade ↔ DTO
│       │   │   └── dto/
│       │   │       ├── UsuarioDTO.java
│       │   │       ├── EnderecoDTO.java
│       │   │       └── TelefoneDTO.java
│       │   ├── controller/
│       │   │   └── UsuarioController.java    # REST Controller
│       │   └── infrastructure/
│       │       ├── entity/
│       │       │   ├── Usuario.java          # Entidade JPA (implements UserDetails)
│       │       │   ├── Endereco.java
│       │       │   └── Telefone.java
│       │       ├── exceptions/
│       │       │   ├── ConflictException.java
│       │       │   └── ResourceNotFoundException.java
│       │       ├── repository/
│       │       │   ├── UsuarioRepository.java
│       │       │   ├── EnderecoRepository.java
│       │       │   └── TelefoneRepository.java
│       │       └── security/
│       │           ├── SecurityConfig.java        # Configuração Spring Security
│       │           ├── JwtUtil.java                # Geração e validação de tokens JWT
│       │           ├── JwtRequestFilter.java       # Filtro de interceptação JWT
│       │           └── UserDetailsServiceImpl.java # Carregamento de usuário para autenticação
│       └── resources/
│           └── application.properties        # Configurações da aplicação
├── .gitattributes
├── .gitignore
├── build.gradle                              # Dependências e plugins Gradle
├── gradlew                                   # Gradle Wrapper (Linux/macOS)
├── gradlew.bat                               # Gradle Wrapper (Windows)
└── settings.gradle                           # Configurações do projeto Gradle
```

---

## 📄 Licença

Este projeto é de uso livre para fins de estudo e desenvolvimento.

---

## 👨‍💻 Autor

Desenvolvido por [mauricioRodriguesDev](https://github.com/mauricioRodriguesDev).
