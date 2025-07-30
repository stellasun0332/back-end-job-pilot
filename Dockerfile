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

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://dpg-d1u01bidbo4c73e2m7h0-a.oregon-postgres.render.com:5432/jobpilot_db_ecym?sslmode=require"
ENV SPRING_DATASOURCE_USERNAME="jobpilot_db_ecym_user"
ENV SPRING_DATASOURCE_PASSWORD="4NzWHRE7A9V346lKYGZ7eZ3J6bkcZBmu"

# Copy the built jar to the runtime image
COPY --from=build /app/target/back-end-job-pilot-1.0-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
