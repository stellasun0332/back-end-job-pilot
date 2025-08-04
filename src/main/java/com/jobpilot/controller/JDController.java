package com.jobpilot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobpilot.dto.JDResponse;
import com.jobpilot.model.Job;
import com.jobpilot.repository.JobRepository;
import com.jobpilot.service.GeminiService;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/jd")
public class JDController {

    private static final Logger logger = LoggerFactory.getLogger(JDController.class);

    private final GeminiService geminiService;
    private final JobRepository jobRepository;

    @Autowired
    public JDController(GeminiService geminiService, JobRepository jobRepository) {
        this.geminiService = geminiService;
        this.jobRepository = jobRepository;
    }

    @PostConstruct
    public void init() {
        logger.info("JDController loaded successfully.");
    }

    @PostMapping("/analyze")
    public JDResponse analyze(@RequestBody Map<String, Long> requestBody) {
        Long jobId = requestBody.get("jobId");

        if (jobId == null) {
            logger.error("Missing jobId in request.");
            return new JDResponse(null, null, null, "Missing jobId.");
        }

        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isEmpty()) {
            logger.error("Job not found for ID: {}", jobId);
            return new JDResponse(null, null, null, "Job not found.");
        }

        String jobDescription = optionalJob.get().getJobDescription();
        logger.info("Analyzing job description for job ID {}: {}", jobId, jobDescription);

        String result = geminiService.getAnalysis(jobDescription);
        logger.info("Gemini raw response: {}", result);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);

            List<String> keywords = mapper.convertValue(root.get("keywords"), new TypeReference<>() {});
            List<String> hardSkills = extractListFromJsonNode(root.get("hardSkills"), mapper);
            List<String> softSkills = extractListFromJsonNode(root.get("softSkills"), mapper);
            String summary = root.get("oneSentenceSummary").asText();

            return new JDResponse(keywords, hardSkills, softSkills, summary);

        } catch (Exception e) {
            logger.error("Failed to parse Gemini result: {}", e.getMessage());
            return new JDResponse(null, null, null, "Failed to parse Gemini response.");
        }
    }

    private List<String> extractListFromJsonNode(JsonNode node, ObjectMapper mapper) {
        if (node == null) {
            return Collections.emptyList();
        }

        if (node.isArray()) {
            return mapper.convertValue(node, new TypeReference<List<String>>() {});
        } else {
            return Arrays.stream(node.asText().split(","))
                    .map(String::trim)
                    .toList();
        }
    }
}
