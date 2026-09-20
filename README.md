# Shakthi Mart

Shakthi Mart is a Java Servlet-based marketplace application that connects buyers and sellers.

## Project Overview

Shakthi Mart allows users to register and log in as buyers or sellers.

The project is developed using Java Servlets, JDBC, H2 Database and Apache Tomcat.

## Technology Stack

- Java 17
- Java Servlets
- JDBC
- Apache Tomcat 9
- Maven
- H2 Database
- HikariCP
- BCrypt
- JUnit 5
- GitHub Actions

## Features

- User Registration
- User Login
- Buyer and Seller Roles
- BCrypt Password Hashing
- Session-based Authentication
- Session ID Regeneration
- Session Timeout
- Authentication Filter
- H2 Database
- HikariCP Connection Pool
- PreparedStatement-based Database Operations
- Admin Seed Data
- JUnit 5 Testing
- GitHub Actions CI

## Project Structure

```text
Shakthi-Mart/
├── .github/
│   └── workflows/
│       └── ci.yml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   ├── dao/
│   │   │   ├── filter/
│   │   │   ├── listener/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   └── util/
│   │   └── webapp/
│   │       └── sql/
│   └── test/
│       └── java/
│           └── service/
├── pom.xml
├── README.md
└── .gitignore
