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
- Java 21 (if running locally)
- Maven 3.9+ (if running locally)

## Quick Start

### Using Docker (Recommended)

1. Clone the repository
2. Start the application:
   ```bash
   docker-compose up
   ```

The application will be available at `http://localhost:8080`

The Docker setup includes:
- Automatic recompilation on source code changes
- PostgreSQL database with health checks
- Database schema initialization
- Maven dependency caching

### Stopping the Application

To stop the application:
```bash
docker-compose down
```

To stop and remove the database volume:
```bash
docker-compose down --volumes
```

### Rebuilding After Changes

If you modify the pom.xml or Dockerfile:
```bash
docker-compose up --build
```

For code changes, the application automatically recompiles when files are modified.

### Local Development

1. Set up environment variables:
   ```bash
   export DB_URL="jdbc:postgresql://localhost:5432/mydb"
   export DB_USER="myuser"
   export DB_PASS="mypass"
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/SemesterProjekt-1.0-SNAPSHOT.jar
   ```

   Or use Maven:
   ```bash
   mvn exec:java
   ```

## API Documentation

Full API documentation is available in the `specification/` directory:

- **OpenAPI Specification**: `specification/openapi-mrp.yaml` - Complete API spec (view with Swagger Editor)
- **Postman Collection**: `specification/MRP_Postman_Collection.json` - Import into Postman for testing
- **Full Specification**: `specification/MRP_Specification.pdf` - Complete project specification

### Quick Reference

#### Authentication
- `POST /api/users/register` - Register a new user
- `POST /api/users/login` - Login and receive JWT token

#### User Management
- `GET /api/users` - Get user profile
- `PUT /api/users` - Update user profile
- `DELETE /api/users` - Delete user account

#### Media
- `GET /api/media` - List media entries (supports filters)
- `GET /api/media/{id}` - Get specific media entry
- `POST /api/media` - Create new media entry
- `PUT /api/media/{id}` - Update media entry
- `DELETE /api/media/{id}` - Delete media entry

#### Ratings
- `POST /api/ratings` - Rate or like media
- `GET /api/ratings` - Get ratings

#### Recommendations
- `GET /api/recommendations` - Get personalized recommendations

## Project Structure

```
src/main/java/
├── org/example/
│   ├── Main.java              # Application entry point
│   ├── SimpleServer.java      # HTTP server implementation
│   └── StartHandler.java      # Start endpoint handler
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

Database credentials (Docker):
- Database: `mydb`
- User: `myuser`
- Password: `mypass`
- Port: `5432`

## Development

The Docker Compose setup includes live reloading:
- Changes to Java files trigger automatic recompilation
- No need to rebuild the container for code changes
- Use `docker-compose up --watch` for enhanced file watching (Docker Compose v2.22+)

## Building

### Create JAR file
```bash
mvn clean package
```

The Maven Shade plugin creates a fat JAR with all dependencies included.

## License

This project is a semester project for FHTW.