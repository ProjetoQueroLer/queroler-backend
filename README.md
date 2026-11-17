# 📚 Projeto Quero Ler - API Backend

A **API Quero Ler** é uma plataforma inspirada na rede social Skoob, desenvolvida para gerenciar bibliotecas pessoais, monitorar leituras e conectar leitores.

---

## 🛠️ Tecnologias e Ferramentas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.x
- **Banco de Dados:** PostgreSQL (Produção/Dev)
- **Migrações:** Flyway
- **Segurança:** Spring Security + JWT (Cookie HttpOnly)
- **Documentação:** Swagger (OpenAPI 3) — disponível em `/swagger-ui.html`
- **Containers:** Docker

---

## 🔐 Segurança e Autenticação

A API utiliza **Spring Security** com estratégia **Stateless** via tokens JWT armazenados em **Cookie HttpOnly**, prevenindo ataques XSS.

### Fluxo de autenticação

1. **POST `/logins`** — envia `user` e `senha`, recebe um cookie `jwt` (HttpOnly) e um body `{"primeiroLogin": boolean}`
2. Para usuários admin/moderador com `primeiroLogin: true`, deve-se trocar a senha via **PUT `/usuarios/alterar-senha`**
3. Nas demais requisições, o cookie `jwt` é enviado automaticamente pelo navegador. Para testes com curl, use o header `Authorization: Bearer <token>` extraído do cookie

### Perfis de usuário

| Perfil | Acesso |
|--------|--------|
| `LEITOR` | Acesso à plataforma, cadastro de livros, diário de leitura, metas |
| `MODERADOR` | Acesso de leitor + moderação de resenhas |
| `ADMINISTRADOR` | Acesso de moderador + gerenciamento de documentos e exclusão de usuários |

### Credenciais padrão (pré-cadastradas)

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Administrador | `administrador@queroler.com` | `Admin123@` |
| Moderador | `moderador@queroler.com` | `Moderador123@` |

---

## 📡 API Reference

### Autenticação

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/logins` | ❌ Público | Realiza login e retorna cookie JWT + flag `primeiroLogin` |

**Request:**
```json
{ "user": "email@exemplo.com", "senha": "MinhaSenha123@" }
```

**Response:**
```json
{ "primeiroLogin": false }
```

---

### Usuários

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/usuarios` | ❌ Público | Cadastrar novo leitor (multipart: `dados` JSON + `imagem` opcional) |
| GET | `/usuarios` | ✅ | Retorna dados do usuário autenticado |
| PUT | `/usuarios` | ✅ | Atualizar perfil (multipart) |
| PUT | `/usuarios/dados-adicionais` | ✅ | Inserir dados complementares (cidade, estado, país + foto opcional) |
| PUT | `/usuarios/alterar-senha` | ✅ | Alterar senha |
| PUT | `/usuarios/administrador` | ✅ | Atualizar perfil por administrador |
| DELETE | `/usuarios` | ✅ | Excluir próprio perfil (apenas leitor) |
| GET | `/usuarios/foto` | ✅ | Obter foto do perfil |

**POST `/usuarios` (multipart):**
- `dados`: JSON com `{ "nome", "email", "senha", "cpf", "dataDeNascimento", "checkTermo" }`
- `imagem`: arquivo de foto (opcional, até 10MB, formatos JPG/PNG/JPEG)

**PUT `/usuarios/alterar-senha`:**
```json
{ "senhaAtual": "Admin123@", "senhaNova": "NovaSenha123@" }
```

**PUT `/usuarios/dados-adicionais` (multipart):**
- `dados`: JSON com `{ "cidade", "estado", "pais" }`
- `imagem`: arquivo de foto (opcional)

---

