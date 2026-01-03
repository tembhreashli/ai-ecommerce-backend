# Docker Setup Guide

This guide provides instructions for setting up and running the AI E-Commerce application using Docker and Docker Compose.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Docker** (version 20.10 or higher)
  - [Install Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)
  - [Install Docker Desktop for Mac](https://docs.docker.com/desktop/install/mac-install/)
  - [Install Docker Engine for Linux](https://docs.docker.com/engine/install/)

- **Docker Compose** (version 2.0 or higher)
  - Included with Docker Desktop
  - For Linux: [Install Docker Compose](https://docs.docker.com/compose/install/)

- **Git** (for cloning the repository)

Optional for local development:
- Java 17 or higher
- Maven 3.8 or higher

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/tembhreashli/ai-ecommerce-backend.git
cd ai-ecommerce-backend
```

### 2. Run the Setup Script

**Linux/Mac:**
```bash
./setup.sh
```

**Windows (PowerShell):**
```powershell
.\setup.ps1
```

**Windows (Command Prompt):**
```cmd
setup.bat
```

The setup script will:
- Check prerequisites
- Create environment configuration files
- Build Docker images
- Start all services
- Display service URLs and credentials

### 3. Access the Application

Once all services are running:

- **Backend API**: http://localhost:9090/api
- **Health Check**: http://localhost:9090/api/actuator/health
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)
- **PostgreSQL**: localhost:5432 (postgres/postgres)
- **Redis**: localhost:6379

## Manual Setup

If you prefer to set up manually without using the setup script:

### 1. Create Environment File

```bash
cp .env.example .env
```

Edit `.env` and configure your environment variables as needed.

### 2. Build Docker Images

```bash
docker compose build
```

### 3. Start Services

```bash
docker compose up -d
```

### 4. Check Service Status

```bash
docker compose ps
```

## Docker Services

The application stack includes the following services:

### PostgreSQL Database
- **Image**: postgres:15-alpine
- **Port**: 5432
- **Database**: ai_ecommerce
- **Credentials**: postgres/postgres (default)
- **Features**:
  - Persistent data storage
  - Automatic schema initialization
  - Sample data loading
  - Health checks

### Redis Cache
- **Image**: redis:7-alpine
- **Port**: 6379
- **Features**:
  - Persistent data storage with AOF
  - Health checks

### RabbitMQ Message Broker
- **Image**: rabbitmq:3-management-alpine
- **Ports**:
  - 5672 (AMQP)
  - 15672 (Management UI)
- **Credentials**: guest/guest (default)
- **Features**:
  - Management UI for monitoring
  - Persistent message storage
  - Health checks

### Backend Application
- **Build**: Multi-stage Dockerfile
- **Port**: 9090
- **Features**:
  - Depends on all infrastructure services
  - Health checks
  - Graceful shutdown
  - Hot reload in development mode
  - Debug port 5005 (development)

## Environment Configuration

### Essential Environment Variables

```env
# Database
POSTGRES_DB=ai_ecommerce
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432

# Redis
REDIS_PORT=6379
REDIS_PASSWORD=

# RabbitMQ
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_PORT=5672
RABBITMQ_MANAGEMENT_PORT=15672

# Backend
BACKEND_PORT=9090
JWT_SECRET=your-secret-key-change-in-production
SPRING_PROFILES_ACTIVE=docker
```

### Optional Variables

```env
# Mail Configuration
MAIL_FROM=noreply@aiecommerce.com
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Stripe Payment Integration
STRIPE_API_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...

# OpenAI Integration
OPENAI_API_KEY=sk-...
```

## Common Commands

### Using Docker Compose

```bash
# Start all services
docker compose up -d

# Stop all services
docker compose down

# View logs (all services)
docker compose logs -f

# View logs (specific service)
docker compose logs -f backend
docker compose logs -f postgres
docker compose logs -f redis
docker compose logs -f rabbitmq

# Restart services
docker compose restart

# Rebuild and restart
docker compose up -d --build

# Remove everything including volumes
docker compose down -v

# Check service status
docker compose ps
```

### Using Makefile (Linux/Mac)

```bash
# View all available commands
make help

# Initial setup
make setup

# Start services
make up

# Stop services
make down

# View logs
make logs-f

# Access database shell
make db-shell

# Access Redis CLI
make redis-cli

# Access backend shell
make backend-sh

# Check health
make health

# Show service URLs
make urls
```

## Database Management

### Access PostgreSQL Shell

```bash
docker exec -it ai-ecommerce-db psql -U postgres -d ai_ecommerce
```

### Common SQL Commands

```sql
-- List all tables
\dt

-- Describe a table
\d users

-- View all users
SELECT * FROM users;

-- View all products
SELECT * FROM products;

-- Exit
\q
```

### Database Backup

```bash
# Backup database
docker exec ai-ecommerce-db pg_dump -U postgres ai_ecommerce > backup.sql

# Restore database
docker exec -i ai-ecommerce-db psql -U postgres ai_ecommerce < backup.sql
```

## Redis Management

### Access Redis CLI

```bash
docker exec -it ai-ecommerce-redis redis-cli
```

### Common Redis Commands

```bash
# Check connection
PING

# List all keys
KEYS *

# Get a key value
GET key_name

# Delete a key
DEL key_name

# Flush all data
FLUSHALL

# Exit
EXIT
```

## RabbitMQ Management

### Access Management UI

Open http://localhost:15672 in your browser and login with:
- **Username**: guest
- **Password**: guest

### Features Available
- Queue monitoring
- Exchange management
- Connection tracking
- Message tracing
- Performance metrics

## Troubleshooting

### Services Not Starting

1. Check if ports are already in use:
```bash
# Check port 9090
lsof -i :9090  # Linux/Mac
netstat -ano | findstr :9090  # Windows

# Check port 5432
lsof -i :5432  # Linux/Mac
netstat -ano | findstr :5432  # Windows
```

2. Check Docker logs:
```bash
docker compose logs
```

3. Remove and recreate containers:
```bash
docker compose down -v
docker compose up -d
```

### Database Connection Issues

1. Check if PostgreSQL is healthy:
```bash
docker compose ps postgres
```

2. Verify database credentials in `.env`

3. Check backend logs:
```bash
docker compose logs backend
```

### Out of Memory Errors

Increase Docker memory allocation:
- **Docker Desktop**: Settings → Resources → Memory
- Recommended: 4GB minimum, 8GB preferred

### Permission Issues (Linux)

If you encounter permission issues:
```bash
sudo chown -R $USER:$USER .
```

## Development Mode

### Enable Hot Reload

The `docker-compose.override.yml` file enables development mode with:
- Source code volume mounting
- Maven cache mounting
- Debug port exposure (5005)
- Hot reload enabled

### Connect Debugger

1. Ensure debug port is exposed (5005)
2. Configure your IDE:
   - **IntelliJ IDEA**: Run → Edit Configurations → Remote JVM Debug
   - **VS Code**: Add Java debug configuration
   - **Host**: localhost
   - **Port**: 5005

### Run Tests

```bash
# Inside container
docker compose exec backend mvn test

# Or using Makefile
make test
```

## Production Considerations

For production deployment, make these changes:

1. **Update Environment Variables**:
   - Use strong passwords
   - Change JWT secret
   - Configure real email service
   - Add API keys for external services

2. **Use Production Dockerfile Target**:
```yaml
backend:
  build:
    target: production
```

3. **Remove Development Overrides**:
```bash
docker compose -f docker-compose.yml up -d
```

4. **Enable HTTPS**:
   - Add a reverse proxy (nginx, Traefik)
   - Configure SSL certificates
   - Update environment variables

5. **Resource Limits**:
```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
```

6. **Monitoring**:
   - Enable Prometheus metrics
   - Add Grafana dashboards
   - Configure log aggregation

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Redis Documentation](https://redis.io/documentation)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)

## Support

For issues and questions:
- Check the [GitHub Issues](https://github.com/tembhreashli/ai-ecommerce-backend/issues)
- Review the [Development Guide](DEVELOPMENT.md)
- Consult the [Local Setup Guide](LOCAL_SETUP.md)
