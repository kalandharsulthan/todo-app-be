# Todo App Backend

Spring Boot 3.5 REST API for managing Todo items. Java 17, PostgreSQL (local), deployed to Railway.

## Build & Run

```bash
# Run locally (requires PostgreSQL running)
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t todo-app-be .
```

## Key URLs (local)

- API Base: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Health: `http://localhost:8080/api/v1/health`

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/prod_support_ai` | JDBC connection URL |
| `DATABASE_USERNAME` | `ai_user` | DB username |
| `DATABASE_PASSWORD` | `ai_password` | DB password |
| `PORT` | `8080` | Server port |
| `CORS_ALLOWED_ORIGINS` | `*` | Allowed CORS origins |
| `SPRING_PROFILES_ACTIVE` | — | Set to `prod` on Railway |

## Architecture

```
com.todo.app/
├── config/          # CORS (WebConfig), OpenAPI (OpenApiConfig)
├── controller/      # TodoController, HealthController
├── dto/
│   ├── request/     # TodoRequest, TodoStatusUpdateRequest
│   └── response/    # ApiResponse<T>, PagedResponse<T>, TodoResponse
├── entity/          # Todo (JPA entity)
├── enums/           # TodoStatus, TodoPriority
├── exception/       # GlobalExceptionHandler, ResourceNotFoundException, ErrorResponse
├── repository/      # TodoRepository (JpaRepository + custom JPQL)
└── service/         # TodoService (interface), TodoServiceImpl
```

## API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/todos` | List todos — params: `page`, `size`, `status`, `priority`, `sort` |
| GET | `/api/v1/todos/{id}` | Get by UUID |
| POST | `/api/v1/todos` | Create todo |
| PUT | `/api/v1/todos/{id}` | Full update |
| PATCH | `/api/v1/todos/{id}/status` | Update status only |
| DELETE | `/api/v1/todos/{id}` | Delete one |
| DELETE | `/api/v1/todos/completed` | Delete all completed |
| GET | `/api/v1/health` | Health check |

## Todo Fields

- `title` — required, max 100 chars
- `description` — optional, max 500 chars
- `status` — `PENDING` | `IN_PROGRESS` | `COMPLETED` (default: `PENDING`)
- `priority` — `LOW` | `MEDIUM` | `HIGH` (default: `MEDIUM`)
- `dueDate` — optional, must be a future date

## Deployment

- **Platform:** Railway (Docker-based)
- **Config:** `railway.toml`
- **Health check:** `/api/v1/health`
- Set env vars in Railway dashboard from `.env.example`

## Database

- Local: PostgreSQL — `ddl-auto=update` (Hibernate auto-creates tables)
- Production: set `ddl-auto=validate` via `application-prod.properties`
- Table: `todos` with indexes on `status`, `priority`, `created_at`
