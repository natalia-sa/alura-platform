FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.2_13-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar /app/alura_platform.jar

EXPOSE 8080

CMD ["java", "-jar", "alura_platform.jar"]