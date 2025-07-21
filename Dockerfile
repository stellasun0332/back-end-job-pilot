# ========================
# Stage 1: Build Stage
# ========================
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy all project files into the container
COPY . .

# Build the JAR file (skip tests to speed up the build)
RUN mvn clean package -DskipTests=true

# ========================
# Stage 2: Runtime Stage
# ========================
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
