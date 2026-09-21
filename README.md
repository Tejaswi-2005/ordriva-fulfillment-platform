# Ordriva — Fulfillment Platform

Ordriva is a production-style order fulfillment and inventory management platform designed to demonstrate modern full-stack backend architecture and enterprise application development.

The platform provides a centralized interface for managing orders, products, inventory, warehouses, payments, and fulfillment operations.

## Features

- Order management and order status tracking
- Product and inventory management
- Warehouse management
- Fulfillment workflow
- Payment tracking
- JWT-based authentication and authorization
- Role-based security
- RESTful backend APIs
- Database migrations and development seed data
- Responsive operations dashboard
- Health monitoring and API documentation support

## Tech Stack

### Frontend
- React
- TypeScript
- Vite
- Tailwind CSS

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven
- REST APIs

### Database & Infrastructure
- SQL
- Flyway Database Migrations
- Docker-ready architecture
- Git & GitHub

## Architecture

Ordriva follows a modular backend structure with separate domains for:

- Authentication
- Orders
- Products
- Inventory
- Warehouses
- Fulfillment
- Payments
- Notifications
- Events

The backend separates API, service, repository, security, and domain layers to provide a maintainable and scalable application structure.

## Project Structure

```text
Ordriva-Fulfillment-Platform/
│
├── artifacts/
│   ├── api-server/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/ordriva/
│   │       └── resources/
│   │
│   └── ordriva/
│       ├── public/
│       ├── src/
│       └── package.json
│
├── lib/
├── scripts/
├── package.json
├── pnpm-workspace.yaml
└── README.md
```

## Getting Started

### Prerequisites

Install:

- Node.js
- pnpm
- Java
- Maven
- Git

### Install frontend dependencies

```bash
pnpm install
```

### Run the frontend

```bash
cd artifacts/ordriva
pnpm run dev
```

The frontend will run locally using Vite.

### Run the Spring Boot backend

```bash
cd artifacts/api-server
mvn spring-boot:run
```

Configure the required database and environment variables before starting the backend.

## Security

Sensitive environment variables and credentials are excluded from version control. Authentication is implemented using Spring Security and JWT-based authorization.

## Future Enhancements

- Redis caching
- Message queues for asynchronous order processing
- Dockerized deployment
- Cloud deployment
- Real-time order events
- Advanced analytics
- CI/CD pipeline
- Observability and monitoring

## Author

**Tejaswi Kunche**

B.Tech Computer Science & Engineering — Artificial Intelligence & Machine Learning

GitHub: [Tejaswi-2005](https://github.com/Tejaswi-2005)

---

If you find this project useful, feel free to explore the repository and its implementation.
