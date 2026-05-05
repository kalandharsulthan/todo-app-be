# Todo App — Backend API

A production-ready RESTful API for managing Todo items, built with **Spring Boot 3.5** and **Java 17**.

## Tech Stack

- **Java 17** + **Spring Boot 3.5**
- **Spring Data JPA** + **Hibernate 6**
- **PostgreSQL**
- **SpringDoc OpenAPI** (Swagger UI)
- **Docker** + **Railway** deployment
- **Lombok** + **Maven**

## Features

- Full CRUD operations
- Pagination, filtering (by status & priority), and sorting
- Input validation with meaningful error messages
- Consistent JSON response wrapper (`ApiResponse<T>`)
- Auto-generated UUID primary keys
- OpenAPI 3.0 spec — shareable with frontend developers

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally
- Maven (or use the included `./mvnw` wrapper)

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/kalandharsulthan/todo-app-be.git
   cd todo-app-be
   ```

2. **Create the database**
   ```bash
   psql -U postgres -c "CREATE DATABASE todo_db;"
   ```

3. **Configure environment** — copy `.env.example` and set your values, or export directly:
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/todo_db
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=yourpassword
   ```

4. **Run the app**
   ```bash
   ./mvnw spring-boot:run
   ```

The `todos` table is auto-created on first startup.

## API Documentation

Once running, open **Swagger UI** in your browser:

```
http://localhost:8080/swagger-ui.html
```

Raw OpenAPI JSON spec:
```
http://localhost:8080/api-docs
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/todos` | Get all todos (paginated) |
| `GET` | `/api/v1/todos/{id}` | Get todo by ID |
| `POST` | `/api/v1/todos` | Create a todo |
| `PUT` | `/api/v1/todos/{id}` | Full update |
| `PATCH` | `/api/v1/todos/{id}/status` | Update status only |
| `DELETE` | `/api/v1/todos/{id}` | Delete a todo |
| `DELETE` | `/api/v1/todos/completed` | Delete all completed todos |
| `GET` | `/api/v1/health` | Health check |

### Query Parameters for GET /api/v1/todos

| Param | Type | Description |
|-------|------|-------------|
| `page` | int | Page number (default: 0) |
| `size` | int | Page size (default: 10) |
| `status` | enum | Filter: `PENDING`, `IN_PROGRESS`, `COMPLETED` |
| `priority` | enum | Filter: `LOW`, `MEDIUM`, `HIGH` |
| `sort` | string | e.g. `createdAt,desc` or `title,asc` |

### Example Request

```bash
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "priority": "HIGH",
    "dueDate": "2026-12-01T10:00:00"
  }'
```

### Example Response

```json
{
  "success": true,
  "message": "Todo created successfully",
  "data": {
    "id": "84b90b88-7dce-4c5f-a47c-4ead845ff4f0",
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "status": "PENDING",
    "priority": "HIGH",
    "dueDate": "2026-12-01T10:00:00",
    "createdAt": "2026-05-05T23:32:55.599078",
    "updatedAt": "2026-05-05T23:32:55.599078"
  }
}
```

## Running Tests

```bash
./mvnw test
```

## Docker

```bash
# Build
docker build -t todo-app-be .

# Run
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/todo_db" \
  -e DATABASE_USERNAME="postgres" \
  -e DATABASE_PASSWORD="yourpassword" \
  todo-app-be
```

## Deploying to Railway

1. Push this repo to GitHub
2. Go to [railway.app](https://railway.app) → New Project → Deploy from GitHub
3. Add environment variables from `.env.example` in Railway dashboard
4. Railway auto-detects `Dockerfile` and deploys
5. Visit `<your-railway-url>/swagger-ui.html`

## Environment Variables

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | JDBC PostgreSQL connection URL |
| `DATABASE_USERNAME` | Database username |
| `DATABASE_PASSWORD` | Database password |
| `PORT` | Server port (Railway sets this automatically) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins (default: `*`) |
| `SPRING_PROFILES_ACTIVE` | Set to `prod` in production |
