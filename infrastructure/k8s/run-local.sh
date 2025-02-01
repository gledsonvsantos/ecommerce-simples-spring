#!/bin/bash

# Check if minikube is running
if ! minikube status > /dev/null 2>&1; then
    echo "Starting Minikube..."
    minikube start
fi

# Configure Docker environment to use Minikube
echo "Configuring Docker environment..."
eval $(minikube docker-env)

# Build the application
echo "Building the application..."
cd ../../app && ./mvnw clean package -DskipTests

# Build Docker image
echo "Building Docker image..."
docker build -t ecommerce-api:latest .

# Create namespaces if they don't exist
echo "Creating namespaces..."
kubectl create namespace ecommerce --dry-run=client -o yaml | kubectl apply -f -
kubectl create namespace monitoring --dry-run=client -o yaml | kubectl apply -f -

# Apply Kubernetes configurations
echo "Applying Kubernetes configurations..."
cd ../infrastructure/k8s
kubectl apply -k base/
kubectl apply -k monitoring/

# Wait for pods to be ready
echo "Waiting for pods to be ready..."
kubectl wait --for=condition=ready pod -l app=ecommerce-api -n ecommerce --timeout=120s
kubectl wait --for=condition=ready pod -l app=prometheus -n monitoring --timeout=120s
kubectl wait --for=condition=ready pod -l app=grafana -n monitoring --timeout=120s

# Set up port forwarding
echo "Setting up port forwarding..."
kubectl port-forward -n ecommerce svc/ecommerce-api 8080:8080 &
kubectl port-forward -n monitoring svc/prometheus 9090:9090 &
kubectl port-forward -n monitoring svc/grafana 3000:3000 &

echo "Application is running!"
echo "Access the API at: http://localhost:8080"
echo "Access Grafana at: http://localhost:3000"
echo "Access Prometheus at: http://localhost:9090"

# Show pods status
echo "Pods status:"
kubectl get pods -n ecommerce
kubectl get pods -n monitoring

# Show logs
echo "Showing application logs (Ctrl+C to stop)..."
kubectl logs -n ecommerce -l app=ecommerce-api -f 