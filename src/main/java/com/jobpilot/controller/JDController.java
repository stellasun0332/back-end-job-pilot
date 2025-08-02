package com.jobpilot.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.jobpilot.dto.JDRequest;
import com.jobpilot.dto.JDResponse;
import com.jobpilot.service.GeminiService;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jd")
public class JDController {

    private static final Logger logger = LoggerFactory.getLogger(JDController.class);

    private final GeminiService geminiService;

    @Autowired
    public JDController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostConstruct
    public void init() {
        logger.info("JDController loaded successfully.");
    }

    @PostMapping("/analyze")
    public JDResponse analyze(@RequestBody JDRequest request) {
        logger.info("Received JD analysis request: {}", request.getJobDescription());

        String result = geminiService.getAnalysis(request.getJobDescription());
        logger.info("Gemini raw response: {}", result);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);

            // keywords
            List<String> keywords = mapper.convertValue(root.get("keywords"), new TypeReference<List<String>>() {});

            // hardSkills
            JsonNode hardSkillsNode = root.get("hardSkills");
            List<String> hardSkills = hardSkillsNode.isArray()
                    ? mapper.convertValue(hardSkillsNode, new TypeReference<List<String>>() {})
                    : Arrays.stream(hardSkillsNode.asText().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // softSkills
            JsonNode softSkillsNode = root.get("softSkills");
            List<String> softSkills = softSkillsNode.isArray()
                    ? mapper.convertValue(softSkillsNode, new TypeReference<List<String>>() {})
                    : Arrays.stream(softSkillsNode.asText().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // one sentence summary
            String summary = root.get("oneSentenceSummary").asText();

            return new JDResponse(keywords, hardSkills, softSkills, summary);

        } catch (Exception e) {
            logger.error("Failed to parse Gemini result: {}", e.getMessage());
            return new JDResponse(null, null, null, "Failed to parse response");
        }
    }
}
