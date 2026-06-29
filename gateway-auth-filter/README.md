# Gateway Authentication Filter (Java)

This project is now implemented in Java using Spring Cloud Gateway with JWT authentication.

## Project Structure

- **pom.xml**: Maven configuration for the Spring Boot gateway.
- **src/main/java**: Java application source code.
  - **com.bcopstein.gatewayauth.GatewayAuthFilterApplication**: Spring Boot application bootstrap.
  - **com.bcopstein.gatewayauth.security.JwtAuthFilter**: Global Gateway filter that validates JWT tokens.
  - **com.bcopstein.gatewayauth.service.JwtService**: JWT validation service.
- **src/main/resources/application.yml**: Gateway routes, Eureka client settings, and JWT secret configuration.
- **src/test/java**: Unit tests.
  - **com.bcopstein.gatewayauth.service.JwtServiceTest**: Tests for JWT validation.

## Setup Instructions

1. Navigate to the project directory:
   ```bash
   cd gateway-auth-filter
   ```

2. Build the application:
   ```bash
   ./mvnw clean package
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Usage Guidelines

- The gateway listens on `http://localhost:3000` by default.
- Requests must include a valid JWT in the `Authorization` header using the `Bearer` scheme.
- The filter allows public access to `/auth`, `/login`, and `/actuator` endpoints.
- Routes are configured to forward requests to the registered Eureka services:
  - `/estoque/**` -> `estoque-service`
  - `/entrega/**` -> `entrega-service`
  - `/delivery/**` -> `delivery-service`
  - `/gateway/**` -> `gateway-service`
  - `/auth/**` -> `auth-service`

## Configuration

- `jwt.secret` in `application.yml` is used to validate JWT signatures.
- The Eureka client is configured to use `http://localhost:8761/eureka/`.

## Notes

- The Java gateway replaces the previous TypeScript-based auth gateway.
- To extend route mappings or token policies, update `application.yml` and `JwtAuthFilter`.
