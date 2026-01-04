kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-pvc.yaml
kubectl apply -f postgres-service.yaml
kubectl apply -f products-deployment.yaml
kubectl apply -f products-service.yaml
