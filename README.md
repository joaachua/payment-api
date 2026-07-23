# Payment API

A Spring Boot REST API for handling payment creation, confirmation, failure, refunds and webhook processing.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## Implemented Features

- Create a payment
- Retrieve payment details
- Confirm a pending payment
- Mark a pending payment as failed
- Refund a successful payment
- Validate payment status transitions
- Validate incoming payment requests
- Return structured API errors
- Persist payment data using MySQL

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments` | Create a payment |
| GET | `/api/payments/{id}` | Retrieve a payment |
| POST | `/api/payments/{id}/confirm` | Confirm a pending payment |
| POST | `/api/payments/{id}/fail` | Mark a pending payment as failed |
| POST | `/api/payments/{id}/refund` | Refund a successful payment |

## Create Payment

### Request

```json
{
  "amount": 100.00,
  "currency": "MYR"
}
```

## Roadmap

- Idempotency key support
- Payment gateway webhook simulation
- Dockerized application setup
- Expanded automated test coverage
- CI pipeline

## Payment Flow

```text
PENDING → SUCCESS → REFUNDED
    ↓
  FAILED
```

## Running Locally

### Requirements

- Java 21
- MySQL 8
- Maven, or the included Maven wrapper

### Database

Create a MySQL database:

```sql
CREATE DATABASE payment_api;
```

```bash
./mvnw spring-boot:run
```

## Status

Work in progress.
