# Build
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Startup
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install wget for healthcheck
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/labs-oop*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]