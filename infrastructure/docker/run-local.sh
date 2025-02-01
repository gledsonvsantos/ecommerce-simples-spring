#!/bin/bash

# Build the application
echo "Building the application..."
cd ../../app && ./mvnw clean package -DskipTests

# Build and start containers
echo "Starting containers..."
cd ../infrastructure/docker && docker-compose up --build -d

# Wait for services to be ready
echo "Waiting for services to be ready..."
sleep 10

# Show running containers
echo "Running containers:"
docker-compose ps

echo "Application is running!"
echo "Access the API at: http://localhost:8080"
echo "Access Grafana at: http://localhost:3000"
echo "Access Prometheus at: http://localhost:9090"

# Show logs
echo "Showing application logs (Ctrl+C to stop)..."
docker-compose logs -f ecommerce-api 