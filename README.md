# SemesterProjekt

A Java-based REST API server for managing media content with user authentication, ratings, and personalized recommendations.

## Features

- **User Management**: Registration, login, and profile management with JWT authentication
- **Media Management**: Create, read, update, and delete media entries
- **Rating System**: Rate and like media content
- **Recommendations**: Personalized media recommendations based on user preferences
- **Favorites**: Mark and manage favorite media items

## Tech Stack

- **Language**: Java 21
- **Framework**: Java HttpServer
- **Database**: PostgreSQL 16
- **Build Tool**: Maven
- **Dependencies**:
  - Jackson (JSON processing)
  - PostgreSQL JDBC Driver
  - password4j (Password hashing)
  - Lombok (Boilerplate reduction)
  - JUnit (Testing)

## Prerequisites

- Docker and Docker Compose

## Quick Start

1. Clone the repository
2. Start the application:
   ```bash
   docker compose up
   ```

The application will be available at `http://localhost:8080`

The Docker setup includes:
- Automatic compilation and dependency management
- PostgreSQL database with health checks
- Database schema initialization

### Stopping the Application

```bash
docker compose down
```

### Run Tests

```bash
mvn test
```

## API Documentation

Full API documentation is available in the `specification/` directory:

- **OpenAPI Specification**: `specification/openapi-mrp.yaml` - Complete API spec (view with Swagger Editor)
- **Postman Collection**: `specification/mrp_collection.json` - Import into Postman for testing
- **Full Specification**: `specification/MRP_Specification.pdf` - Complete project specification


## Project Structure

```
src/main/java/
├── org/example/
│   ── Main.java              # Application entry point
├── handlers/                  # HTTP request handlers
├── service/                   # Business logic layer
├── dataaccess/               # Database access layer (DAOs)
├── models/                   # Data models
├── datatransfer/             # DTOs for requests/responses
├── helpers/                  # Utility classes
└── config/                   # Configuration classes
```

## Database

The application uses PostgreSQL with automatic schema initialization via `database/schema.sql`.

Database credentials:
- Database: `mydb`
- User: `myuser`
- Password: `mypass`
- Port: `5432`

