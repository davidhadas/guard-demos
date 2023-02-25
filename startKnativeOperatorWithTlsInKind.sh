# Create Kind
kind delete cluster --name k8s
kind create cluster --config ./kind-config.yaml
kubectl cluster-info --context kind-k8s
kubectl create namespace knative-serving
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

#Install Knative Operator
kubectl apply -f https://github.com/knative/operator/releases/download/knative-v1.9.1/operator.yaml

kubectl apply --filename - <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: knative-serving
---
apiVersion: operator.knative.dev/v1beta1
kind: KnativeServing
metadata:
  name: knative-serving
  namespace: knative-serving
spec:
  security:
    securityGuard:
      enabled: true
  ingress:
    kourier:
      enabled: true
  config:
    network:
      ingress.class: "kourier.ingress.networking.knative.dev"
EOF

kubectl apply -f https://raw.githubusercontent.com/knative-sandbox/security-guard/release-0.4/config/resources/gateAccount.yaml

