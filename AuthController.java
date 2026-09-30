package com.example.functionhall.controller;

import com.example.functionhall.dto.LoginRequest;
import com.example.functionhall.dto.RegisterRequest;
import com.example.functionhall.model.User;
import com.example.functionhall.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
        try {
            return ResponseEntity.ok(Map.of("message","Registration successful",
                    "user", auth.register(r.name(),r.email(),r.phone(),r.password())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
        User user = auth.authenticate(r.email(), r.password());
        if (user == null) return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
        return ResponseEntity.ok(Map.of("message","Login successful","user",user));
    }
}
