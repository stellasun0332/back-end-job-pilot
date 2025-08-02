package com.jobpilot.dto;

public class JDRequest {
    private String jobDescription;

    public JDRequest() {}

    public JDRequest(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
