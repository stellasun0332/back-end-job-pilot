package com.jobpilot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAnalysis(String jdText) {
        RestTemplate restTemplate = new RestTemplate();


        String GEMINI_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-pro:generateContent?key=" + apiKey;


        String prompt = """
Analyze the following job description and extract the following information in a structured JSON format. Be specific and avoid generic terms.

Instructions:
1. "keywords": Extract 5–8 high-impact keywords or phrases that best represent the core technologies and responsibilities.
2. "hardSkills": List only the essential technical proficiencies required for the role, separated by commas.
3. "softSkills": Include only those that are strongly emphasized or directly mentioned, separated by commas.
4. "oneSentenceSummary": Write a sharp, clear sentence describing what value this role brings to the company in under 20 words.

Return your response strictly as a raw JSON object. Do NOT return a string. Do NOT add any explanation or formatting.

Job Description:
""" + jdText;

        // Request body format for Gemini API
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_URL, requestEntity, Map.class);
            Map responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("candidates")) {
                return "{\"error\": \"Empty response or missing candidates\"}";
            }

            // Extract raw text output from Gemini
            Map candidate = ((List<Map>) responseBody.get("candidates")).get(0);
            Map content = (Map) candidate.get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            String text = (String) parts.get(0).get("text");

            // Remove extra explanation if any before the first {
            int firstBraceIndex = text.indexOf("{");
            if (firstBraceIndex >= 0) {
                text = text.substring(firstBraceIndex);
            }

            // If the response is a JSON string (double escaped), unescape it first
            if (text.trim().startsWith("\"{")) {
                text = objectMapper.readTree(text).asText(); // Convert stringified JSON to plain JSON string
            }

            // Convert valid JSON string to JsonNode and return as clean JSON string
            JsonNode jsonNode = objectMapper.readTree(text);
            return jsonNode.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to get or parse response from Gemini\"}";
        }
    }
}
