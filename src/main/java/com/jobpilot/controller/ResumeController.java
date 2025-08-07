package com.jobpilot.controller;

import com.jobpilot.model.Job;
import com.jobpilot.repository.JobRepository;
import com.jobpilot.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private JobRepository jobRepository;

    // Upload resume, associate with the job, and return download link
    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobId") Long jobId) {
        try {
            // Step 1: Upload to S3
            String filePath = s3Service.uploadFile(file);

            // Step 2: Find job
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            if (optionalJob.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Job not found");
            }

            // Step 3: Save file path to DB
            Job job = optionalJob.get();
            job.setResumeFile(filePath);
            jobRepository.save(job);

            // Step 4: Generate presigned download URL
            String downloadLink = s3Service.generatePresignedUrl(filePath);

            // Step 5: Return download link
            return ResponseEntity.ok(downloadLink);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("❌ Upload failed: " + e.getMessage());
        }
    }
    @GetMapping("/download")
    public ResponseEntity<String> downloadResume(@RequestParam("jobId") Long jobId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);

        if (optionalJob.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Job not found");
        }

        Job job = optionalJob.get();
        String resumeKey = job.getResumeFile();

        if (resumeKey == null || resumeKey.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ No resume file associated with this job.");
        }

        String presignedUrl = s3Service.generatePresignedUrl(resumeKey);

        return ResponseEntity.ok(presignedUrl);
    }

}