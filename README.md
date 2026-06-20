# TinyURL Backend

A scalable URL Management Platform built using Spring Boot, JWT Authentication, MySQL, and Docker.

This backend provides secure REST APIs for URL shortening, analytics tracking, user authentication, and URL management.

## Features

### Security

* JWT Authentication
* BCrypt Password Encryption
* Protected REST APIs
* Role-Based Architecture Ready

### URL Management

* Create Short URLs
* Custom Alias Support
* Edit Alias
* Delete URLs
* URL Ownership Validation
* URL Expiration Management

### Analytics

* Click Tracking
* URL Statistics
* Last Access Monitoring
* Analytics Dashboard Support

### Additional Features

* QR Code Generation
* Global Exception Handling
* Validation Support
* Scheduled Expired URL Cleanup

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Security

* JWT
* BCrypt

### DevOps

* Docker
* Docker Compose
* Maven
* GitHub

## API Modules

### Authentication APIs

```http
POST /api/auth/signup
POST /api/auth/login
```

### URL APIs

```http
POST   /api/v1/url/shorten
GET    /api/v1/url/stats/{shortCode}
PUT    /api/v1/url/update-alias
DELETE /api/v1/url/{id}
GET    /r/{shortCode}
```

## Architecture

```text
React Frontend
        │
        ▼
Spring Boot REST API
        │
        ▼
Spring Security + JWT
        │
        ▼
Service Layer
        │
        ▼
JPA / Hibernate
        │
        ▼
MySQL Database
```

## Running Locally

### Clone Repository

```bash
git clone <repository-url>
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

### Docker

```bash
docker compose up --build
```

## Future Enhancements

* Swagger/OpenAPI Documentation
* Refresh Token Support
* Rate Limiting
* Redis Caching
* User Activity Logs
* Admin Dashboard

## Author

Fenil Savaliya
