package com.jobpilot.dto;

import java.util.List;

public class JDResponse {
    private List<String> keywords;
    private List<String> hardSkills;
    private List<String> softSkills;
    private String oneSentenceSummary;

    public JDResponse() {}

    public JDResponse(List<String> keywords, List<String> hardSkills, List<String> softSkills, String oneSentenceSummary) {
        this.keywords = keywords;
        this.hardSkills = hardSkills;
        this.softSkills = softSkills;
        this.oneSentenceSummary = oneSentenceSummary;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getHardSkills() {
        return hardSkills;
    }

    public void setHardSkills(List<String> hardSkills) {
        this.hardSkills = hardSkills;
    }

    public List<String> getSoftSkills() {
        return softSkills;
    }

    public void setSoftSkills(List<String> softSkills) {
        this.softSkills = softSkills;
    }

    public String getOneSentenceSummary() {
        return oneSentenceSummary;
    }

    public void setOneSentenceSummary(String oneSentenceSummary) {
        this.oneSentenceSummary = oneSentenceSummary;
    }
}
