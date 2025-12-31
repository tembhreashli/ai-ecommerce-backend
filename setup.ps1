# AI E-Commerce Docker Setup Script for Windows PowerShell
# This script sets up the complete Docker environment for local development

$ErrorActionPreference = "Stop"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "AI E-Commerce Docker Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

function Print-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Print-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

function Print-Warning {
    param([string]$Message)
    Write-Host "⚠ $Message" -ForegroundColor Yellow
}

function Print-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor White
}

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor White
Write-Host ""

# Check Docker
try {
    $dockerVersion = docker --version
    Print-Success "Docker is installed"
    Write-Host $dockerVersion
} catch {
    Print-Error "Docker is not installed. Please install Docker Desktop first."
    Write-Host "Visit: https://docs.docker.com/desktop/install/windows-install/"
    exit 1
}

# Check Docker Compose
try {
    $composeVersion = docker compose version
    Print-Success "Docker Compose is installed"
    Write-Host $composeVersion
} catch {
    try {
        $composeVersion = docker-compose --version
        Print-Success "Docker Compose is installed"
        Write-Host $composeVersion
    } catch {
        Print-Error "Docker Compose is not installed."
        exit 1
    }
}

# Check Java (optional)
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Print-Success "Java is installed"
    Write-Host $javaVersion
} catch {
    Print-Warning "Java is not installed (optional for local development)"
}

# Check Maven (optional)
try {
    $mavenVersion = mvn --version | Select-Object -First 1
    Print-Success "Maven is installed"
    Write-Host $mavenVersion
} catch {
    Print-Warning "Maven is not installed (optional for local development)"
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Environment Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Create .env file if it doesn't exist
if (-Not (Test-Path ".env")) {
    Print-Info "Creating .env file from template..."
    Copy-Item ".env.example" ".env"
    Print-Success ".env file created"
    Print-Warning "Please review and update .env file with your configuration"
} else {
    Print-Info ".env file already exists"
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Docker Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Stop any existing containers
Print-Info "Stopping any existing containers..."
try {
    docker compose down --remove-orphans 2>$null
} catch {
    try {
        docker-compose down --remove-orphans 2>$null
    } catch {
        # Ignore errors if no containers are running
    }
}

# Build Docker images
Print-Info "Building Docker images..."
try {
    docker compose build --no-cache
} catch {
    docker-compose build --no-cache
}
Print-Success "Docker images built successfully"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Starting Services" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Start services
Print-Info "Starting all services..."
try {
    docker compose up -d
} catch {
    docker-compose up -d
}

# Wait for services to be healthy
Print-Info "Waiting for services to be healthy..."
Start-Sleep -Seconds 10

# Check service status
Write-Host ""
Print-Info "Service Status:"
try {
    docker compose ps
} catch {
    docker-compose ps
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

Print-Success "All services are running!"
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor White
Write-Host "  🌐 Backend API:         http://localhost:8080/api"
Write-Host "  🐰 RabbitMQ Management: http://localhost:15672 (guest/guest)"
Write-Host "  🗄️  PostgreSQL:         localhost:5432 (postgres/postgres)"
Write-Host "  🔴 Redis:               localhost:6379"
Write-Host ""
Write-Host "Health Check:" -ForegroundColor White
Write-Host "  Backend:  http://localhost:8080/api/actuator/health"
Write-Host ""
Write-Host "Useful Commands:" -ForegroundColor White
Write-Host "  View logs:           docker compose logs -f [service-name]"
Write-Host "  Stop services:       docker compose down"
Write-Host "  Restart services:    docker compose restart"
Write-Host "  Remove volumes:      docker compose down -v"
Write-Host "  Rebuild:             docker compose up -d --build"
Write-Host ""
Write-Host "Database Access:" -ForegroundColor White
Write-Host "  docker exec -it ai-ecommerce-db psql -U postgres -d ai_ecommerce"
Write-Host ""
Write-Host "Test Credentials:" -ForegroundColor White
Write-Host "  Admin:    admin@aiecommerce.com / password123"
Write-Host "  Customer: john@example.com / password123"
Write-Host ""
Print-Warning "Note: Default passwords are for development only. Change them for production!"
Write-Host ""
