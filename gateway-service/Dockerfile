FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gateway-service.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]
