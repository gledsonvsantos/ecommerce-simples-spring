#!/bin/bash

# Exit on error
set -e

echo "Starting cleanup of Kubernetes resources..."

# Function to check if a namespace exists
check_namespace() {
    kubectl get namespace $1 >/dev/null 2>&1
    return $?
}

# Function to check if Minikube is running
check_minikube() {
    minikube status >/dev/null 2>&1
    return $?
}

# Check if Minikube is running
if ! check_minikube; then
    echo "Error: Minikube is not running"
    echo "Please start Minikube with: minikube start"
    exit 1
fi

# Stop any running port-forwards
echo "Stopping port-forwards..."
pkill -f "kubectl port-forward" || true

# Delete resources in the ecommerce namespace
if check_namespace ecommerce; then
    echo "Deleting ecommerce namespace resources..."
    kubectl delete -k base/ || true
    echo "Deleting ecommerce namespace..."
    kubectl delete namespace ecommerce --timeout=60s || true
fi

# Delete resources in the monitoring namespace
if check_namespace monitoring; then
    echo "Deleting monitoring namespace resources..."
    kubectl delete -k monitoring/ || true
    echo "Deleting monitoring namespace..."
    kubectl delete namespace monitoring --timeout=60s || true
fi

# Wait for namespaces to be fully deleted
echo "Waiting for namespaces to be deleted..."
while check_namespace ecommerce || check_namespace monitoring; do
    echo "Waiting for namespaces to be deleted..."
    sleep 5
done

echo "Cleanup complete!"
echo
echo "Additional cleanup options:"
echo "1. To stop Minikube: minikube stop"
echo "2. To delete Minikube cluster: minikube delete"
echo "3. To remove all local Docker images: docker system prune -a"
echo
echo "Note: You may need to manually delete persistent volumes if they are not being automatically cleaned up." 