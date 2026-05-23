# AGENTS.md

## Project

DealGuard is a Spring Boot backend project for AI-powered secondhand transaction dispute prevention.

## Main Goal

Build a backend that can:

- manage users with JWT authentication
- store product posts
- store transaction conversations and chat messages
- extract transaction conditions from chat
- detect missing conditions, ambiguous expressions, condition changes, and listing-chat mismatches
- generate PDF evidence packages

## Tech Stack

- Java
- Spring Boot
- Gradle
- PostgreSQL
- Spring Security
- Spring Data JPA
- JWT
- Swagger/OpenAPI
- Flyway
- Docker Compose

## Package Rules

Use the package root:

```text
com.dealguard
```

Recommended packages:

- global
- auth
- user
- product
- conversation
- message
- analysis
- evidence
- ai

## Coding Rules

- Do not implement frontend in this repository.
- Do not hardcode secrets.
- Do not expose entity classes directly from controllers.
- Use request and response DTOs.
- Use constructor injection.
- Keep controllers thin.
- Put business logic in service classes.
- Use repositories only from service classes.
- Use GlobalExceptionHandler for error responses.
- Use Bean Validation for request DTOs.
- Write unit tests for analysis logic.
- Keep analysis logic understandable for a university project presentation.

## AI Rules

- The backend must work with `ai.provider=mock`.
- External LLM API integration must be optional.
- Gemini integration should be implemented behind `AiAnalysisClient`.
- Do not send real private user data to external APIs during local testing.
- Use dummy data for tests.

## Analysis Rules

The analysis module should detect:

- price extraction
- listing price vs chat price mismatch
- condition changes
- missing refund policy
- missing product condition
- ambiguous expressions
- risky payment patterns
- direct/delivery trade method

## Documentation Rules

Keep docs under `/docs`.
Update `README.md` when adding new features.
API design should be documented in `docs/05-api-spec-draft.md`.
