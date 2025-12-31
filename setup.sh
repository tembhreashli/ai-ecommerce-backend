#!/bin/bash

# AI E-Commerce Docker Setup Script
# This script sets up the complete Docker environment for local development

set -e

echo "=========================================="
echo "AI E-Commerce Docker Setup"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${NC}ℹ $1${NC}"
}

# Check prerequisites
echo "Checking prerequisites..."
echo ""

# Check Docker
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker first."
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
else
    print_success "Docker is installed"
    docker --version
fi

# Check Docker Compose
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
else
    print_success "Docker Compose is installed"
    if command -v docker-compose &> /dev/null; then
        docker-compose --version
    else
        docker compose version
    fi
fi

# Check Java (optional, for local development)
if command -v java &> /dev/null; then
    print_success "Java is installed"
    java --version 2>&1 | head -n 1
else
    print_warning "Java is not installed (optional for local development)"
fi

# Check Maven (optional, for local development)
if command -v mvn &> /dev/null; then
    print_success "Maven is installed"
    mvn --version | head -n 1
else
    print_warning "Maven is not installed (optional for local development)"
fi

echo ""
echo "=========================================="
echo "Environment Setup"
echo "=========================================="
echo ""

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    print_info "Creating .env file from template..."
    cp .env.example .env
    print_success ".env file created"
    print_warning "Please review and update .env file with your configuration"
else
    print_info ".env file already exists"
fi

echo ""
echo "=========================================="
echo "Docker Setup"
echo "=========================================="
echo ""

# Stop any existing containers
print_info "Stopping any existing containers..."
if command -v docker-compose &> /dev/null; then
    docker-compose down --remove-orphans 2>/dev/null || true
else
    docker compose down --remove-orphans 2>/dev/null || true
fi

# Build Docker images
print_info "Building Docker images..."
if command -v docker-compose &> /dev/null; then
    docker-compose build --no-cache
else
    docker compose build --no-cache
fi
print_success "Docker images built successfully"

echo ""
echo "=========================================="
echo "Starting Services"
echo "=========================================="
echo ""

# Start services
print_info "Starting all services..."
if command -v docker-compose &> /dev/null; then
    docker-compose up -d
else
    docker compose up -d
fi

# Wait for services to be healthy
print_info "Waiting for services to be healthy..."
sleep 10

# Check service status
echo ""
print_info "Service Status:"
if command -v docker-compose &> /dev/null; then
    docker-compose ps
else
    docker compose ps
fi

echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""

print_success "All services are running!"
echo ""
echo "Service URLs:"
echo "  🌐 Backend API:        http://localhost:8080/api"
echo "  🐰 RabbitMQ Management: http://localhost:15672 (guest/guest)"
echo "  🗄️  PostgreSQL:         localhost:5432 (postgres/postgres)"
echo "  🔴 Redis:              localhost:6379"
echo ""
echo "Health Check:"
echo "  Backend:  http://localhost:8080/api/actuator/health"
echo ""
echo "Useful Commands:"
echo "  View logs:           docker-compose logs -f [service-name]"
echo "  Stop services:       docker-compose down"
echo "  Restart services:    docker-compose restart"
echo "  Remove volumes:      docker-compose down -v"
echo "  Rebuild:             docker-compose up -d --build"
echo ""
echo "Database Access:"
echo "  docker exec -it ai-ecommerce-db psql -U postgres -d ai_ecommerce"
echo ""
echo "Test Credentials:"
echo "  Admin:    admin@aiecommerce.com / password123"
echo "  Customer: john@example.com / password123"
echo ""
print_warning "Note: Default passwords are for development only. Change them for production!"
echo ""