### Livros

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/livros` | ✅ | Cadastrar novo livro (multipart: `dados` JSON + `imagem` capa opcional) |
| GET | `/livros` | ✅ | Buscar livros (query params: `titulo`, `editora`, `autor`) |
| GET | `/livros/populares` | ✅ | Listar top 5 livros mais adicionados |
| GET | `/livros/{id}` | ✅ | Detalhar livro com estatísticas (avaliação, contagens, resenhas públicas) |
| GET | `/livros/buscar/{isbn}` | ✅ | Buscar livro por ISBN |
| GET | `/livros/{id}/capa` | ✅ | Obter imagem da capa |
| PUT | `/livros/{id}/capa` | ✅ | Inserir/atualizar capa (multipart) |
| GET | `/livros/tela_de_leitura` | ✅ | Listar livros do usuário para tela de leitura |
| GET | `/livros/detalhados` | ✅ | Listar livros detalhados do usuário |

**POST `/livros` (multipart):**
- `dados`: JSON com:
```json
{
  "titulo": "Dom Casmurro",
  "isbn": "9788543107163",
  "editora": "Sextante",
  "anoDePublicacao": 2000,
  "numeroDePaginas": 512,
  "idioma": "PORTUGUES",
  "sinopse": "Texto com no mínimo 50 caracteres...",
  "autores": [{ "nome": "Machado de Assis" }]
}
```
- `imagem`: arquivo de capa (opcional, até 10MB, JPG/PNG)

**GET `/livros/{id}`** — retorna:
```json
{
  "urlCapaDoLivro": "/livros/1/capa",
  "titulo": "Dom Casmurro",
  "editora": "Sextante",
  "anoDePublicacao": 2000,
  "numeroDePaginas": 512,
  "idioma": "PORTUGUES",
  "isbn": "9788543107163",
  "sinopse": "...",
  "dataDeCadastro": "08/03/2026 11:30:00",
  "autores": [{ "id": 1, "nome": "Machado de Assis" }],
  "mediaAvaliacao": 4.5,
  "totalAvaliacoes": 12,
  "quantidadeQueremLer": 8,
  "quantidadeEstaoLendo": 3,
  "quantidadeJaLeRAM": 10,
  "quantidadeAbandonaram": 1,
  "resenhas": [
    {
      "nomeDoAutor": "João",
      "tituloDaResenha": "Incrível",
      "resenha": "Texto da resenha...",
      "spoiler": false,
      "nota": 5.0,
      "data": "10/03/2026 15:30:00"
    }
  ]
}
```

---

### Leitura (Estante)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/leituras` | ✅ | Adicionar livro à estante com status |
| DELETE | `/leituras/{livroId}` | ✅ | Remover livro da estante |

**POST `/leituras`:**
```json
{
  "livroId": 1,
  "status": "LIVROS_QUE_QUERO_LER"
}
```

**Status disponíveis:** `LIVROS_QUE_QUERO_LER`, `LIVROS_QUE_ESTOU_LENDO`, `RELENDO`, `LIVROS_LIDOS`, `LIVROS_ABANDONADOS`

---

### Diário de Leitura

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/diario` | ✅ | Criar diário de leitura para um livro |
| GET | `/diario?livroId={id}` | ✅ | Buscar diário de leitura do livro |
| PUT | `/diario/{id}` | ✅ | Atualizar diário de leitura |
| POST | `/leituras/{diarioId}/comentarios` | ✅ | Adicionar acompanhamento (páginas lidas + comentário) |

**POST `/diario`:**
```json
{
  "livroId": 1,
  "inicioDaLeitura": "08/03/2026 10:00:00",
  "terminoDaLeitura": null
}
```

**POST `/leituras/{diarioId}/comentarios`:**
```json
{
  "paginaInicial": 1,
  "paginaFinal": 50,
  "comentario": "Comecei a leitura, ótimo início!"
}
```

---

### Metas de Leitura

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/metas` | ✅ | Criar meta de leitura para o ano |
| PUT | `/metas` | ✅ | Atualizar meta de leitura |
| DELETE | `/metas` | ✅ | Excluir meta de leitura |

**POST/PUT `/metas`:**
```json
{
  "ano": 2026,
  "metaLivrosAno": 24,
  "metaLivrosMes": 2,
  "metaPaginasDia": 30
}
```
> Todos os campos de meta são opcionais. Se não informado o ano, assume o ano corrente.

---

