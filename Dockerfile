FROM maven:3.8.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/gie-backend-1.0-SNAPSHOT.jar app.jar

EXPOSE 4567

ENTRYPOINT ["java","-jar","app.jar"]
