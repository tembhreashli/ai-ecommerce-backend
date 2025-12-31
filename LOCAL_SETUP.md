# Local Development Setup Guide

This guide explains how to set up the AI E-Commerce Backend for local development without Docker.

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - [Download Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
   - [Download OpenJDK](https://adoptium.net/)
   
   Verify installation:
   ```bash
   java --version
   ```

2. **Apache Maven 3.8 or higher**
   - [Download Maven](https://maven.apache.org/download.cgi)
   - [Installation Guide](https://maven.apache.org/install.html)
   
   Verify installation:
   ```bash
   mvn --version
   ```

3. **PostgreSQL 15**
   - [Download PostgreSQL](https://www.postgresql.org/download/)
   - Note: You can use Docker for PostgreSQL only if preferred

4. **Redis**
   - [Download Redis](https://redis.io/download)
   - For Windows: [Redis on Windows](https://github.com/microsoftarchive/redis/releases)
   - Note: You can use Docker for Redis only if preferred

5. **RabbitMQ**
   - [Download RabbitMQ](https://www.rabbitmq.com/download.html)
   - Note: You can use Docker for RabbitMQ only if preferred

### Optional Tools

- **Git** - For version control
- **IntelliJ IDEA** or **Eclipse** - Java IDE
- **Postman** or **Insomnia** - API testing
- **pgAdmin** or **DBeaver** - Database management

## Setup Steps

### 1. Clone the Repository

```bash
git clone https://github.com/tembhreashli/ai-ecommerce-backend.git
cd ai-ecommerce-backend
```

### 2. Set Up PostgreSQL

#### Create Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE ai_ecommerce;

# Create user (optional)
CREATE USER aicommerce WITH PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE ai_ecommerce TO aicommerce;

# Exit
\q
```

#### Initialize Schema

```bash
# Run initialization script
psql -U postgres -d ai_ecommerce -f database/init-db.sql

# Optional: Load sample data
psql -U postgres -d ai_ecommerce -f database/seed-data.sql
```

### 3. Set Up Redis

#### Start Redis Server

**Linux/Mac:**
```bash
redis-server
```

**Windows:**
```cmd
redis-server.exe
```

Verify Redis is running:
```bash
redis-cli ping
# Should return: PONG
```

### 4. Set Up RabbitMQ

#### Start RabbitMQ Server

**Linux:**
```bash
sudo systemctl start rabbitmq-server
sudo systemctl enable rabbitmq-server
```

**Mac (Homebrew):**
```bash
brew services start rabbitmq
```

**Windows:**
- Start from the Start Menu or Services

#### Enable Management Plugin

```bash
rabbitmq-plugins enable rabbitmq_management
```

Access Management UI: http://localhost:15672 (guest/guest)

### 5. Configure Application

#### Create Environment File

Create a `.env` file in the root directory or set environment variables:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=ai_ecommerce
DB_USER=postgres
DB_PASSWORD=postgres

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# RabbitMQ Configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_VHOST=/

# Application Configuration
SERVER_PORT=8080
JWT_SECRET=your-secret-key-at-least-256-bits-long-for-production
```

#### Alternative: Update application.yml

Edit `src/main/resources/application.yml` or create `src/main/resources/application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_ecommerce
    username: postgres
    password: postgres
  
  redis:
    host: localhost
    port: 6379
    password: 
  
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

server:
  port: 8080

app:
  jwt:
    secret: your-secret-key-at-least-256-bits-long
```

### 6. Build the Application

```bash
# Clean and build
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

### 7. Run the Application

#### Option 1: Using Maven

```bash
mvn spring-boot:run
```

#### Option 2: Using IDE

1. Open the project in your IDE
2. Navigate to `src/main/java/com/ecommerce/AiEcommerceBackendApplication.java`
3. Right-click and select "Run" or "Debug"

#### Option 3: Using JAR

```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/ai-ecommerce-backend-1.0.0.jar
```

### 8. Verify Installation

The application should start on port 8080. Verify by accessing:

- **Health Check**: http://localhost:8080/api/actuator/health
- **API Base**: http://localhost:8080/api

Expected health check response:
```json
{
  "status": "UP"
}
```

## Testing the API

### Using cURL

#### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

#### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john@example.com",
    "password": "password123"
  }'
```

#### Get Products

```bash
curl http://localhost:8080/api/products
```

### Test Credentials

If you loaded the seed data, you can use these credentials:

- **Admin**: admin@aiecommerce.com / password123
- **Customer**: john@example.com / password123
- **Customer**: jane@example.com / password123
- **Vendor**: vendor@example.com / password123

## Development Workflow

### Hot Reload with Spring DevTools

Spring DevTools is included in the dependencies and enables hot reload:

1. Make changes to your code
2. Save the file
3. The application will automatically restart

### IDE Configuration

#### IntelliJ IDEA

1. **Enable annotation processing**:
   - File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"

2. **Configure Spring Boot**:
   - Run → Edit Configurations → Add New → Spring Boot
   - Main class: `com.ecommerce.AiEcommerceBackendApplication`
   - Active profiles: `local`

3. **Enable hot reload**:
   - File → Settings → Build, Execution, Deployment → Compiler
   - Check "Build project automatically"
   - Advanced Settings → Allow auto-make to start even if developed application is currently running

#### Visual Studio Code

1. Install extensions:
   - Java Extension Pack
   - Spring Boot Extension Pack

2. Create `.vscode/launch.json`:
```json
{
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "com.ecommerce.AiEcommerceBackendApplication",
      "projectName": "ai-ecommerce-backend",
      "args": "--spring.profiles.active=local"
    }
  ]
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Database Management

#### Using psql

```bash
# Connect to database
psql -U postgres -d ai_ecommerce

# List tables
\dt

# Describe table
\d users

# Query data
SELECT * FROM users;

# Exit
\q
```

#### Using GUI Tools

- **pgAdmin**: Full-featured PostgreSQL GUI
- **DBeaver**: Universal database tool
- **DataGrip**: JetBrains database IDE

### Redis Management

```bash
# Connect to Redis
redis-cli

# View all keys
KEYS *

# Get value
GET key_name

# Delete key
DEL key_name

# Monitor commands
MONITOR

# Exit
EXIT
```

## Troubleshooting

### Port Already in Use

If port 8080 is already in use:

```bash
# Find process using port 8080
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Kill the process
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows
```

Or change the application port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Database Connection Failed

1. Verify PostgreSQL is running:
```bash
pg_isready -U postgres
```

2. Check connection parameters in configuration

3. Verify database exists:
```bash
psql -U postgres -l
```

### Redis Connection Failed

1. Verify Redis is running:
```bash
redis-cli ping
```

2. Check Redis logs:
```bash
# Linux
tail -f /var/log/redis/redis-server.log

# Mac (Homebrew)
tail -f /usr/local/var/log/redis.log
```

### RabbitMQ Connection Failed

1. Verify RabbitMQ is running:
```bash
rabbitmq-diagnostics ping
```

2. Check RabbitMQ status:
```bash
rabbitmq-diagnostics status
```

### Maven Build Failures

1. Clean Maven cache:
```bash
mvn clean
mvn dependency:purge-local-repository
```

2. Update dependencies:
```bash
mvn clean install -U
```

### Lombok Issues

If Lombok annotations don't work:

1. Enable annotation processing in IDE
2. Install Lombok plugin for your IDE
3. Rebuild project

## Environment Profiles

Use Spring profiles to manage different configurations:

### Development Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Test Profile

```bash
mvn test -Dspring.profiles.active=test
```

### Create Custom Profile

Create `application-custom.yml` in `src/main/resources/`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_ecommerce_custom
```

Run with:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=custom
```

## Next Steps

- Review [Development Guide](DEVELOPMENT.md) for coding standards
- Check [Docker Setup Guide](DOCKER_SETUP.md) for containerized development
- Explore the API documentation (if Swagger/OpenAPI is configured)
- Set up your preferred API testing tool

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Redis Documentation](https://redis.io/documentation)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [Maven Documentation](https://maven.apache.org/guides/)
