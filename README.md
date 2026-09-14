# Dealership Management System

A full-stack dealership management application built with Java, Spring Boot, PostgreSQL, and React.

The application provides secure management of cars, customers, and vehicle sales through a REST API and React frontend.

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- PostgreSQL
- Maven

### Frontend
- React
- Vite
- React Router
- Bootstrap
- JavaScript

### Testing & Documentation
- JUnit
- Mockito
- MockMvc
- Integration Testing
- Swagger / OpenAPI

## Features

- Manage cars with create, view, update, delete, search, sorting, and pagination
- Manage customers with validation and duplicate email protection
- Record vehicle sales and prevent the same car from being sold twice
- JWT-based authentication
- Role-based authorization for ADMIN and SALESPERSON users
- Secure protected API endpoints with Spring Security
- Global exception handling with meaningful HTTP status codes
- DTO-based request and response handling
- Input validation using Jakarta Validation
- PostgreSQL database persistence with Spring Data JPA
- Swagger / OpenAPI documentation for REST endpoints
- Unit, controller, and integration testing
- React frontend with login, dashboard, cars, customers, and sales pages

## Security & Roles

The application uses Spring Security with JWT-based authentication.

### ADMIN
- Create, update, and delete cars
- View and manage customers
- Delete customers
- View and record sales
- Access protected administrative functionality

### SALESPERSON
- View customers
- Create and update customers
- View sales
- Record sales
- Cannot create, update, or delete cars
- Cannot delete customers

### Public Access
- View car information
- Register a new user
- Login to receive a JWT token
- Access Swagger / OpenAPI documentation

Protected requests require a valid JWT token in the `Authorization` header.

Example:

```text
Authorization: Bearer <token>
```

## API Overview

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Authenticate a user and return a JWT token |
| POST | `/users/register` | Register a new user |

### Cars

| Method | Endpoint | Description |
|---|---|---|
| GET | `/cars` | View cars |
| GET | `/cars/{id}` | View a car by ID |
| POST | `/cars` | Add a new car |
| PUT | `/cars/{id}` | Update a car |
| DELETE | `/cars/{id}` | Delete a car |

### Customers

| Method | Endpoint | Description |
|---|---|---|
| GET | `/customers` | View customers |
| GET | `/customers/{id}` | View a customer by ID |
| POST | `/customers` | Add a customer |
| PUT | `/customers/{id}` | Update a customer |
| DELETE | `/customers/{id}` | Delete a customer |

### Sales

| Method | Endpoint | Description |
|---|---|---|
| GET | `/sales` | View sales |
| POST | `/sales` | Record a vehicle sale |

## How to Run the Project

### Prerequisites

Make sure the following are installed:

- Java 21
- PostgreSQL
- Node.js
- npm

The project includes the Maven Wrapper, so a separate Maven installation is not required.

### Database Setup

Create the following PostgreSQL databases:

```text
dealershipdb
dealership_test
```

### Environment Variables

For local development in PowerShell:

```powershell
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="your_long_jwt_secret"
$env:ADMIN_PASSWORD="your_admin_password"
$env:SALES_PASSWORD="your_sales_password"
$env:SPRING_PROFILES_ACTIVE="dev"
```

For running tests:

```powershell
$env:TEST_DB_PASSWORD="your_test_database_password"
```

Do not commit real passwords or JWT secrets to the repository.

### Run the Backend

From the project root:

```powershell
.\mvnw spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

### Run the Frontend

Open another terminal:

```powershell
cd dealership-frontend
npm install
npm run dev
```

The React frontend runs at:

```text
http://localhost:5173
```

### Swagger API Documentation

After starting the backend, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

### Run Tests

Make sure the test database exists and `TEST_DB_PASSWORD` is configured, then run:

```powershell
.\mvnw test
```

Current test suite:

```text
Tests run: 54
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Project Structure

```text
dealership/
├── src/
│   ├── main/
│   │   ├── java/com/autovibe/dealership/
│   │   │   ├── Controllers
│   │   │   ├── Services
│   │   │   ├── Repositories
│   │   │   ├── Entities
│   │   │   ├── DTOs
│   │   │   ├── Security
│   │   │   └── Exception Handling
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/
│       │   └── Unit, controller, and integration tests
│       └── resources/
│           └── application-test.properties
│
├── dealership-frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
│   └── package.json
│
├── pom.xml
└── README.md
```

The backend classes are primarily contained within the `com.autovibe.dealership` package, with DTOs organized in a dedicated `dto` package.

## Application Architecture

The backend follows a layered architecture:

```text
React Frontend
      ↓
REST Controller
      ↓
Service Layer
      ↓
Repository Layer
      ↓
PostgreSQL Database
```

Protected requests are processed by Spring Security and the JWT authentication filter before reaching protected controller endpoints.

## Testing

The backend includes unit, controller, and integration tests.

### Unit Tests

Service-layer tests use JUnit and Mockito to isolate business logic from the database.

### Controller Tests

MockMvc is used to test REST endpoints, HTTP status codes, validation, authorization, and JSON responses.

### Integration Tests

Integration tests verify multiple application layers working together, including:

- REST API behavior
- PostgreSQL persistence
- Spring Security
- JWT authentication
- Role-based authorization
- Invalid and tampered JWT handling

Current test results:

```text
Tests run: 54
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Key Design Decisions

- **DTOs:** Request and response DTOs separate the API contract from database entities.
- **BigDecimal:** Monetary values such as vehicle prices and sale prices use `BigDecimal` instead of floating-point types.
- **JWT Authentication:** The backend uses stateless JWT authentication for protected API requests.
- **Role-Based Authorization:** ADMIN and SALESPERSON permissions are enforced by Spring Security.
- **Password Security:** User passwords are stored using BCrypt hashing.
- **Environment Variables:** Database passwords, JWT secrets, and seeded-user passwords are kept outside source code.
- **Database Integrity:** A vehicle cannot be sold more than once, and referenced cars/customers cannot be deleted.
- **Validation:** Incoming DTOs are validated before business logic is executed.
- **Global Exception Handling:** Application exceptions are translated into meaningful HTTP responses such as 400, 404, and 409.
- **Transactions:** Sale creation uses transactional behavior to maintain data consistency.