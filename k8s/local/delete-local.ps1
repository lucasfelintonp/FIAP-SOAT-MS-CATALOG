kubectl delete -f catalog-service.yaml
kubectl delete -f catalog-deployment.yaml
kubectl delete -f postgres-service.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f postgres-pvc.yaml
kubectl delete -f secret.yaml
kubectl delete -f configmap.yaml
