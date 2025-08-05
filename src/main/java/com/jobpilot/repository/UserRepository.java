package com.jobpilot.repository;

import com.jobpilot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 不用写任何代码，CRUD 都能自动用！
}
