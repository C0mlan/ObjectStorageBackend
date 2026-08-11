# Object Storage Backend

## Project overview

Object Uploader is a backend-focused file management service designed to provide a secure and scalable way for clients to upload and retrieve objects from cloud-based object storage.

Instead of transferring files through the application server, the system generates a presigned URLs that allow clients to communicate directly with the object storage service. This reduces server bandwidth and processing requirements while improving scalability for larger files.

### Key Features
- JWT-based authentication and authorization

## Technology Stack:
| Category | Technology                      |
|----------|---------------------------------|
| Language | Java 21                         |
| Framework | Spring Boot 3.5.16              |
| Security | Spring Security, JWT            |
| Database | PostgreSQL                      |
| Database Migration | Flyway                          |
| Build Tool | Maven                           |
| Testing | JUnit 5,Testcontainers, Mockito |
| Code Coverage | JaCoCo                          |
| Containerization | Docker, Docker Compose          |
| CI/CD | GitHub Actions                  |
| API Documentation | SpringDoc OpenAPI, Swagger UI   |


## 🛠 Getting Started

### Prerequisites

Before running the project, make sure you have the following installed:

* Java 21
* Docker
* Git

### Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/C0mlan/ObjectStorageBackend.git
cd ObjectStorageBackend
```

### Configure Environment Variables

Create a `.env` file in the project root:

Copy the environment variables from `.env.example` into `.env` and update the values according to your local environment.

### Start the Services

The project uses Docker Compose to run its required infrastructure services, including:

* PostgreSQL
* Redis

Start the services with:

```bash
docker compose up --build
```

To verify that the services are running:

```bash
docker compose ps
```

### Run the Application

Start the Spring Boot application using the Maven Wrapper:

```bash
doker compose up
```

The application will be available at:

```text
http://localhost:7088
```

### Run the Tests

Run the complete test suite using the Maven Wrapper:

```bash
./mvnw clean verify
```

This command will:

* Execute unit tests
* Execute integration tests
* Generate the JaCoCo code coverage report

### Verify the Installation

Once the application is running,
* Confirming that PostgreSQL and Redis are running:

```bash
docker compose ps
```

* Running the test suite successfully:

```bash
./mvnw verify
```


## Features

### Authentication & Authorization

* User registration with validated user credentials.
* Secure user login and authentication.
* JWT-based access and refresh token authentication.
* Protected API endpoints accessible only to authenticated users.

## Architecture

## API Documentation

The API follows RESTful design principles and uses JWT-based authentication.


For detailed endpoint specifications, request/response schemas,
authentication requirements, and error responses, see:



[- API Reference](docs/api_documentation.md)

