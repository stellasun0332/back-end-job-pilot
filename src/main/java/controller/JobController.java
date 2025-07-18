package com.jobpilot.controller;

import com.jobpilot.model.Job;
import com.jobpilot.repository.JobRepository;
import com.jobpilot.repository.UserRepository;
import com.jobpilot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        // 检查 user 是否存在
        Optional<User> userOpt = userRepository.findById(jobData.getUser().getId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with id: " + jobData.getUser().getId());
        }

        // 设置用户对象（确保是托管的 Entity）
        jobData.setUser(userOpt.get());

        return jobRepository.save(jobData);
    }

    // Get all Jobs
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get a Job
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    // update Job
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(updatedJob.getTitle());
        job.setCompany(updatedJob.getCompany());
        job.setStatus(updatedJob.getStatus());
        // 其他字段也可以继续 set...

        return jobRepository.save(job);
    }

//    Delete a Job
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobRepository.deleteById(id);
    }

}
