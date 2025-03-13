# StellioCode Backend 🚀

Welcome to the backend project for **StellioCode Software House**. This project is designed to provide API services for the company's various business operations. The backend is built using **Spring Boot** and provides support for authentication, project management, cloud storage integration, and payment processing.

## Table of Contents 📚

1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Features](#features)
4. [Environment Configuration](#environment-configuration)
5. [Running the Application](#running-the-application)
6. [API Documentation](#api-documentation)
7. [Dependencies](#dependencies)

## Project Overview 🏢

**StellioCode** is a software house that serves different types of users:

- **Developers** 👨‍💻: Manage projects, track progress, and interact with other users.
- **Administrators** 🛠️: Handle the management of the entire system, users, and projects.
- **Clients** 💼: View progress, interact with assigned developers, and manage their projects.

## Technologies Used 🛠️

- **Spring Boot 3.4**: Framework for building the application.
- **PostgreSQL**: Database for storing data.
- **JWT**: Used for secure authentication.
- **Cloudinary**: Integration for storing images and other media files.
- **Stripe**: Payment processing integration.
- **Spring Security**: For authentication and authorization.
- **Spring Data JPA**: For interacting with the database.
- **Springdoc OpenAPI**: For API documentation.

## Features ✨

- **User Authentication** 🔐: Secure JWT-based authentication.
- **Project Management** 📊: Support for managing projects with progress tracking.
- **Cloud File Storage** ☁️: Integration with Cloudinary for handling media.
- **Payment Integration** 💳: Stripe integration for handling payments.
- **Multi-role Access** 🔑: Differentiated access for **Developers**, **Administrators**, and **Clients**.

## Environment Configuration ⚙️

Before running the application, make sure to configure the following environment variables:

### Database Configuration 🗃️

The application connects to a **PostgreSQL** database. You need to provide the following properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stelliocode
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### JWT Configuration 🔑

JWT is used for user authentication. Set the JWT secret key:

```properties
jwt.secret=secret
```

### Cloudinary Configuration ☁️

Cloudinary is used for storing files (images, etc.). Set the following:

```properties
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

### Stripe Configuration 💳

Stripe is integrated for payment processing. Set your Stripe API key:

```properties
stripe.api-key=your-stripe-api-key
```

### CORS Configuration 🌐

Allow requests from your frontend application:

```properties
app.cors.allowed-origin=http://localhost:3000
```

### File Upload Configuration 📂

Configure the maximum file size for uploads:

```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Running the Application 🏃‍♂️

To run the application, make sure you have the following dependencies installed:
- **Java 17** (the application is built for Java 17)
- **Maven** (to manage dependencies and build the project)
- **PostgreSQL** (or any other relational database that you configure)

### Running the Application Locally 🌍
1. Clone the repository:
    ```bash
    git clone https://github.com/AlexandreDresch/StellioCode-Server.git
    cd StellioCode-Server
    ```
2. Install dependencies:
   ```bash
    mvn clean install
    ```
3. Start the application:
    ```bash
    mvn spring-boot:run
    ```
The application should now be running on http://localhost:8080.

## API Documentation 📑

The API documentation is generated using **Springdoc OpenAPI** and can be accessed at:
```bash
    http://localhost:8080/swagger-ui.html
```
This provides a detailed view of all the available endpoints, request/response formats, and authentication requirements.

## Dependencies 📦
Here are the key dependencies used in the project:

- **Spring Boot Starter Data JPA**: For database interaction.
- **Spring Boot Starter HATEOAS**: For HATEOAS (Hypermedia as the engine of application state) support.
- **jjwt**: For JWT token handling.
- **Spring Security**: For securing the API.
- **Spring Boot Starter Validation**: For validating input data.
- **Cloudinary HTTP44**: For handling media storage.
- **Stripe Java SDK**: For payment integration.
- **Springdoc OpenAPI Starter**: For generating the Swagger UI documentation.
- **PostgreSQL JDBC Driver**: For connecting to a PostgreSQL database.
- **Lombok**: For reducing boilerplate code (getters, setters, constructors).