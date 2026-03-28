# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Test
./mvnw test

# Run a single test class
./mvnw test -Dtest=MyApiApplicationTests

# Clean
./mvnw clean
```

## Architecture

This is a Spring Boot REST API (Java 25, Maven) following Clean Architecture with four layers:

- **`interfaces/rest/`** — HTTP layer. `AuthController` exposes `POST /api/auth/login`.
- **`application/auth/`** — Use case layer. `LoginService` orchestrates the auth flow via `PasswordHasher` and `TokenService` interfaces.
- **`domain/auth/`** — Core domain: `User` aggregate, value objects (`UserId`, `Email`, `PasswordHash`), and `UserRepository` interface.
- **`infrastructure/auth/`** — Implementations: `InMemoryUserRepository` (no database), `BCryptPasswordHasher`, `UuidTokenService`.
- **`config/`** — Spring beans (`AuthConfig`) and CORS setup (`CorsConfig`).

**Request flow:** `AuthController` → `LoginService` → `UserRepository` + `PasswordHasher` + `TokenService` → response with `userId`, `email`, `token`.

## Key Details

- **No database**: `InMemoryUserRepository` seeds one test user (`user@example.com` / `Password123!`) on startup.
- **Token service**: `UuidTokenService` returns a random UUID — not JWT.
- **CORS**: Configured for `localhost:3000` and `localhost:5173` via `application.properties`.
