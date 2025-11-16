FROM eclipse-temurin:17-jdk
WORKDIR /user-service
COPY target/user-service-1.0-SNAPSHOT.jar user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]