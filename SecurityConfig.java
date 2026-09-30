package com.example.functionhall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // This project uses an application-level login/session implementation for simplicity.
        // CSRF is disabled because the frontend uses JSON APIs; in a production deployment,
        // enable CSRF protection if using cookie-authenticated browser sessions.
        http
            .csrf(csrf -> csrf.disable())
            .headers(h -> h.frameOptions(f -> f.deny()))
            .authorizeHttpRequests(a -> a.anyRequest().permitAll());
        return http.build();
    }
}
