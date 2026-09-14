# Dealership Management System

A full-stack dealership management application built with **Java, Spring Boot, PostgreSQL, and React**.

The system provides secure management of vehicles, customers, and vehicle sales through a REST API and React frontend. It demonstrates a production-style layered architecture with authentication, role-based authorization, validation, database persistence, automated testing, API documentation, and Docker deployment.

---

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

### Deployment
- Docker
- Docker Compose
- Nginx

---

## Features

- Create, view, update, delete, search, sort, and paginate vehicles
- Manage customers with input validation and duplicate email protection
- Record vehicle sales
- Prevent the same vehicle from being sold more than once
- JWT-based authentication
- Role-based authorization with `ADMIN` and `SALESPERSON` roles
- Protected REST API endpoints using Spring Security
- BCrypt password hashing
- DTO-based request and response handling
- Jakarta Bean Validation
- Global exception handling with meaningful HTTP status codes
- PostgreSQL persistence using Spring Data JPA
- Transactional sale processing
- Swagger / OpenAPI REST API documentation
- Unit, controller, security, and integration testing
- React frontend with login, dashboard, cars, customers, and sales pages
- Docker Compose support for running the complete application

---

## Security & Roles

The application uses **Spring Security** with stateless **JWT authentication**.

After successful login, the backend returns a JWT token. Protected API requests must include the token in the `Authorization` header:

```text
Authorization: Bearer <token>
```

### ADMIN

An administrator can:

- Create, update, and delete cars
- View and manage customers
- Delete customers
- View vehicle sales
- Record vehicle sales
- Access protected administrative functionality

### SALESPERSON

A salesperson can:

- View customers
- Create customers
- Update customers
- View sales
- Record vehicle sales

A salesperson cannot:

- Create cars
- Update cars
- Delete cars
- Delete customers

### Public Access

Unauthenticated users can:

- View car information
- Register a user
- Login and receive a JWT token
- Access Swagger / OpenAPI documentation

---

## API Overview

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/login` | Authenticate a user and return a JWT token |
| `POST` | `/users/register` | Register a new user |

### Cars

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/cars` | View cars |
| `GET` | `/cars/{id}` | View a car by ID |
| `POST` | `/cars` | Add a new car |
| `PUT` | `/cars/{id}` | Update a car |
| `DELETE` | `/cars/{id}` | Delete a car |

### Customers

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/customers` | View customers |
| `GET` | `/customers/{id}` | View a customer by ID |
| `POST` | `/customers` | Add a customer |
| `PUT` | `/customers/{id}` | Update a customer |
| `DELETE` | `/customers/{id}` | Delete a customer |

### Sales

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/sales` | View vehicle sales |
| `POST` | `/sales` | Record a vehicle sale |

---

# Running the Project Locally

## Prerequisites

Install the following:

- Java 21
- PostgreSQL
- Node.js
- npm

The project includes the **Maven Wrapper**, so a separate Maven installation is not required.

---

## Database Setup

Create the PostgreSQL databases:

```text
dealershipdb
dealership_test
```

The first database is used for local development and the second is used by the integration test environment.

---

## Environment Variables

Sensitive configuration is provided through environment variables rather than being stored directly in source code.

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

> Do not commit real database passwords, JWT secrets, or application passwords to the repository.

---

## Run the Backend

From the project root:

```powershell
.\mvnw spring-boot:run
```

The backend starts at:

```text
http://localhost:8080
```

---

## Run the Frontend

Open another terminal and run:

```powershell
cd dealership-frontend
npm install
npm run dev
```

The React development server starts at:

```text
http://localhost:5173
```

---

## Swagger API Documentation

After starting the backend, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger can be used to inspect and test the REST API endpoints.

---

# Testing

The backend contains unit, controller, security, and integration tests.

To run the complete test suite:

```powershell
.\mvnw test
```

Make sure the `dealership_test` PostgreSQL database exists and `TEST_DB_PASSWORD` is configured before running the integration tests.

### Current Test Results

```text
Tests run: 54
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Unit Tests

Service-layer tests use **JUnit** and **Mockito** to test business logic independently from the database.

These tests verify scenarios such as:

- Successful business operations
- Resource-not-found conditions
- Duplicate data handling
- Invalid business operations
- Sale validation

## Controller Tests

**MockMvc** is used to test REST controllers without requiring a running frontend.

Controller tests verify:

- REST endpoints
- HTTP status codes
- Request validation
- JSON responses
- Authentication
- Role-based authorization

## Integration Tests

Integration tests verify multiple application layers working together.

They cover areas including:

- REST API behavior
- PostgreSQL persistence
- Spring Security
- JWT authentication
- Role-based authorization
- Invalid JWT handling
- Tampered JWT handling
- Business rules involving customers, vehicles, and sales

---

# Running with Docker

The complete application can also be run using **Docker Compose**.

## Docker Prerequisites

Install:

- Docker Desktop

## Environment Variables

Set the required environment variables:

```powershell
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="your_long_jwt_secret"
$env:ADMIN_PASSWORD="your_admin_password"
$env:SALES_PASSWORD="your_sales_password"
```

> Do not commit real passwords or JWT secrets to the repository.

---

## Build and Start the Application

From the project root:

```powershell
docker compose up -d --build
```

Docker Compose starts the complete application architecture:

```text
Browser
   |
   v
