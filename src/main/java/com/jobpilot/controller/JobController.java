package com.jobpilot.controller;

import com.jobpilot.model.Job;
import com.jobpilot.repository.JobRepository;
import com.jobpilot.repository.UserRepository;
import com.jobpilot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a Job
    @PostMapping
    public Job createJob(@RequestBody Job jobData) {
        if (jobData.getUser() == null || jobData.getUser().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Missing user or user ID in request body.");
        }

        Optional<User> userOpt = userRepository.findById(jobData.getUser().getId());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found. No user exists with ID: " + jobData.getUser().getId()
            );
        }

        jobData.setUser(userOpt.get());

        // Set reminder date if not provided
        if (jobData.getReminderDate() == null && jobData.getDateApplied() != null) {
            jobData.setReminderDate(jobData.getDateApplied().plusDays(5));
        }

        return jobRepository.save(jobData);
    }

    // Get all Jobs
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get one Job by ID
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job not found. No job exists with ID: " + id
                ));
    }

    // Partially update Job (PATCH)
    @PatchMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Job not found. No job exists with ID: " + id));

        if (updatedJob.getTitle() != null) job.setTitle(updatedJob.getTitle());
        if (updatedJob.getCompany() != null) job.setCompany(updatedJob.getCompany());
        if (updatedJob.getStatus() != null) job.setStatus(updatedJob.getStatus());
        if (updatedJob.getNotes() != null) job.setNotes(updatedJob.getNotes());
        if (updatedJob.getJobDescription() != null) job.setJobDescription(updatedJob.getJobDescription());
        if (updatedJob.getDateApplied() != null) job.setDateApplied(updatedJob.getDateApplied());
        if (updatedJob.getReminderDate() != null) job.setReminderDate(updatedJob.getReminderDate());

        return jobRepository.save(job);
    }

    // Delete a Job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Job not found. No job exists with ID: " + id
            );
        }

        jobRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Get Jobs that are overdue for reminders
    @GetMapping("/reminders-due")
    public List<Job> getJobsToRemind() {
        LocalDate today = LocalDate.now();
        String status = "Interview Scheduled";  // You can later make this configurable if needed

        return jobRepository.findByReminderDateBeforeAndStatus(today, status);
    }
}
