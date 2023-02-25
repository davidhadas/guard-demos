kind delete cluster --name k8s
kind create cluster --config kind-config.yaml 
kubectl cluster-info --context kind-k8s
kubectl create namespace knative-serving
kubectl apply -f https://raw.githubusercontent.com/knative-sandbox/security-guard/release-0.4/config/resources/gateAccount.yaml
kubectl apply -f https://raw.githubusercontent.com/knative-sandbox/security-guard/release-0.4/config/resources/serviceAccount.yaml
kubectl apply -f https://raw.githubusercontent.com/knative-sandbox/security-guard/release-0.4/config/resources/guardiansCrd.yaml
kubectl apply -f guard-service-NoAuth.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
echo "waiting for ingrss to be ready"
sleep  5
# wait for ingress to be ready
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller  --timeout=120s

