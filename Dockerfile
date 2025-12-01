# ---------------------------- Сборка WAR файла -----------------------------
FROM maven:3.8.6-openjdk-8 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# --------------------- Развертывание на Tomcat -----------------------------
FROM tomcat:9.0-jdk8-openjdk-slim

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/labs-oop.war /usr/local/tomcat/webapps/labs-oop.war
COPY --from=build /root/.m2/repository/org/postgresql/postgresql/42.7.1/postgresql-42.7.1.jar /usr/local/tomcat/lib/

EXPOSE 8080

CMD ["catalina.sh", "run"]