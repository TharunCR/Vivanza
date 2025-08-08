# Event-Management-System

## Project Overview

Event-Management-System is a comprehensive web application built with Spring Boot framework to manage various types of events efficiently. The system allows users to register, login, create, update, and manage events while providing robust security through JWT-based authentication. Flyway manages database migrations to ensure seamless schema updates.

This system is ideal for organizations or communities looking to streamline their event lifecycle management with a modern backend API-driven solution.

---

## Key Features

- **User Registration and Authentication** using Spring Security with JWT tokens.
- **Role-based Access Control** (e.g., USER, ADMIN roles).
- **Event Management**: Create, update, delete, and view events.
- **Database Migration** handled by Flyway for version-controlled schema updates.
- **Global Exception Handling** for consistent and clear error responses.
- **Unit Testing** implemented with JUnit and coverage reporting via Jacoco.
- **API Documentation and Testing** with Swagger UI.
- **Internationalization Support** (example: Accept-Language header usage).

---

## Technologies Used

| Technology                | Purpose                                      |
|--------------------------|----------------------------------------------|
| Spring Boot 3.2.4        | Application framework                         |
| Spring Security (JWT)    | Secure authentication and authorization      |
| Flyway                   | Database schema migration and version control|
| MySQL 8.3                | Relational database                          |
| JUnit + Jacoco           | Unit testing and code coverage               |
| Swagger Explorer         | API documentation and testing                |
| Maven                    | Build and dependency management              |

---

## System Architecture

- **Controller Layer:** REST APIs that receive HTTP requests and return responses.
- **Service Layer:** Business logic processing.
- **Repository Layer:** Interacts with MySQL database.
- **Security Layer:** JWT-based authentication and authorization.
- **Exception Handling:** Centralized global exception handler for API errors.

---

## Prerequisites

- Java Development Kit (JDK) 17
- MySQL Server 8.3 (Ensure MySQL is installed and running)
- Maven (for building and running the project)
- IDE or code editor (e.g., IntelliJ IDEA, VS Code)

---

## API Documentation and Testing
Swagger UI is available at:
http://localhost:8081/swagger-ui/index.html

---

## Future Enhancements
Add event reminders/notifications via email or SMS.
Implement an Admin panel for managing users and events.
Integrate payment gateway for paid event registration.
Add frontend client (React/Angular) to consume APIs.
Enable multi-language support.
Extend role management (Admin, Organizer, User, etc.).
Add detailed event analytics and reporting.

--- 

Tharun C R

