package com.jobpilot.controller;

import com.jobpilot.model.Interview;
import com.jobpilot.model.Job;
import com.jobpilot.repository.InterviewRepository;
import com.jobpilot.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobRepository jobRepository;

    // Create a new interview record for a specific job
    @PostMapping
    public Interview createInterview(@RequestBody Interview interviewData) {

        // ✅ 防止 NPE：要求 body 里必须有 { "job": { "id": <jobId> }, ... }
        if (interviewData.getJob() == null || interviewData.getJob().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Missing job.id in request body. Expected: { \"job\": { \"id\": <jobId> }, ... }"
            );
        }

        Long jobId = interviewData.getJob().getId();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job not found. No job exists with ID: " + jobId
                ));

        interviewData.setJob(job);
        return interviewRepository.save(interviewData);
    }

    // Get all interview records (used for dev/testing)
    @GetMapping
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    // Get a specific interview by ID
    @GetMapping("/{id}")
    public Interview getInterviewById(@PathVariable Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Interview not found. No record exists for interview ID: " + id
                ));
    }

    // Get all interviews associated with a specific job
    @GetMapping("/job/{jobId}")
    public List<Interview> getInterviewsByJobId(@PathVariable Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Job not found. No job exists with ID: " + jobId
            );
        }

        // ✅ 同步修改仓库方法名
        return interviewRepository.findByJob_Id(jobId);
    }

    // Partially update an interview (only non-null fields will be updated)
    @PatchMapping("/{id}")
    public Interview updateInterview(@PathVariable Long id, @RequestBody Interview updatedInterview) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Interview not found. No interview exists with ID: " + id
                ));

        if (updatedInterview.getDate() != null) {
            interview.setDate(updatedInterview.getDate());
        }
        if (updatedInterview.getInterviewer() != null) {
            interview.setInterviewer(updatedInterview.getInterviewer());
        }
        if (updatedInterview.getPrepNotes() != null) {
            interview.setPrepNotes(updatedInterview.getPrepNotes());
        }
        if (updatedInterview.getReminderDatetime() != null) {
            interview.setReminderDatetime(updatedInterview.getReminderDatetime());
        }

        return interviewRepository.save(interview);
    }

    // Delete an interview by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        if (!interviewRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Interview not found. No interview exists with ID: " + id
            );
        }

        interviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
