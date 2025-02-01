# E-commerce API

A simple e-commerce API built with Spring Boot, following Clean Architecture and DDD principles.

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- PostgreSQL
- Flyway for database migrations
- MapStruct for object mapping
- OpenAPI/Swagger for API documentation
- Spring Boot Actuator for monitoring
- Docker and Docker Compose for development environment
- Terraform for Infrastructure as Code
- Kubernetes (EKS) for container orchestration

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven
- AWS CLI configured
- Terraform
- kubectl
- Minikube (for local development)

## Local Development

1. Clone the repository:
```bash
git clone <repository-url>
cd ecommerce
```

2. Start the PostgreSQL database using Docker Compose:
```bash
docker-compose up -d
```

3. Build and run the application:
```bash
./mvnw spring-boot:run
```

## Infrastructure Setup

### Local Kubernetes (Minikube)

1. Start Minikube:
```bash
minikube start
```

2. Build and load the Docker image:
```bash
eval $(minikube docker-env)
docker build -t ecommerce-api:latest .
```

3. Deploy to Minikube:
```bash
kubectl apply -k infrastructure/k8s/base
```

### AWS Infrastructure

1. Create an S3 bucket for Terraform state:
```bash
aws s3 mb s3://ecommerce-terraform-state
```

2. Initialize Terraform:
```bash
cd infrastructure/terraform/environments/prod
terraform init
```

3. Plan and apply the infrastructure:
```bash
terraform plan
terraform apply
```

4. Configure kubectl for EKS:
```bash
aws eks update-kubeconfig --name prod-ecommerce --region us-east-1
```

5. Deploy the application:
```bash
kubectl apply -k infrastructure/k8s/base
```

## CI/CD Pipeline

The project includes a GitHub Actions workflow that:
1. Builds and tests the application
2. Builds a Docker image
3. Pushes the image to Amazon ECR
4. Deploys to EKS

Required GitHub Secrets:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

## Infrastructure Components

### AWS Resources (managed by Terraform)
- VPC with public and private subnets
- EKS cluster
- Node group with auto-scaling
- Security groups
- IAM roles and policies

### Kubernetes Resources
- Namespace
- Deployment with 3 replicas
- LoadBalancer Service
- Secrets for database credentials

## Testing the API

You can test the API using curl commands. Here are examples for each endpoint:

### 1. Create a Product (POST)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "description": "Um smartphone incrível",
    "price": 999.99,
    "stockQuantity": 50,
    "sku": "PHONE-XYZ-001"
  }'
```

### 2. List All Products (GET)
```bash
curl http://localhost:8080/api/v1/products
```

### 3. Get Product by ID (GET)
```bash
curl http://localhost:8080/api/v1/products/{id}
```
Replace `{id}` with the actual product ID.

### 4. Update Product (PUT)
```bash
curl -X PUT http://localhost:8080/api/v1/products/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ Pro",
    "description": "Um smartphone ainda mais incrível",
    "price": 1299.99,
    "stockQuantity": 30,
    "sku": "PHONE-XYZ-001"
  }'
```
Replace `{id}` with the actual product ID.

### 5. Delete Product (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/v1/products/{id}
```
Replace `{id}` with the actual product ID.

## Monitoring and Observability

- Kubernetes Dashboard: `minikube dashboard` (local) or EKS Console (AWS)
- Application metrics: `/actuator/metrics`
- Health check: `/actuator/health`
- API Documentation: `/swagger-ui.html`

## Monitoring with Grafana

### Metrics Dashboard

The project includes a custom Grafana dashboard for application monitoring. The dashboard is located at `infrastructure/k8s/monitoring/grafana/dashboards/ecommerce-metrics.json`.

#### Available Metrics

The dashboard includes the following panels:

1. **Request Rate per Minute (by Endpoint)**
   - Time series visualization
   - Shows the number of requests per minute for each endpoint
   - Includes calculations for maximum, sum, and last value

2. **Total Requests by Endpoint**
   - Bar gauge visualization
   - Displays the accumulated total of requests per endpoint

3. **Request Latency**
   - Heatmap visualization
   - Shows the distribution of request latency
   - Helps identify performance patterns

4. **Error Rate (%)**
   - Time series visualization
   - Shows the percentage of errors (status 500) relative to total requests
   - Useful for application health monitoring

5. **Active Database Connections**
   - Stat visualization
   - Monitors the number of active database connections
   - Important for resource management

#### How to Import the Dashboard

