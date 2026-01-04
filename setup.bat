@echo off
REM AI E-Commerce Docker Setup Script for Windows
REM This script sets up the complete Docker environment for local development

echo ==========================================
echo AI E-Commerce Docker Setup
echo ==========================================
echo.

REM Check Docker
echo Checking prerequisites...
echo.

docker --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Docker is not installed. Please install Docker Desktop first.
    echo Visit: https://docs.docker.com/desktop/install/windows-install/
    exit /b 1
)
echo [OK] Docker is installed
docker --version

REM Check Docker Compose
docker compose version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    docker-compose --version >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Docker Compose is not installed.
        exit /b 1
    )
    echo [OK] Docker Compose is installed
    docker-compose --version
) else (
    echo [OK] Docker Compose is installed
    docker compose version
)

echo.
echo ==========================================
echo Environment Setup
echo ==========================================
echo.

REM Create .env file if it doesn't exist
if not exist .env (
    echo Creating .env file from template...
    copy .env.example .env
    echo [OK] .env file created
    echo [WARNING] Please review and update .env file with your configuration
) else (
    echo [INFO] .env file already exists
)

echo.
echo ==========================================
echo Docker Setup
echo ==========================================
echo.

REM Stop any existing containers
echo Stopping any existing containers...
docker compose down --remove-orphans 2>nul
if %ERRORLEVEL% NEQ 0 (
    docker-compose down --remove-orphans 2>nul
)

REM Build Docker images
echo Building Docker images...
docker compose build --no-cache
if %ERRORLEVEL% NEQ 0 (
    docker-compose build --no-cache
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to build Docker images
        exit /b 1
    )
)
echo [OK] Docker images built successfully

echo.
echo ==========================================
echo Starting Services
echo ==========================================
echo.

REM Start services
echo Starting all services...
docker compose up -d
if %ERRORLEVEL% NEQ 0 (
    docker-compose up -d
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to start services
        exit /b 1
    )
)

REM Wait for services to be healthy
echo Waiting for services to be healthy...
timeout /t 10 /nobreak >nul

REM Check service status
echo.
echo Service Status:
docker compose ps
if %ERRORLEVEL% NEQ 0 (
    docker-compose ps
)

echo.
echo ==========================================
echo Setup Complete!
echo ==========================================
echo.

echo [OK] All services are running!
echo.
echo Service URLs:
echo   Backend API:         http://localhost:9090/api
echo   RabbitMQ Management: http://localhost:15672 (guest/guest)
echo   PostgreSQL:          localhost:5432 (postgres/postgres)
echo   Redis:               localhost:6379
echo.
echo Health Check:
echo   Backend:  http://localhost:9090/api/actuator/health
echo.
echo Useful Commands:
echo   View logs:           docker compose logs -f [service-name]
echo   Stop services:       docker compose down
echo   Restart services:    docker compose restart
echo   Remove volumes:      docker compose down -v
echo   Rebuild:             docker compose up -d --build
echo.
echo Database Access:
echo   docker exec -it ai-ecommerce-db psql -U postgres -d ai_ecommerce
echo.
echo Test Credentials:
echo   Admin:    admin@aiecommerce.com / password123
echo   Customer: john@example.com / password123
echo.
echo [WARNING] Note: Default passwords are for development only. Change them for production!
echo.

pause
