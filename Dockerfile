# Multi-stage Dockerfile for building a Maven-based Java app and running on Temurin 21

# Builder stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only what is needed for dependency resolution first to leverage layer cache
COPY pom.xml mvnw* ./
COPY .mvn .mvn
COPY src ./src

# Build the application (produces target/*.jar)
RUN mvn -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy fat/jar from builder
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