1. Access Grafana (http://localhost:3000)
2. Navigate to Dashboards > Import
3. Click on "Upload JSON file"
4. Select the file `infrastructure/k8s/monitoring/grafana/dashboards/ecommerce-metrics.json`
5. Configure the Prometheus datasource
6. Click on "Import"

#### Dashboard Settings

- Auto-refresh: 5 seconds
- Default time range: last 6 hours
- Available variables: application="ecommerce"

#### Prometheus Queries Used

```promql
# Request Rate
sum(rate(http_server_requests_seconds_count{application="ecommerce"}[1m])) by (uri)

# Total Requests
sum(http_server_requests_seconds_count{application="ecommerce"}) by (uri)

# Latency
rate(http_server_requests_seconds_sum{application="ecommerce"}[1m]) 
/ 
rate(http_server_requests_seconds_count{application="ecommerce"}[1m])

# Error Rate
sum(rate(http_server_requests_seconds_count{application="ecommerce",status="500"}[1m])) 
/ 
sum(rate(http_server_requests_seconds_count{application="ecommerce"}[1m])) * 100

# Database Connections
hikaricp_connections_active{application="ecommerce"}
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Project Structure

The project is organized into two main directories:

### App
Contains the Spring Boot application:
- **src/**: Application source code with Clean Architecture layers
  - Domain: Core business logic and domain models
  - Application: Use cases and business rules
  - Infrastructure: External concerns like database and external services
  - Presentation: Controllers, DTOs, and API endpoints
- **Dockerfile**: Container definition for the application
- **docker-compose.yml**: Local development environment setup
- **pom.xml**: Maven dependencies and build configuration
- **load-test.sh**: Load testing script for the application endpoints

### Infrastructure
Contains all infrastructure-related configurations:
- **k8s/**: Kubernetes manifests
  - base/: Base Kubernetes configurations
  - monitoring/: Prometheus and Grafana setup
- **terraform/**: Infrastructure as Code for cloud deployment

## Available Endpoints

### Products API

- `POST /api/v1/products` - Create a new product
- `GET /api/v1/products` - List all products
- `GET /api/v1/products/{id}` - Get a product by ID
- `PUT /api/v1/products/{id}` - Update a product
- `DELETE /api/v1/products/{id}` - Delete a product

## Development

The project uses:
- Flyway for database migrations
- MapStruct for object mapping
- Bean Validation for input validation
- Global exception handling
- Structured logging with SLF4J and Logback

## Expected Responses

### Success Responses
- **Create Product**: Returns 201 Created with the created product
- **Get Product**: Returns 200 OK with the product details
- **List Products**: Returns 200 OK with an array of products
- **Update Product**: Returns 200 OK with the updated product
- **Delete Product**: Returns 204 No Content

### Error Responses
```json
{
    "message": "Error message",
    "status": 400,
    "error": "Bad Request",
    "details": ["Additional error details"],
    "timestamp": "2024-01-31 18:38:48"
}
```

## Running Locally

The application can be run locally in two ways: using Docker Compose or Kubernetes (Minikube).

### Option 1: Docker Compose

This is the simplest way to run the application locally. It will start all required services (application, database, Prometheus, and Grafana) using Docker Compose.

Prerequisites:
- Docker Desktop installed and running
- Java 17 or higher installed

To run:
```bash
./infrastructure/docker/run-local.sh
```

This will:
1. Build the application
2. Start all containers
3. Set up monitoring
4. Show application logs

### Option 2: Kubernetes (Minikube)

This option runs the application in a local Kubernetes cluster using Minikube.

Prerequisites:
- Docker Desktop installed and running
- Minikube installed
- kubectl installed
- Java 17 or higher installed

To run:
```bash
./infrastructure/k8s/run-local.sh
```

This will:
1. Start Minikube if not running
2. Build the application
3. Create necessary namespaces
4. Deploy all components to Kubernetes
5. Set up port forwarding
6. Show application logs

### Accessing the Application

Both options will expose the following endpoints:

- API: http://localhost:8080
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090

### Stopping the Application

For Docker Compose:
```bash
cd infrastructure/docker && docker-compose down
```

For Kubernetes:
```bash
# Stop port forwarding (Ctrl+C)
# Then run the cleanup script:
./infrastructure/k8s/cleanup.sh
```

The cleanup script will:
1. Delete all resources in the ecommerce namespace
2. Delete all resources in the monitoring namespace
3. Delete both namespaces
4. Provide instructions for stopping or deleting the Minikube cluster