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

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Getting Started

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

The application will be available at `http://localhost:8080`

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

## API Documentation

Once the application is running, you can access:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI documentation: `http://localhost:8080/api-docs`

## Monitoring

Health and metrics endpoints are available through Spring Boot Actuator:
- Health check: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## Project Structure

The project follows Clean Architecture principles with the following layers:

- **Domain**: Core business logic and domain models
- **Application**: Use cases and business rules
- **Infrastructure**: External concerns like database and external services
- **Presentation**: Controllers, DTOs, and API endpoints

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