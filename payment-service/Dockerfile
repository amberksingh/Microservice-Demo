FROM eclipse-temurin:17-jdk
WORKDIR /payment-service
COPY target/payment-service-1.0-SNAPSHOT.jar payment-service.jar
ENTRYPOINT ["java", "-jar", "payment-service.jar"]