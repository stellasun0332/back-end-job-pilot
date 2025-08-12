package com.jobpilot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    /** ✨ 关键：声明与 Interview 的一对多，删除 Job 时级联删除 */
    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.REMOVE,   // 只在删除时级联，避免影响你现有的保存逻辑
            orphanRemoval = true
    )
    @JsonIgnoreProperties("job")        // 防止序列化递归
    private List<Interview> interviews = new ArrayList<>();

    public Job() {}

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

    public LocalDate getReminderDate() { return reminderDate; }
    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }

    public String getResumeFile() { return resumeFile; }
    public void setResumeFile(String resumeFile) { this.resumeFile = resumeFile; }

    public List<Interview> getInterviews() { return interviews; }
    public void setInterviews(List<Interview> interviews) { this.interviews = interviews; }
}
