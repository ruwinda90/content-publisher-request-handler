# content-publisher-request-handler
The request handler is the entry-point to the backend system.
Tech stack - Spring Boot, Java8, MySQL, Jenkins, Docker, Kubernetes

## Build the project
```
./mvnw clean package
```

## Deployment
This repository contains a Jenkins pipeline which is capable of deploying the application on a Kubernetes cluster. 
The Jenkins pipeline is currently tested and verified on a Ubuntu 18 VM running inside a Windows 10 laptop.

## TODO
### Jenkins pipeline
- Helm integration
- Web hook integrations (The pipeline is currently triggered manually)
