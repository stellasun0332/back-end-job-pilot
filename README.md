# Job Pilot - Backend API

This is the backend service for **Job Pilot**, a job application tracking system built for the Ada Developers Academy Capstone project.  
It is developed using **Java** and **Spring Boot** and provides RESTful APIs to support job tracking, interview management, reminders, secure resume uploads, and AI-powered job description analysis.

## Team Information
- **Team Members**: Stella Sun (Dragonfly), Brian Martinez (Bumblebee)
- **Project Type**: Capstone - Backend Service

## Problem Statement
Job seekers often lose track of where and when they applied, forget to follow up, or fail to tailor their resume for each role.  
This backend supports a full-stack application that helps users stay organized, receive timely reminders, and gain meaningful insights using AI.

## Backend Features

### 1. Job Management
- Create, retrieve, update, and delete job records.
- Fields: job title, company, application date, status, job description link, personal notes, resume file, reminder date.
- Supports partial updates (PATCH).

### 2. Interview Tracker
- Track interview stages for each job (e.g., recruiter screen, technical round).
- Add notes, dates, and interviewer names.

### 3. Reminder System
- Identify jobs needing follow-up based on reminder date and status.
- Retrieve all jobs with overdue reminders.

### 4. Resume Uploads (AWS S3)
- Securely upload and store resumes using pre-signed S3 URLs.
- Download resumes with temporary access links.

### 5. AI Integration (Gemini AI)
- Extract keywords and requirements from job descriptions.
- Help users quickly understand what a posting is asking for.

### 6. Security & CORS Configuration
- Configured for secure communication with a separately hosted frontend (Vue/React).
- Implements CORS rules and basic security configuration.

## Technology Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- AWS S3
- Google Gemini AI API

## Project Structure
```text
src/main/java/com/jobpilot
├── controller               # REST controllers (Job, Interview, Resume, AI)
├── dto                      # Data Transfer Objects
├── model                    # JPA entity classes
├── repository               # Spring Data JPA repositories
├── service                  # Business logic and integrations
├── SecurityConfig.java      # Security and CORS configuration
└── JobPilotApplication.java # Main Spring Boot entry point

src/main/resources
└── application.properties   # Spring Boot configuration
