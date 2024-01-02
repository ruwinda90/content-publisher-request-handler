FROM openjdk:8-jdk-alpine
ARG CONFIG_FILE=application.yml
RUN addgroup -S spring && adduser -S spring -G spring &&\
    mkdir /app/ &&\
    chown spring /app
USER spring:spring
ADD request-handler-0.0.1-SNAPSHOT.jar /app/app.jar
ADD "${CONFIG_FILE}" /app/application.yml
ENTRYPOINT ["/bin/sh", "-l", "-c", "java -jar /app/app.jar --spring.config.location=/app/application.yml"]