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
                    "Missing user or user id in request body");
        }

        Optional<User> userOpt = userRepository.findById(jobData.getUser().getId());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found with id: " + jobData.getUser().getId());
        }

        jobData.setUser(userOpt.get());

        return jobRepository.save(jobData);
    }

    // Get all Jobs
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    //  Get a Job
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    //  update Job
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setTitle(updatedJob.getTitle());
                    job.setCompany(updatedJob.getCompany());
                    job.setStatus(updatedJob.getStatus());
                    job.setNotes(updatedJob.getNotes());
                    job.setDateApplied(updatedJob.getDateApplied());
                    job.setJobDescription(updatedJob.getJobDescription());
                    job.setReminderDate(updatedJob.getReminderDate());
                    return ResponseEntity.ok(updatedJob);
                })
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    //    update part of the job
    @PatchMapping("/{id}")
    public Job updateJobPartially(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Job job = jobRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found with id: " + id)
        );

        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> job.setTitle((String) value);
                case "company" -> job.setCompany((String) value);
                case "status" -> job.setStatus((String) value);
                case "jobDescription" -> job.setJobDescription((String) value);
                case "notes" -> job.setNotes((String) value);
                case "resumeFile" -> job.setResumeFile((String) value);
                case "dateApplied" -> job.setDateApplied(LocalDate.parse((String) value));
                case "reminderDate" -> job.setReminderDate(LocalDate.parse((String) value));

            }
        });

        return jobRepository.save(job);
    }


    //    Delete a Job
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobRepository.deleteById(id);
    }

}
