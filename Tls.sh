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
EOF

echo "Waiting for secret to be created (CTRL-C to exit)"
PEM=""
while [[ -z $PEM ]]
do
  echo -n "."
  sleep 1
  DOC=`kubectl get secret -n knative-serving knative-serving-certs -o json 2> /dev/null`
  PEM=`echo $DOC | jq -r '.data."ca-cert.pem"'`
done
echo " Secret found!"

echo "Copy the certificate to file"
ROOTCA="$(mktemp)"
FILENAME=`basename $ROOTCA`
echo $PEM | base64 -d >  $ROOTCA

echo "Create a temporary config-deployment configmap with the certificate"
CERT=`kubectl create cm config-deployment --from-file $ROOTCA -o json --dry-run=client |jq .data.\"$FILENAME\"`

echo "cleanup"
rm $ROOTCA

kubectl apply --filename - <<EOF
apiVersion: operator.knative.dev/v1beta1
kind: KnativeServing
metadata:
  name: knative-serving
  namespace: knative-serving
spec:
  deployments:
  - name: guard-service
    volumes:
      - name: guard-service-tls-volume
        secret:
          secretName: knative-serving-certs 
    
    env:
    - container: guard-service
      envVars:
      - name: GUARD_SERVICE_TLS
        value: "true"
      - name: GUARD_SERVICE_AUTH
        value: "true"
  security:
    securityGuard:
      enabled: true
  ingress:
    kourier:
      enabled: true
  config:
    network:
      ingress.class: "kourier.ingress.networking.knative.dev"
    deployment:
      queue-sidecar-rootca: ${CERT}
      queue-sidecar-token-audiences: guard-service
EOF