React Frontend
   |
   v
Nginx
   |
   v
Spring Boot REST API
   |
   v
PostgreSQL
```

The services are available at:

| Service | Address |
|---|---|
| Frontend | `http://localhost` |
| Backend API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| PostgreSQL | Host port `5433` |

The PostgreSQL container uses a **Docker named volume**, allowing database data to persist when containers are recreated.

---

## Stop the Application

```powershell
docker compose down
```

This stops and removes the application containers while preserving PostgreSQL data.

To remove the containers **and delete the PostgreSQL data volume**:

```powershell
docker compose down -v
```

---

# Project Structure

```text
dealership/
├── src/
│   ├── main/
│   │   ├── java/com/autovibe/dealership/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── security/
│   │   │   └── exception/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/
│       │   └── unit, controller, security,
│       │       and integration tests
│       │
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
├── docker-compose.yml
├── pom.xml
└── README.md
```

The backend application is primarily contained within the `com.autovibe.dealership` package, with responsibilities separated into controllers, services, repositories, entities, DTOs, security components, and exception handling.

---

# Application Architecture

The backend follows a layered architecture:

```text
React Frontend
       |
       v
REST Controller
       |
       v
Service Layer
       |
       v
Repository Layer
       |
       v
PostgreSQL Database
```

Each layer has a specific responsibility:

**Controller Layer**  
Receives HTTP requests, validates request data, delegates operations to services, and returns HTTP responses.

**Service Layer**  
Contains application business logic and coordinates operations between controllers and repositories.

**Repository Layer**  
Uses Spring Data JPA to communicate with PostgreSQL.

**DTO Layer**  
Defines the API request and response models without exposing persistence entities directly.

**Security Layer**  
Handles authentication, JWT validation, password security, and role-based authorization.

**Exception Handling Layer**  
Converts application exceptions into consistent and meaningful HTTP responses.

For protected endpoints, requests first pass through Spring Security and the JWT authentication filter before reaching the controller.

```text
HTTP Request
     |
     v
JWT Authentication Filter
     |
     v
Spring Security
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
PostgreSQL
```

---

# Key Design Decisions

### DTOs

Request and response DTOs separate the public API contract from persistence entities.

This prevents database entities from being exposed directly and provides better control over validation and returned data.

### BigDecimal for Monetary Values

Vehicle prices and sale prices use `BigDecimal` rather than `double` or `float`.

`BigDecimal` avoids floating-point precision problems and is better suited for financial values.

### JWT Authentication

The backend uses stateless JWT authentication.

After login, the client sends the JWT with subsequent protected requests rather than maintaining a server-side HTTP session.

### Role-Based Authorization

Application permissions are controlled using the `ADMIN` and `SALESPERSON` roles.

Spring Security prevents unauthorized users from accessing protected operations.

### Password Security

Passwords are hashed using **BCrypt** before being stored in the database.

Plain-text passwords are never stored.

### Environment Variables

Sensitive configuration such as:

- Database passwords
- JWT secrets
- Seeded administrator passwords
- Seeded salesperson passwords

is provided through environment variables rather than being committed to source control.

### Database Integrity

Business rules protect the consistency of application data.

Examples include:

- A vehicle cannot be sold more than once.
- Customers referenced by existing sales cannot be deleted.
- Cars referenced by existing sales cannot be deleted.
- Duplicate customer email addresses are rejected.

### Validation

Incoming request DTOs use Jakarta Validation annotations to reject invalid data before business operations are performed.

### Global Exception Handling

Centralized exception handling converts application errors into meaningful HTTP responses.

Typical responses include:

- `400 Bad Request` — invalid request or validation failure
- `401 Unauthorized` — missing or invalid authentication
- `403 Forbidden` — authenticated user does not have permission
- `404 Not Found` — requested resource does not exist
- `409 Conflict` — request conflicts with existing application data

### Transactions

Vehicle sale creation uses transactional behavior to maintain database consistency.

If part of the sale operation fails, the transaction can be rolled back rather than leaving the database in a partially updated state.

---

# What This Project Demonstrates

This project demonstrates practical full-stack development concepts including:

- Object-oriented Java development
- REST API design
- Layered application architecture
- Dependency injection
- Spring Boot application development
- Spring Data JPA and relational persistence
- PostgreSQL database design
- DTO mapping
- Input validation
- Business-rule enforcement
- Exception handling
- Spring Security
- JWT authentication
- Role-based authorization
- Automated testing with JUnit, Mockito, and MockMvc
- Integration testing with PostgreSQL
- React frontend development
- REST API integration
- Docker containerization
- Docker Compose orchestration
- Secure configuration using environment variables

---

## Future Improvements

Possible future enhancements include:

- Refresh tokens
- Password reset functionality
- User account management
- Sales reporting and analytics
- Vehicle image uploads
- Advanced search and filtering
- Frontend automated testing
- CI/CD pipeline
- Cloud deployment

---

## Author

**Khushwinder Sehmi**

Java Full-Stack Developer