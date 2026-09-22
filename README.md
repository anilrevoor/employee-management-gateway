# Employee Management Gateway

## Overview

Employee Management Gateway is a Spring Cloud Gateway application that acts as the entry point between the React frontend and the Employee Management backend.

It routes API requests from the frontend to the appropriate backend service.

## Technology Stack

* Java 21
* Spring Boot
* Spring Cloud Gateway Server WebMVC 5.0.3
* Spring Boot Actuator
* Maven

## Application URL

Gateway:

`http://localhost:8081`

Backend:

`http://localhost:8080`

## Architecture

```text
+----------------------------+
| React Frontend             |
| localhost:3000             |
+-------------+--------------+
              |
              | API Requests
              v
+----------------------------+
| Spring Cloud Gateway       |
| localhost:8081             |
+-------------+--------------+
              |
              | Routing
              v
+----------------------------+
| Employee Management        |
| Spring Boot Backend       |
| localhost:8080             |
+-------------+--------------+
              |
              v
+----------------------------+
| H2 Database                |
+----------------------------+
```

## Gateway Routing

The gateway routes frontend requests to the Employee Management backend.

Backend service:

`http://localhost:8080`

Versioned employee API:

`/api/v1/employees/**`

Example gateway request:

`http://localhost:8081/api/v1/employees`

The gateway forwards the request to the backend.

## Application Names

Gateway application:

```properties
spring.application.name=employee-management-gateway
```

Backend application:

```properties
spring.application.name=employee-management
```

These are separate Spring applications and therefore have different application names.

## Ports

| Application    | Port |
| -------------- | ---: |
| React Frontend | 3000 |
| Gateway        | 8081 |
| Backend        | 8080 |
| Config Server  | 8888 |

## Actuator

Spring Boot Actuator is included for monitoring and health information.

Health endpoint:

`http://localhost:8081/actuator/health`

## Request Flow

For example, when the React application requests employees:

```text
React
 |
 | GET /api/v1/employees
 v
Gateway :8081
 |
 | Route request
 v
Backend :8080
 |
 v
Employee Service
 |
 v
H2 Database
```

## Gateway and Security

The backend remains responsible for application-level security and authorization.

JWT authentication is used for protected employee APIs.

Example:

```text
Authorization: Bearer <JWT_TOKEN>
```

If a request is rejected with `401 Unauthorized` or `403 Forbidden`, verify the authentication token and backend Spring Security configuration.

## Startup Order

Recommended startup order:

```text
1. Employee Config Server :8888
2. Employee Management Backend :8080
3. Employee Management Gateway :8081
4. React Frontend :3000
```

## Testing the Gateway

First verify the backend directly:

```text
http://localhost:8080/api/v1/employees
```

Then verify through the gateway:

```text
http://localhost:8081/api/v1/employees
```

For protected APIs, provide a valid JWT token.

## Troubleshooting

### Gateway returns 404

Check:

* Gateway is running on port `8081`
* Backend is running on port `8080`
* Gateway route predicate matches the requested path
* Backend URI is configured correctly
* The API path uses the correct version

### Gateway returns 401

A `401` normally indicates that authentication is required or the JWT token is missing/invalid.

Check:

```text
Authorization: Bearer <JWT_TOKEN>
```

### Gateway returns 403

A `403` indicates that the request reached security checks but the authenticated user does not have the required authorization.

Check the user's role and backend security configuration.

## Purpose of the Gateway

The gateway provides:

* Single API entry point
* Request routing
* Service abstraction
* Separation between frontend and backend
* Central location for future cross-cutting concerns
* Easier integration with multiple backend services

## Complete Application Architecture

```text
                         +-------------------+
                         | React Frontend    |
                         | :3000             |
                         +---------+---------+
                                   |
                                   v
                         +-------------------+
                         | API Gateway       |
                         | :8081             |
                         +---------+---------+
                                   |
                                   v
                         +-------------------+
                         | Spring Boot       |
                         | Backend :8080     |
                         +---------+---------+
                                   |
                                   v
                         +-------------------+
                         | H2 Database       |
                         +-------------------+

                         +-------------------+
                         | Config Server     |
                         | :8888             |
                         +-------------------+
                                   |
                                   v
                         Configuration Repo
```

## Project Role in the System

The Gateway is the communication layer between the React frontend and backend services.

The overall system consists of:

1. React frontend
2. Spring Cloud Gateway
3. Spring Boot Employee Management backend
4. Spring Cloud Config Server
5. H2 database
