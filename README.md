# 🔗 TinyURL - URL Shortener Backend

A production-ready URL shortening REST API built using **Spring Boot**, **Spring Security**, **JWT Authentication**, and **MySQL**.

It provides secure authentication, URL shortening, analytics, QR code generation, role-based authorization, and an admin dashboard. The backend is containerized with Docker and deployed on Render with a cloud-hosted MySQL database.

> 🚀 Developed as a full-stack portfolio project following modern backend development practices.

---

## 🌐 Live Demo

| Service | URL |
|---------|-----|
| Frontend | https://tinyurl-fronted.onrender.com |
| Backend API | https://tinyurl-backend-nv4d.onrender.com |

---

## 🔗 Related Repository

Frontend Repository

https://github.com/Fenilsavaliya23/tinyurl-fronted

![Java](https://img.shields.io/badge/Java-23-orange)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-green)

![React](https://img.shields.io/badge/React-19-blue)

![JWT](https://img.shields.io/badge/Auth-JWT-red)

![Docker](https://img.shields.io/badge/Docker-Supported-blue)

![Render](https://img.shields.io/badge/Deployment-Render-purple)

![MySQL](https://img.shields.io/badge/Database-MySQL-blue)

![License](https://img.shields.io/badge/License-MIT-green)

## 📖 Project Overview

TinyURL Backend is a secure RESTful web application that enables users to shorten long URLs, create custom aliases, monitor analytics, generate QR codes, and manage URLs through role-based authentication.

The project follows a layered architecture using Spring Boot and Spring Security while implementing JWT authentication for secure access. It also includes an admin module for user and URL management.

Designed as a production-ready portfolio project, the application is deployed on Render and connected to a cloud-hosted MySQL database.

## ✨ Features

### Authentication

- User Registration
- Secure Login
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization
- Protected REST APIs

### URL Management

- Shorten Long URLs
- Custom Alias
- Edit Alias
- Delete URL
- Expiration Support

### Analytics

- Click Tracking
- URL Statistics
- Dashboard Analytics
- Top Performing URLs
- Most Clicked URL

### QR Code

- Generate QR Codes
- Download QR Code

### Admin

- User Management
- URL Management
- Dashboard Statistics

### Security

- Spring Security
- JWT Token Validation
- BCrypt Password Hashing
- Global Exception Handling
- Input Validation

### Deployment

- Docker
- Render
- Cloud MySQL Database
- Environment Variables

## 🛠️ Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

### Database

- MySQL
- Railway MySQL (Production)

### Authentication

- JWT (JSON Web Token)
- BCrypt Password Encoder

### Cloud & Deployment

- Render
- Docker
- GitHub

### Utilities

- QR Code Generator
- RESTful APIs
- Global Exception Handling
- Bean Validation

---

## 🏗️ System Architecture

```

```
                     +-------------------------+
                     |      React Frontend     |
                     |        (Render)         |
                     +-----------+-------------+
                                 |
                                 | HTTPS REST API
                                 |
                     +-----------v-------------+
                     | Spring Boot Backend API |
                     |        (Render)         |
                     +-----------+-------------+
                                 |
                 +---------------+---------------+
                 |                               |
        JWT Authentication              Spring Security
                 |                               |
                 +---------------+---------------+
                                 |
                          Service Layer
                                 |
                          Repository Layer
                                 |
                           Hibernate / JPA
                                 |
                     +-----------v------------+
                     |   Railway MySQL DB     |
                     +------------------------+
```

---

## 📂 Project Structure

```

src
├── main
│   ├── java
│   │   └── com
│   │       └── tinyurl
│   │           ├── config
│   │           ├── controller
│   │           ├── dto
│   │           ├── entity
│   │           ├── exception
│   │           ├── repository
│   │           ├── security
│   │           ├── service
│   │           └── util
│   │
│   └── resources
│       ├── application.properties
│       └── application-prod.properties
│
├── Dockerfile
├── pom.xml
└── README.md

```

---

## 🔐 Security Features

- JWT Authentication
- BCrypt Password Hashing
- Role-Based Access Control (USER / ADMIN)
- Protected REST Endpoints
- CORS Configuration
- Global Exception Handling
- Request Validation
- Secure Password Storage

---

## ✨ Core Functionalities

### 👤 User Module

- User Registration
- Secure Login
- JWT Token Generation
- Profile-based Authorization

---

### 🔗 URL Module

- Create Short URL
- Custom Alias
- Edit Alias
- Delete URL
- URL Details
- URL Expiration Support

---

### 📊 Analytics Module

- Total URLs
- Total Clicks
- Click Statistics
- Top Performing URLs
- Dashboard Overview

---

### 📱 QR Code Module

- Generate QR Code
- Preview QR Code
- Download QR Code

---

### 👨‍💼 Admin Module

- Dashboard Statistics
- User Management
- URL Management
- Promote User
- Demote User
- Delete Users
- Delete URLs

---

## 🚀 REST API Overview

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/signup` | Register User |
| POST | `/api/auth/login` | Login User |
| POST | `/api/v1/url/shorten` | Shorten URL |
| GET | `/api/v1/url/dashboard` | Dashboard Statistics |
| GET | `/api/v1/url/my-urls` | User URLs |
| PATCH | `/api/v1/url/{shortCode}/alias` | Update Alias |
| DELETE | `/api/v1/url/{shortCode}` | Delete URL |
| GET | `/api/v1/url/{shortCode}/qr` | QR Code |
| GET | `/r/{shortCode}` | Redirect URL |

---

## ⚙️ Environment Variables

| Variable | Description |
|----------|-------------|
| DB_URL | Railway MySQL JDBC URL |
| DB_USERNAME | Railway Database Username |
| DB_PASSWORD | Railway Database Password |
| JWT_SECRET | JWT Secret Key |
| PORT | Server Port (Provided by Render) |
| SPRING_PROFILES_ACTIVE | Active Spring Profile |

---

## 🐳 Docker Support

The backend is fully containerized using Docker.

Build the Docker image

```bash
docker build -t tinyurl-backend .
```

Run the container

```bash
docker run -p 8080:8080 tinyurl-backend
```

---

## 🚀 Local Installation

### Clone Repository

```bash
git clone https://github.com/Fenilsavaliya23/tinyurl-springboot.git
```

Move into project

```bash
cd tinyurl-springboot
```

Install dependencies

```bash
mvn clean install
```

Run project

```bash
mvn spring-boot:run
```

---

## 🌐 Live Deployment

### Backend

https://tinyurl-backend-nv4d.onrender.com

### Frontend

https://tinyurl-fronted.onrender.com

---

## 📈 Future Enhancements

- Google OAuth Login
- GitHub OAuth Login
- Email Verification
- Forgot Password
- Password Reset
- URL Password Protection
- Custom Domains
- Redis Caching
- API Rate Limiting
- Unit & Integration Testing
- GitHub Actions CI/CD

---

## 🤝 Contributing

Contributions, issues and feature requests are welcome.

Feel free to fork this repository and submit a Pull Request.

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Developer

**Fenil Savaliya**

Backend Repository

https://github.com/Fenilsavaliya23/tinyurl-springboot

Frontend Repository

https://github.com/Fenilsavaliya23/tinyurl-fronted

If you found this project helpful, consider giving it a ⭐ on GitHub.
