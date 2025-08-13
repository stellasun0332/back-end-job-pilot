package com.jobpilot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Job job;

    @Column(nullable = false)
    private LocalDate date;

    private String interviewer;
    private String prepNotes;
    private LocalDateTime reminderDatetime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }


    @JsonProperty("jobId")
    public Long getJobId() {
        return job != null ? job.getId() : null;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getInterviewer() { return interviewer; }
    public void setInterviewer(String interviewer) { this.interviewer = interviewer; }

    public String getPrepNotes() { return prepNotes; }
    public void setPrepNotes(String prepNotes) { this.prepNotes = prepNotes; }

    public LocalDateTime getReminderDatetime() { return reminderDatetime; }
    public void setReminderDatetime(LocalDateTime reminderDatetime) { this.reminderDatetime = reminderDatetime; }
}
