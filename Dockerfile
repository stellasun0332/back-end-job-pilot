# Stage 1: Build Stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy all project files
COPY . .

# Build the JAR file
RUN mvn clean package -DskipTests


# Stage 2: Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app


# Copy the built jar to the runtime image
COPY --from=build /app/target/back-end-job-pilot-1.0-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
