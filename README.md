# 📚 Projeto Quero Ler - API Backend

A **API Quero Ler** é uma plataforma inspirada na rede social Skoob, desenvolvida para gerenciar bibliotecas pessoais, monitorar leituras e conectar leitores. O projeto foca em segurança robusta e boas práticas de arquitetura Java.

---

## 🛠️ Tecnologias e Ferramentas

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.x
*   **Banco de Dados:** PostgreSQL (Produção/Dev), H2 (Testes)
*   **Migrações:** Flyway
*   **Segurança:** Spring Security + JWT (Cookie HttpOnly)
*   **Documentação:** Swagger (OpenAPI 3)
*   **Containers:** Docker

---

## 🔐 Segurança e Autenticação

O projeto utiliza **Spring Security** com uma estratégia **Stateless** via tokens JWT. Um diferencial desta implementação é o uso de **Cookies HttpOnly**, o que aumenta a segurança contra ataques XSS ao impedir que o JavaScript do front-end acesse o token diretamente.

### Modelo de Dados de Usuário
A estrutura de segurança é dividida em duas entidades para separação de responsabilidades:

1.  **`User`**: Entidade de infraestrutura que implementa `UserDetails`. Gerencia credenciais e perfis de acesso (`ROLE_USER`, `ROLE_ADMIN`).
2.  **`Usuario`**: Entidade de domínio que armazena informações cadastrais, perfil social e relacionamentos (livros e notificações).

### Configuração de Segurança (SecurityConfig)
A gerência e o ciclo de vida do token de autenticação estão concentrados no backend, removendo a responsabilidade do front-end de armazenar ou validar o token manualmente.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
*   Docker e Docker Compose
*   Java 21 e Maven (ou Maven Wrapper `./mvnw`)

### Passo a Passo

1.  **Subir o Banco de Dados (Docker):**
    ```bash
    docker run --name postgres-queroler -e POSTGRES_PASSWORD=suasenha -p 5432:5432 -d postgres
    ```

2.  **Compilar e Instalar as dependências:**
    ```bash
    ./mvnw clean install
    ```

3.  **Executar a Aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 🧪 Testes

Para garantir a qualidade e integridade do código, execute a suíte de testes unitários e de integração:

```bash
./mvnw clean test
