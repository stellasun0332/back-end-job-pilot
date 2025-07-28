# Job Pilot - Backend API

This is the backend service for **Job Pilot**, a job application tracking system built for the Ada Developers Academy Capstone project. The backend is developed using Java and Spring Boot and provides RESTful APIs to support job tracking, interview management, reminders, and AI-powered job description analysis.

## Team Information

- **Team Members**: Stella Sun (Dragonfly), Brian Martinez (Bumblebee)
- **Project Type**: Capstone - Backend Service Only

## Problem Statement

Job seekers often lose track of where and when they applied, forget to follow up, or fail to tailor their resume for each role. This leads to missed opportunities and inefficiencies. The goal of this backend is to support a full-stack application that helps users stay organized and gain meaningful insights throughout their job search.

## Backend Features

### 1. Job Management
- Create, retrieve, update, and delete job records.
- Fields include: job title, company, application date, current status, job description link, personal notes, resume file, and reminder date.
- Supports partial updates for job entries.

### 2. Interview Tracker
- Track interview stages for each job (e.g., recruiter screen, technical round).
- Add notes, dates, and interviewer names for each stage.

### 3. Reminder System
- Identify jobs needing follow-up based on reminder date and status.
- Retrieve all jobs with overdue reminders.

### 4. CORS Configuration
- Configured to support communication with a separately hosted frontend (e.g., Vue or React app).

## Technology Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

## Project Structure

```text
src/main/java/com/jobpilot
├── controller               # REST controllers (Job, User, Interview)
├── model                    # JPA entity classes
├── repository               # Spring Data JPA repositories
├── CorsConfig.java          # Global CORS configuration
└── JobPilotApplication.java # Main Spring Boot entry point

src/main/resources
└── application.properties   # Spring Boot configuration

```

## Getting Started

### 1. Prerequisites

- Java 17+
- PostgreSQL
- Maven
- Docker (optional)

### 2. Configure Database

Update `src/main/resources/application.properties` with your local DB credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jobpilot_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
````
### 3. Run Locally
```bash
   ./mvnw spring-boot:run
 ````
### 4. Run with Docker
```bash
   docker build -t job-pilot-api .
   docker run -p 8080:8080 job-pilot-api
```   
## Sample API Endpoints

| Method | Endpoint         | Description                       |
|--------|------------------|-----------------------------------|
| GET    | `/jobs`          | Retrieve all jobs                 |
| POST   | `/jobs`          | Create a new job                  |
| PATCH  | `/jobs/{id}`     | Partially update job fields       |
| DELETE | `/jobs/{id}`     | Delete a job by ID                |
| GET    | `/reminders-due` | Get jobs with due reminder dates  |
