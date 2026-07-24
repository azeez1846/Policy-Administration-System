# Multi-Stage Dockerfile for Guidewire PolicyCenter Sandbox

# Stage 1: Build Application Package
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy Maven POM and Source Code
COPY pom.xml .
COPY backend ./backend
COPY src ./src
COPY frontend ./frontend

# Package Runnable JAR (skip tests during container image creation)
RUN mvn clean package -DskipTests

# Stage 2: Runtime Container Environment
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy Built JAR artifact and frontend web assets
COPY --from=builder /app/target/policycenter-sandbox-1.0.0.jar app.jar
COPY --from=builder /app/frontend ./frontend

# Expose HTTP Server Port
EXPOSE 8080

# Run Application Server
ENTRYPOINT ["java", "-jar", "app.jar"]
