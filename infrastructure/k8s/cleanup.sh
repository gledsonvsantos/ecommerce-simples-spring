#!/bin/bash

echo "Cleaning up Kubernetes resources..."

# Delete all resources in the ecommerce namespace
echo "Deleting ecommerce namespace resources..."
kubectl delete -k base/

# Delete all resources in the monitoring namespace
echo "Deleting monitoring namespace resources..."
kubectl delete -k monitoring/

# Delete namespaces
echo "Deleting namespaces..."
kubectl delete namespace ecommerce
kubectl delete namespace monitoring

echo "Cleanup complete!"
echo "To stop Minikube, run: minikube stop"
echo "To delete Minikube cluster, run: minikube delete" 