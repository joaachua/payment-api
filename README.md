# Payment API

A Spring Boot REST API demonstrating payment lifecycle management, request idempotency, validation, persistence, and structured error handling.

> This is a portfolio and learning project. It does not process real card details or connect to a production payment provider.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- MySQL 8
- Maven
- OpenAPI and Swagger UI
- Docker and Docker Compose

## Implemented Features

- Create a payment
- Retrieve payment details
- Confirm a pending payment
- Mark a pending payment as failed
- Refund a successful payment
- Validate payment status transitions
- Validate incoming payment requests
- Prevent duplicate payment creation using an idempotency key
- Reject reuse of an idempotency key with a different request
- Return structured API errors
- Persist payment data using MySQL
- Run the application and database using Docker Compose
- Explore the API using Swagger UI

## Payment Lifecycle

```text
PENDING ──→ SUCCESS ──→ REFUNDED
    │
    └─────→ FAILED
```

Allowed transitions:

- `PENDING` → `SUCCESS`
- `PENDING` → `FAILED`
- `SUCCESS` → `REFUNDED`

Other transitions return `409 Conflict`.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/payments` | Create an idempotent payment |
| `GET` | `/api/payments/{id}` | Retrieve a payment |
| `POST` | `/api/payments/{id}/confirm` | Confirm a pending payment |
| `POST` | `/api/payments/{id}/fail` | Mark a pending payment as failed |
| `POST` | `/api/payments/{id}/refund` | Refund a successful payment |

## Running with Docker

### Requirements

- Docker
- Docker Compose

Build and start the application and MySQL:

```bash
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Stop the containers:

```bash
docker compose down
```

The MySQL data is stored in a Docker volume and remains available after the containers stop.

To also delete the local database volume:

```bash
docker compose down -v
```

> Warning: The `-v` option permanently deletes the local Docker database data.

## Creating a Payment

Creating a payment requires an `Idempotency-Key` header.

```bash
curl --request POST \
  --url http://localhost:8080/api/payments \
  --header "Content-Type: application/json" \
  --header "Idempotency-Key: order-1001" \
  --data '{
    "amount": 100.00,
    "currency": "MYR"
  }'
```

Example response:

```json
{
  "id": 1,
  "reference": "PAY-example",
  "amount": 100.00,
  "currency": "MYR",
  "status": "PENDING",
  "createdAt": "2026-08-24T10:00:00",
  "updatedAt": "2026-08-24T10:00:00"
}
```

Sending the same request again with the same idempotency key returns the existing payment instead of creating a duplicate.

Using the same key with a different amount or currency returns `409 Conflict`.

## Retrieving a Payment

```bash
curl http://localhost:8080/api/payments/1
```

## Confirming a Payment

```bash
curl --request POST \
  http://localhost:8080/api/payments/1/confirm
```

## Failing a Payment

```bash
curl --request POST \
  http://localhost:8080/api/payments/1/fail
```

## Refunding a Payment

Only a payment with `SUCCESS` status can be refunded.

```bash
curl --request POST \
  http://localhost:8080/api/payments/1/refund
```

## Running Without Docker

### Requirements

- Java 21
- MySQL 8
- Maven or the included Maven wrapper

Start only MySQL using Docker:

```bash
docker compose up -d mysql
```

Then start Spring Boot directly:

```bash
./mvnw spring-boot:run
```

Application configuration is stored in:

```text
src/main/resources/application.yml
```

Database settings can be overridden using environment variables:

| Variable | Purpose |
|---|---|
| `DB_URL` | JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JPA_DDL_AUTO` | Hibernate schema behavior |
| `SERVER_PORT` | Application port |

Do not commit production passwords or secrets to the repository.

## Running Tests

With the required test database available:

```bash
./mvnw test
```

Current tests cover payment creation, retrieval, validation, not-found responses, and basic idempotency.

Expanded lifecycle, concurrency, and container-based integration tests are planned.

## Project Roadmap

### Backend quality

- Complete payment lifecycle test coverage
- Add Testcontainers integration tests
- Add optimistic locking for concurrent updates
- Add Flyway database migrations
- Add pagination and payment filtering
- Add an append-only payment event history

### External integrations

- Simulate an external payment provider
- Process signed webhook events
- Add retry and timeout handling
- Add transactional outbox processing

### Full-stack application

- Build a React and TypeScript frontend
- Add payment list and detail screens
- Add payment lifecycle actions
- Display payment event history

### Enterprise readiness

- Add Spring Security and role-based authorization
- Add Spring Boot Actuator
- Add metrics, structured logs, and distributed tracing
- Add a GitHub Actions CI pipeline
- Add automated dependency and container scanning
- Prepare an AWS deployment using infrastructure as code

## Status

Active portfolio project under development.