#!/bin/bash

application_name=$1
image_name=$2
image_tag=$3
port=$4
replicas=1
namespace_name="${application_name}-ns"
deploy_name="${application_name}-deploy"
service_name="${application_name}-svc"

TEMP_SCRIPT_DIR="temp-scripts"

# Create namespace
if [ $(microk8s kubectl get ns --no-headers | grep "${namespace_name}" | wc -l) -eq 1 ]; then
  echo "Namespace ${namespace_name} is already created"
else
  microk8s kubectl create ns "${namespace_name}" || exit 1;
  echo "Namespace ${namespace_name} is created"
fi

# Create deployment
if [ -d "${TEMP_SCRIPT_DIR}" ]; then
  rm -rf "${TEMP_SCRIPT_DIR}"
fi
mkdir "${TEMP_SCRIPT_DIR}" && cd "${TEMP_SCRIPT_DIR}"
sed -e "s/\${APPLICATION_NAME}/${application_name}/g" -e "s/\${DEPLOY_NAME}/${deploy_name}/g" -e "s/\${N_REPLICAS}/${replicas}/g" \
 -e "s/\${IMAGE}/${image_name}/g" -e "s/\${IMAGE_TAG}/${image_tag}/g" -e "s/\${APPLICATION_PORT}/${port}/g" ../deployment/scripts/deployment.yml > deployment-processed.yml

if [ $(microk8s kubectl get deploy -n ${namespace_name} --no-headers | grep "${deploy_name}" | wc -l) -eq 1 ]; then
  echo "Deployment ${deploy_name} is already created"
fi
microk8s kubectl apply -n "${namespace_name}" -f deployment-processed.yml || exit 1;
echo "Deployment ${deploy_name} is created"
cd ../

# Create service
if [ $(microk8s kubectl get svc -n ${namespace_name} --no-headers | grep "${service_name}" | wc -l) -eq 1 ]; then
  echo "Service ${service_name} is already created"
else
  microk8s kubectl expose deploy -n "${namespace_name}" "${deploy_name}" --name "${service_name}" --type NodePort --port "${port}" --target-port "${port}" || exit 1;
  node_port=$(kubectl get svc -n "${namespace_name}" "${service_name}" -o jsonPath=-o jsonpath='{.spec.ports[0].nodePort}')
  echo "Service ${service_name} is created. Application is exposed on port ${node_port}"
fi

# Clean up
rm -rf "${TEMP_SCRIPT_DIR}"
