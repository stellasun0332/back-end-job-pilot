package com.jobpilot.repository;

import com.jobpilot.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByJob_Id(Long jobId);
}
