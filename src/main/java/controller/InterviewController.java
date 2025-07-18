package com.jobpilot.controller;

import com.jobpilot.model.Interview;
import com.jobpilot.model.Job;
import com.jobpilot.repository.InterviewRepository;
import com.jobpilot.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobRepository jobRepository;

    // Create
    @PostMapping
    public Interview createInterview(@RequestBody Interview interviewData) {
        Job job = jobRepository.findById(interviewData.getJob().getId())
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + interviewData.getJob().getId()));
        interviewData.setJob(job);
        return interviewRepository.save(interviewData);
    }

    // Read all
    @GetMapping
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    // Read one
    @GetMapping("/{id}")
    public Interview getInterviewById(@PathVariable Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
    }

    // Update
    @PutMapping("/{id}")
    public Interview updateInterview(@PathVariable Long id, @RequestBody Interview updatedInterview) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        interview.setDate(updatedInterview.getDate());
        interview.setInterviewer(updatedInterview.getInterviewer());
        interview.setPrepNotes(updatedInterview.getPrepNotes());
        interview.setReminderDatetime(updatedInterview.getReminderDatetime());

        return interviewRepository.save(interview);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteInterview(@PathVariable Long id) {
        interviewRepository.deleteById(id);
    }
}
