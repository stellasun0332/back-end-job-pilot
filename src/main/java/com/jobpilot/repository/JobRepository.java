package com.jobpilot.repository;

import com.jobpilot.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // 这里我们暂时不需要写任何代码，Spring 会自动实现常见的数据库操作
}
