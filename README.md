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

#### Utilizando docker compose

1. **Iniciar as instancia da aplicação, e, do banco de dados (docker-compose):"**
    ```bash
    docker compose up -d --build
    ```
2. **Verificar se as instancia estão em execução:**
    ```bash
    docker ps 
    ```
3. ** Deve retornar ao menos 2 instancias:**

|CONTAINER ID|IMAGE|COMMAND|CREATED|STATUS|PORTS|NAMES|
|------------|--------------------|----------------------|---------------|-----------|-------------------------------------------|------------| 
|90bb0deec9c9|queroler-backend-api|"sh -c 'java $JAVA_O…"|49 minutes ago|Up 5 seconds|0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp|api-queroler|
|fe6a83a4f7fe|postgres:latest|"docker-entrypoint.s…"|49 minutes ago|Up 16 seconds (healthy)|0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp|postgres-queroler|
---

## 🧪 Testes

Para garantir a qualidade e integridade do código, execute a suíte de testes unitários e de integração:

```bash
./mvnw clean test
```
## Api Railway

- Fazendo requisições na api via curl.
1. Fazer login
```bash
  curl -i -X 'POST' \
  'https://queroler-backend-production.up.railway.app/logins' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "user":"admin@administrator.com",
  "senha": "Admin123@root"
}' | grep "jwt"
```

Como foi usado o commando grep do linux, esperamos receber como resposta a informação set-cookie com a chave jwt. A chava vamos usar para chamar o nosso proximo endpoit para listar os livros.

```bash
set-cookie: jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJhZG1pbkBhZG1pbmlzdHJhdG9yLmNvbSIsInJvbGUiOiJMRUlUT1IiLCJleHAiOjE3NzUxMjQ1Njh9.wa5v3v_beTIBCWaGpBniF4Nc6hIWEWTgI2QM6_LP8E4; Path=/; Max-Age=7200; Expires=Thu, 02 Apr 2026 07:09:28 GMT; HttpOnly; SameSite=Strict
```

2. Cadastrar livro

Vamos popular a nossa requisição coms as informações dos dados no formulario, alem do arquivo que servira de imagem para capa do livro. O header **Authorization** deve ser informado com a chave jwt que recebemos no login. Dessa forma: `  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJhZG1pbkBhZG1pbmlzdHJhdG9yLmNvbSIsInJvbGUiOiJMRUlUT1IiLCJleHAiOjE3NzUxMjM1MzN9.936-ke4lRqzNhorABVboVl8PGcWLstVMghobi2UKyI8'`
Ficamos com a seguinte request.
```bash
 curl -i --request POST \
  --url 'https://queroler-backend-production.up.railway.app/livros' \
  --header 'Content-Type: multipart/form-data' \
  --header 'User-Agent: insomnia/12.3.1' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJhZG1pbkBhZG1pbmlzdHJhdG9yLmNvbSIsInJvbGUiOiJMRUlUT1IiLCJleHAiOjE3NzUxMjM1MzN9.936-ke4lRqzNhorABVboVl8PGcWLstVMghobi2UKyI8' \
  --form 'dados={
  "titulo": "Scrum: A arte de fazer o dobro do trabalho na metade do tempo",
  "isbn": "8543107164",
  "editora": "Sextante",
  "anoDePublicacao": "2000",
  "numeroDePaginas": 512,
  "idioma": "PORTUGUES",
  "sinopse": "Repleto de histórias empolgantes e exemplos reais. O método de gerenciamento de projetos conhecido como Scrum deve ser a ferramenta de produtividade mais largamente empregada entre as empresas de alta tecnologia. Jeff Sutherland tem sido brilhantemente bem-sucedido em sua missão de pôr esse recurso nas mãos de mais negócios em todo o mundo.",
  "autores": [
    {
      "nome": "Emily Bronte"
    }
  ]
}' \
  --form 'imagem=@/home/renanalves/Imagens/imagem.png'
```
Devemos receber um status 200 confirmando que nossa requisição foi processada com sucesso.
