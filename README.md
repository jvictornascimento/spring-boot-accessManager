# Bear Flow AccessManager Weather

API Spring Boot para demonstrar autenticacao com Spring Security, login com JWT, refresh token em cookie `HttpOnly`, CRUD simples de usuarios e consumo autenticado da OpenWeather por cidade usando Spring Cloud OpenFeign.

O projeto esta em monorepo: o backend fica na raiz e o frontend Next.js fica em `frontend/`. O foco tecnico principal e o backend, e o frontend demonstra o fluxo completo para avaliacao.

## Estrutura

```text
.
|-- src/                    # Backend Spring Boot
|-- frontend/               # Frontend Next.js
|-- docker-compose.yml      # Stack PostgreSQL + backend + frontend
|-- Dockerfile              # Container do backend
`-- README.md
```

## Stack

Backend:

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Spring Cloud OpenFeign
- Flyway
- H2 em `dev` e `test`
- PostgreSQL em `prod`
- JWT com JJWT
- Lombok
- Maven Wrapper

Frontend:

- Next.js 16
- React
- TypeScript
- Tailwind CSS
- Axios
- Lucide React

Infra:

- Docker
- Docker Compose
- PostgreSQL em container para `prod`

## Profiles

O profile padrao e `dev`.

| Profile | Banco | Uso |
| --- | --- | --- |
| `dev` | H2 em arquivo local | Desenvolvimento local |
| `test` | H2 em memoria | Testes automatizados |
| `prod` | PostgreSQL | Container/producao |

## Variaveis de ambiente

Nenhum secret real deve ser versionado. Configure os valores sensiveis no ambiente do servidor, CI ou container.

| Variavel | Obrigatoria em prod | Descricao |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | Nao | Profile ativo. Ex.: `dev`, `test`, `prod` |
| `DATABASE_URL` | Sim | JDBC URL do PostgreSQL |
| `DATABASE_USERNAME` | Sim | Usuario do PostgreSQL |
| `DATABASE_PASSWORD` | Sim | Senha do PostgreSQL |
| `JWT_SECRET` | Sim | Chave simetrica do JWT com pelo menos 32 caracteres |
| `JWT_ACCESS_TOKEN_EXPIRATION_MINUTES` | Nao | Expiracao do access token |
| `JWT_REFRESH_TOKEN_EXPIRATION_DAYS` | Nao | Expiracao do refresh token |
| `JWT_REFRESH_COOKIE_SECURE` | Nao | Define `Secure` no cookie |
| `JWT_REFRESH_COOKIE_SAME_SITE` | Nao | Valor do `SameSite` do cookie |
| `OPENWEATHER_API_URL` | Nao | URL base da OpenWeather |
| `OPENWEATHER_API_KEY` | Sim | Chave da OpenWeather |

## Banco e migrations

As migrations ficam em `src/main/resources/db/migration`.

- `V1__create_users_table.sql`: cria a tabela `users`.
- `V2__insert_seed_users.sql`: insere usuarios ficticios com senha em hash BCrypt.

Usuarios ficticios para desenvolvimento/testes:

| Email | Senha |
| --- | --- |
| `admin@bearflow.local` | `password` |
| `user@bearflow.local` | `password` |

## Rodando localmente

### Backend

```bash
./mvnw spring-boot:run
```

Com profile explicito:

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

Health check:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:

```json
{
  "status": "UP"
}
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Por padrao, o frontend chama `http://localhost:8080`. Para alterar:

```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080 npm run dev
```

A primeira tela e o login da Bear Flow com opcao de cadastro. O access token fica apenas em memoria no estado do app; o refresh token e controlado pelo backend via cookie `HttpOnly`.

## Docker

O compose sobe PostgreSQL, backend e frontend nessa ordem efetiva:

1. `postgres` fica saudavel via `pg_isready`.
2. `backend` inicia com profile `prod` e aguarda o banco.
3. `frontend` inicia apos o backend responder `/actuator/health`.

Subir a stack:

```bash
docker compose up --build
```

Portas padrao:

| Servico | Porta |
| --- | --- |
| Frontend | `3000` |
| Backend | `8080` |
| PostgreSQL | `5432` |

Variaveis uteis no compose:

