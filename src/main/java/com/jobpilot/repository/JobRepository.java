package com.jobpilot.repository;

import com.jobpilot.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByReminderDateBeforeAndStatus(LocalDate date, String status);

}
