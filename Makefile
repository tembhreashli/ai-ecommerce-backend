.PHONY: help build up down restart logs clean ps test

# Default target
help:
	@echo "AI E-Commerce Docker Management"
	@echo ""
	@echo "Available targets:"
	@echo "  make setup      - Run initial setup"
	@echo "  make build      - Build all Docker images"
	@echo "  make up         - Start all services"
	@echo "  make down       - Stop all services"
	@echo "  make restart    - Restart all services"
	@echo "  make logs       - View logs from all services"
	@echo "  make logs-f     - Follow logs from all services"
	@echo "  make ps         - List running containers"
	@echo "  make clean      - Remove all containers and volumes"
	@echo "  make clean-all  - Remove everything including images"
	@echo "  make db-shell   - Open PostgreSQL shell"
	@echo "  make redis-cli  - Open Redis CLI"
	@echo "  make backend-sh - Open shell in backend container"
	@echo "  make test       - Run backend tests"
	@echo ""

# Setup environment
setup:
	@echo "Setting up AI E-Commerce environment..."
	@if [ ! -f .env ]; then cp .env.example .env; echo "Created .env file"; fi
	@chmod +x setup.sh
	@./setup.sh

# Build Docker images
build:
	@echo "Building Docker images..."
	@docker compose build

# Start services
up:
	@echo "Starting services..."
	@docker compose up -d

# Stop services
down:
	@echo "Stopping services..."
	@docker compose down

# Restart services
restart:
	@echo "Restarting services..."
	@docker compose restart

# View logs
logs:
	@docker compose logs

# Follow logs
logs-f:
	@docker compose logs -f

# View logs for specific service
logs-backend:
	@docker compose logs -f backend

logs-db:
	@docker compose logs -f postgres

logs-redis:
	@docker compose logs -f redis

logs-rabbitmq:
	@docker compose logs -f rabbitmq

# List running containers
ps:
	@docker compose ps

# Clean up containers and volumes
clean:
	@echo "Cleaning up containers and volumes..."
	@docker compose down -v

# Clean up everything including images
clean-all:
	@echo "Cleaning up everything..."
	@docker compose down -v --rmi all

# Database shell
db-shell:
	@docker exec -it ai-ecommerce-db psql -U postgres -d ai_ecommerce

# Redis CLI
redis-cli:
	@docker exec -it ai-ecommerce-redis redis-cli

# Backend shell
backend-sh:
	@docker exec -it ai-ecommerce-backend sh

# Run backend tests
test:
	@echo "Running backend tests..."
	@docker compose exec backend mvn test

# Rebuild and restart
rebuild:
	@echo "Rebuilding and restarting services..."
	@docker compose up -d --build

# Check service health
health:
	@echo "Checking service health..."
	@curl -s http://localhost:9090/api/actuator/health | jq .

# Show service URLs
urls:
	@echo "Service URLs:"
	@echo "  Backend API:         http://localhost:9090/api"
	@echo "  Backend Health:      http://localhost:9090/api/actuator/health"
	@echo "  RabbitMQ Management: http://localhost:15672 (guest/guest)"
	@echo "  PostgreSQL:          localhost:5432 (postgres/postgres)"
	@echo "  Redis:               localhost:6379"
