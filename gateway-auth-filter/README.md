# Gateway Authentication Filter

This project implements an authentication filter for a gateway service, focusing on validating JWT tokens and managing user authentication.

## Project Structure

- **src/**: Contains the source code for the application.
  - **index.ts**: Entry point of the application, initializes the gateway and sets up middleware and routes.
  - **gateway/**: Contains the core logic for authentication and request handling.
    - **authFilter.ts**: Implements the authentication logic with methods to validate tokens and handle requests.
    - **proxy.ts**: Manages the proxying of requests to appropriate services.
  - **middleware/**: Contains middleware functions for request processing.
    - **jwtMiddleware.ts**: Checks for JWT tokens in request headers and validates them.
  - **services/**: Contains services related to user authentication.
    - **authService.ts**: Handles user authentication processes such as login and logout.
  - **config/**: Configuration settings for the application.
    - **default.ts**: Exports configuration variables like JWT secret keys.
  - **utils/**: Utility functions and classes.
    - **logger.ts**: Provides logging functionality for the application.
  - **types/**: Type definitions used throughout the application.
    - **index.ts**: Exports interfaces for request and response objects.

- **test/**: Contains unit tests for the application.
  - **authFilter.spec.ts**: Tests for the AuthFilter class to ensure authentication logic works correctly.

## Setup Instructions

1. Clone the repository:
   ```
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```
   cd gateway-auth-filter
   ```

3. Install dependencies:
   ```
   npm install
   ```

4. Run the application:
   ```
   npm start
   ```

5. Run tests:
   ```
   npm test
   ```

## Usage Guidelines

- The application expects JWT tokens to be included in the Authorization header of incoming requests.
- Ensure that the configuration settings in `src/config/default.ts` are set according to your environment.
- Use the Logger utility in `src/utils/logger.ts` for logging throughout the application.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.