package com.jobpilot.service;

import com.jobpilot.model.User;
import com.jobpilot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Optional<User> getByEmail(String email) {
        return repo.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public User save(User u) {
        return repo.save(u);
    }
}
