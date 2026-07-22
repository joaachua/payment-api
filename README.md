# Payment API

A Spring Boot REST API for handling payment creation, confirmation, failure, refunds and webhook processing.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## Planned Features

- Create payments
- Retrieve payment details
- Confirm or fail payments
- Process refunds
- Validate payment status transitions
- Handle duplicate requests using idempotency keys
- Process payment gateway webhooks
- Add automated tests

## Payment Flow

```text
PENDING → SUCCESS → REFUNDED
    ↓
  FAILED
```

## Running Locally

```bash
./mvnw spring-boot:run
```

## Status

Work in progress.
