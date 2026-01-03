# AI E-Commerce Backend

A modern, AI-powered e-commerce backend built with Spring Boot, PostgreSQL, Redis, and RabbitMQ.

## Features

- 🔐 JWT-based authentication and authorization
- 🛍️ Complete e-commerce functionality (products, cart, orders)
- 💾 PostgreSQL database with JPA/Hibernate
- ⚡ Redis caching for improved performance
- 🐰 RabbitMQ message broker for async operations
- 🔄 RESTful API design
- 🐳 Docker & Docker Compose support
- 📊 Spring Boot Actuator for monitoring
- 🔒 Spring Security for authentication

## Quick Start with Docker

The fastest way to get started is using Docker Compose:

```bash
# Clone the repository
git clone https://github.com/tembhreashli/ai-ecommerce-backend.git
cd ai-ecommerce-backend

# Run the setup script (Linux/Mac)
./setup.sh

# Or on Windows (PowerShell)
.\setup.ps1

# Or manually
docker compose up -d
```

Access the application:
- **Backend API**: http://localhost:9090/api
- **Health Check**: http://localhost:9090/api/actuator/health
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## Documentation

- 📖 [Docker Setup Guide](DOCKER_SETUP.md) - Complete Docker setup and usage
- 💻 [Local Development Setup](LOCAL_SETUP.md) - Setup without Docker
- 🛠️ [Development Guide](DEVELOPMENT.md) - Development workflows and best practices

## Tech Stack

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **PostgreSQL 15** - Database
- **Redis** - Caching layer
- **RabbitMQ** - Message broker
- **Maven** - Build tool
- **Docker** - Containerization
- **JWT** - Authentication tokens

## Prerequisites

### For Docker Setup
- Docker 20.10+
- Docker Compose 2.0+

### For Local Development
- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Redis
- RabbitMQ

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Products
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (Admin)
- `PUT /api/products/{id}` - Update product (Admin)
- `DELETE /api/products/{id}` - Delete product (Admin)

### Cart
- `GET /api/cart` - Get user's cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{id}` - Update cart item
- `DELETE /api/cart/items/{id}` - Remove item from cart

### Orders
- `GET /api/orders` - List user's orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create order

## Environment Variables

Key environment variables (see `.env.example` for complete list):

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=ai_ecommerce
DB_USER=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672

# Application
SERVER_PORT=9090
JWT_SECRET=your-secret-key
```

## Development Commands

### Using Docker Compose

```bash
# Start services
docker compose up -d

# Stop services
docker compose down

# View logs
docker compose logs -f backend

# Rebuild
docker compose up -d --build
```

### Using Makefile (Linux/Mac)

```bash
# Start services
make up

# Stop services
make down

# View logs
make logs-f

# Access database
make db-shell
```

### Using Maven

```bash
# Build
mvn clean install

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

## Testing

Test credentials (with seed data loaded):

- **Admin**: admin@aiecommerce.com / password123
- **Customer**: john@example.com / password123
- **Customer**: jane@example.com / password123

## Project Structure

```
ai-ecommerce-backend/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── config/           # Configuration
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── model/            # Entity Models
│   │   │   ├── repository/       # JPA Repositories
│   │   │   ├── service/          # Business Logic
│   │   │   └── security/         # Security Config
│   │   └── resources/
│   │       └── application.yml   # Configuration
│   └── test/                      # Tests
├── database/                      # Database scripts
├── docker-compose.yml            # Docker Compose config
├── Dockerfile.backend            # Backend Dockerfile
└── pom.xml                       # Maven config
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Support

For questions or issues:
- Create an issue on GitHub
- Check the [Documentation](DOCKER_SETUP.md)

## Acknowledgments

Built with ❤️ using Spring Boot and modern cloud-native technologies.
