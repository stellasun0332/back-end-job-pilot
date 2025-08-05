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

    // 上传简历并绑定到 Job
    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobId") Long jobId) {
        try {
            String filePath = s3Service.uploadFile(file);
            Optional<Job> optionalJob = jobRepository.findById(jobId);

            if (optionalJob.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Job not found");
            }

            Job job = optionalJob.get();
            job.setResumeFile(filePath); // 保存文件路径到 job 实体中
            jobRepository.save(job);

            return ResponseEntity.ok("Resume uploaded and linked to job. Path: " + filePath);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("❌ Upload failed: " + e.getMessage());
        }
    }

    // 根据 jobId 获取预签名下载链接
    @GetMapping("/download-link")
    public ResponseEntity<String> getResumeDownloadLink(@RequestParam("jobId") Long jobId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);

        if (optionalJob.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Job not found");
        }

        Job job = optionalJob.get();
        String fileName = job.getResumeFile();

        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ No resume uploaded for this job");
        }

        String downloadLink = s3Service.generatePresignedUrl(fileName);
        return ResponseEntity.ok(downloadLink);
    }
}
