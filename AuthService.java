package com.example.functionhall.service;

import com.example.functionhall.model.User;
import com.example.functionhall.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public User register(String name, String email, String phone, String password) {
        if (users.findByEmail(email) != null)
            throw new IllegalArgumentException("Email already registered");
        long id = users.create(name, email, phone, encoder.encode(password));
        return new User(id, name, email, phone, "USER");
    }

    public User authenticate(String email, String password) {
        User user = users.findByEmail(email);
        if (user == null) return null;
        String hash = users.passwordHash(email);
        return encoder.matches(password, hash) ? user : null;
    }
}
