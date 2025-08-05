package com.jobpilot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("jobs")
    private User user;

    private String title;
    private String company;

    private LocalDate dateApplied;

    private String status;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobDescription;
    private String notes;
    private String resumeFile;
    private LocalDate reminderDate;

    public Job() {}

//    public Job(User user, String title, String company) {
//        this.user = user;
//        this.title = title;
//        this.company = company;
//    }

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public LocalDate getDateApplied() { return dateApplied; }
    public void setDateApplied(LocalDate appliedOn) { this.dateApplied = appliedOn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getReminderDate() {return reminderDate;}
    public void setReminderDate(LocalDate reminderDate) {this.reminderDate = reminderDate;}

    public String getResumeFile() { return resumeFile; }
    public void setResumeFile(String resumeFile) { this.resumeFile = resumeFile; }
}

