package com.jobpilot.dto.auth;

public class AuthResponse {
    private String token;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public static AuthResponse error(String msg) {
        return new AuthResponse(null, msg);
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
