# Build context MUST be the repository root (see docker-compose.yml).
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY eureka-server ./eureka-server
COPY api-gateway ./api-gateway
COPY auth-service ./auth-service
COPY food-donation-service ./food-donation-service
COPY monetary-donation-service ./monetary-donation-service
COPY notification-service ./notification-service
COPY education-service ./education-service
COPY volunteer-service ./volunteer-service
RUN mvn -q -pl volunteer-service -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/volunteer-service/target/volunteer-service-*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
