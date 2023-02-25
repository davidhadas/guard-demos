kubectl delete -f malicious-rmi-server.yaml

docker build -t="malicious-rmi-server" .
kind load docker-image malicious-rmi-server --name k8s

kubectl apply -f malicious-rmi-server.yaml


