kubectl apply -f guardian.yaml 

kubectl delete -f guardTls-vulnerable-app.yaml
kubectl delete -f vulnerable-app.yaml

docker build -t="vulnerable-app" .
kind load docker-image vulnerable-app --name k8s

kubectl apply -f guardTLS-vulnerable-app.yaml
kubectl apply -f vulnerable-app.yaml

