FROM openjdk:8-jdk-alpine
ARG configs=CONFIG_FILE
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir /app/
RUN chown spring /app
USER spring:spring
ADD request-handler-0.0.1-SNAPSHOT.jar /app/app.jar
ADD "${configs}.yml" /app/application.yml
CMD ["/bin/sh", "-l", "-c", "java -jar /app/app.jar --spring.config.location=/app/application.yml"]