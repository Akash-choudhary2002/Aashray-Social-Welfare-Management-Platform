# Build context MUST be the repository root (see docker-compose.yml),
# because this is a Maven multi-module reactor project.
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
COPY chatbot-service ./chatbot-service
RUN mvn -q -pl eureka-server -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/eureka-server/target/eureka-server-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
