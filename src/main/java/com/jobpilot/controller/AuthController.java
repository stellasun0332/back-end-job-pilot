package com.jobpilot.controller;

import com.jobpilot.dto.auth.AuthResponse;
import com.jobpilot.dto.auth.LoginRequest;
import com.jobpilot.dto.auth.SignupRequest;
import com.jobpilot.model.User;
import com.jobpilot.service.AuthService;
import com.jobpilot.service.JwtService;
import com.jobpilot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    /** 注册 */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody SignupRequest req) {
        try {
            return ResponseEntity.ok(authService.register(req));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(AuthResponse.error(ex.getMessage()));
        }
    }

    /** 登录 */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.login(req));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(AuthResponse.error(ex.getMessage()));
        }
    }

    /** 解析 token 返回当前用户 */
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authorization.substring("Bearer ".length()).trim();
            Long userId = jwtService.parseUserId(token);
            if (userId == null) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            User u = userService.getById(userId);
            if (u == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            return ResponseEntity.ok(u);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("ERROR: " + ex.getMessage());
        }
    }
}