### Documentos

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/documentos` | ✅ (Admin) | Criar documento (ex: Termos de Uso) |
| GET | `/documentos/termos-gerais-de-uso` | ❌ Público | Obter termos de uso vigentes |
| PUT | `/documentos/{id}` | ✅ (Admin) | Atualizar documento |

**POST/PUT `/documentos`:**
```json
{
  "titulo": "Termos Gerais de Uso",
  "tipo": "TERMOS_GERAIS_DE_USO",
  "conteudo": "Texto do documento..."
}
```

---

### Notificações

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| GET | `/notificacoes` | ✅ | Listar notificações dos últimos 30 dias (ordenadas da mais recente) |
| PUT | `/notificacoes` | ✅ | Marcar todas como lidas |

**GET `/notificacoes`** — retorna:
```json
{
  "content": [
    {
      "id": 1,
      "notificacao": "Termos de uso atualizados.",
      "dataDeCriacao": "08/03/2026 11:30:00",
      "visualizada": false
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- Docker e Docker Compose
- Java 21 e Maven (ou `./mvnw`)

### Utilizando Docker Compose

1. Configure o arquivo `.env` na raiz do projeto:
```bash
POSTGRES_DB=db_quero_ler_v2
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_quero_ler_v2
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

2. Inicie os containers:
```bash
docker compose up -d --build
```

3. A API estará disponível em `http://localhost:8080`

### Rodar pela IDE/Terminal (apenas PostgreSQL no Docker)

1. Suba apenas o banco:
```bash
docker compose up -d --build db
```

2. Execute a aplicação com profile local:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## 🧪 Testes

```bash
./mvnw clean test
```

---

## 📖 Exemplos de uso com curl

### 1. Login

```bash
curl -i -X POST 'http://localhost:8080/logins' \
  -H 'Content-Type: application/json' \
  -d '{"user":"administrador@queroler.com","senha":"Admin123@"}' \
  | grep -i "jwt"
```

Extraia o token JWT do header `set-cookie` e use nas demais requisições.

### 2. Buscar livros

```bash
TOKEN="seu_token_jwt_aqui"
curl -X GET 'http://localhost:8080/livros?titulo=Dom' \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Detalhar livro

```bash
curl -X GET 'http://localhost:8080/livros/1' \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Cadastrar livro

```bash
curl -X POST 'http://localhost:8080/livros' \
  -H "Authorization: Bearer $TOKEN" \
  -F 'dados={
    "titulo": "Dom Casmurro",
    "isbn": "9788543107163",
    "editora": "Sextante",
    "anoDePublicacao": 2000,
    "numeroDePaginas": 256,
    "idioma": "PORTUGUES",
    "sinopse": "Uma das obras mais importantes da literatura brasileira, Dom Casmurro narra a história de Bentinho e Capitu.",
    "autores": [{"nome": "Machado de Assis"}]
  }' \
  -F 'imagem=@/caminho/para/capa.jpg'
```

### 5. Adicionar livro à estante

```bash
curl -X POST 'http://localhost:8080/leituras' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"livroId": 1, "status": "LIVROS_QUE_QUERO_LER"}'
```

### 6. Criar diário de leitura

```bash
curl -X POST 'http://localhost:8080/diario' \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "livroId": 1,
    "inicioDaLeitura": "08/03/2026 10:00:00"
  }'
```

---

## 🔔 Sistema de Notificações

O sistema de notificações segue uma arquitetura de **broadcast** com controle de leitura por usuário.

- **`Notificacao`**: mensagem e data de criação. Uma única notificação é enviada para todos os usuários.
- **`UsuarioNotificacao`**: vincula a notificação a um usuário, controlando se foi visualizada.

**Comportamento:**
- Notificações com mais de 30 dias são excluídas automaticamente
- O campo `visualizada` no GET permite diferenciar lidas de não lidas
- Ordenação da mais recente para a mais antiga
- Quando um documento é atualizado, uma notificação é criada para todos os usuários

---

## 🤝 Contribuir

Clone o repositório e faça checkout na branch **develop**.

### Convenção de branches

```
tipo/descrição-curta
```

| Tipo | Descrição |
|------|-----------|
| `ci` | Fluxos de integração contínua |
| `docs` | Documentação |
| `fix` | Correção de bugs |
| `feat` | Nova funcionalidade |
| `refactor` | Refatoração de código |

**Exemplo:** `feat/cadastro-usuario`

### Convenção de commits

```
tipo(escopo): mensagem curta
```

**Exemplo:** `feat(infra): Adicionar Dockerfile da aplicação`