| Variavel | Default local |
| --- | --- |
| `POSTGRES_DB` | `accessmanager` |
| `POSTGRES_USER` | `accessmanager` |
| `POSTGRES_PASSWORD` | `accessmanager-local-password` |
| `BACKEND_PORT` | `8080` |
| `FRONTEND_PORT` | `3000` |
| `NEXT_PUBLIC_API_BASE_URL` | `http://localhost:8080` |
| `JWT_SECRET` | placeholder local com mais de 32 caracteres |
| `OPENWEATHER_API_KEY` | `replace-me` |

Em ambiente real, sobrescreva todos os valores sensiveis por variaveis do servidor ou secret manager.

## Testes

Backend:

```bash
./mvnw -B test
```

Categorias cobertas no backend:

- Testes de contexto e actuator.
- Testes de migration/Flyway.
- Testes de repository com H2.
- Testes de integracao HTTP para CRUD, auth e weather.
- Teste E2E de backend para login + JWT + cookie + consulta de clima com OpenWeather mockada.

Frontend:

```bash
cd frontend
npm run lint
npm run build
```

Docker:

```bash
docker compose config
docker build -t bear-flow-accessmanager-backend:local .
docker build -t bear-flow-frontend:local ./frontend
```

## Fluxo Completo

1. Criar usuario em `POST /api/users` ou usar seed ficticio.
2. Fazer login em `POST /api/auth/login`.
3. Guardar o `accessToken` retornado.
4. Enviar `Authorization: Bearer <ACCESS_TOKEN>` em `GET /api/weather?city=Recife`.
5. O backend consulta a OpenWeather via OpenFeign e retorna um DTO interno.

## Autenticacao

### Login

```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@bearflow.local",
    "password": "password"
  }'
```

Resposta `200 OK`:

```json
{
  "authenticated": true,
  "accessToken": "eyJhbGciOi...",
  "user": {
    "id": 1,
    "name": "Bear Flow Admin",
    "email": "admin@bearflow.local",
    "active": true,
    "createdAt": "2026-01-01T00:00:00",
    "updatedAt": "2026-01-01T00:00:00"
  }
}
```

O refresh token nao aparece no corpo. Ele e enviado no header `Set-Cookie` como `refresh_token`, com `HttpOnly`, `Path=/api/auth`, `SameSite` configuravel e `Secure` configuravel por ambiente.

### Usuario autenticado

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

## Usuarios

### Criar usuario

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "password": "strong-password"
  }'
```

Resposta `201 Created`:

```json
{
  "id": 3,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "active": true,
  "createdAt": "2026-05-06T12:00:00",
  "updatedAt": "2026-05-06T12:00:00"
}
```

A senha nunca e retornada pela API e somente o hash BCrypt e persistido.

### Listar usuarios ativos

```bash
curl http://localhost:8080/api/users
```

### Buscar usuario por id

```bash
curl http://localhost:8080/api/users/1
```

### Atualizar usuario

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bear Flow Admin",
    "email": "admin@bearflow.local"
  }'
```

### Remover usuario

O delete e logico: o usuario fica com `active = false`.

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

Resposta `204 No Content`.

## Weather

Endpoint protegido por JWT. A consulta interna usa a Current Weather API da OpenWeather: `GET /data/2.5/weather`, com cidade, API key, `units=metric` e `lang=pt_br`.

Referencia oficial: https://openweathermap.org/current

```bash
curl "http://localhost:8080/api/weather?city=Recife" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

Resposta `200 OK`:

```json
{
  "city": "Recife",
  "country": "BR",
  "temperature": 29.0,
  "feelsLike": 31.0,
  "humidity": 78,
  "pressure": 1011,
  "windSpeed": 3.5,
  "condition": "Clear",
  "description": "ceu limpo",
  "icon": "01d"
}
```

Possiveis falhas:

- `400 Bad Request`: cidade ausente ou invalida.
- `401 Unauthorized`: JWT ausente ou invalido.
- `404 Not Found`: cidade nao encontrada.
- `502 Bad Gateway`: OpenWeather indisponivel ou chave invalida.

## Padrao de erros

Exemplo:

```json
{
  "status": 400,
  "error": "Validation failed",
  "message": "Password must have between 8 and 100 characters",
  "path": "/api/users"
}
```
