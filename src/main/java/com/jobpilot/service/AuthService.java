package com.jobpilot.service;

import com.jobpilot.dto.auth.AuthResponse;
import com.jobpilot.dto.auth.LoginRequest;
import com.jobpilot.dto.auth.SignupRequest;
import com.jobpilot.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /** 注册：校验重复邮箱 -> 保存加密密码 -> 返回 JWT */
    public AuthResponse register(SignupRequest req) {
        if (req.getEmail() == null || req.getPassword() == null || req.getName() == null) {
            throw new IllegalArgumentException("name/email/password is required");
        }
        if (userService.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u = userService.save(u);

        String token = jwtService.generate(u.getId(), u.getEmail());
        return new AuthResponse(token);
    }

    /** 登录：校验用户存在 + 密码匹配 -> 返回 JWT */
    public AuthResponse login(LoginRequest req) {
        User u = userService.getByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        String token = jwtService.generate(u.getId(), u.getEmail());
        return new AuthResponse(token);
    }
}
