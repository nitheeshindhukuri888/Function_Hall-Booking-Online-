package com.example.functionhall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min=2, max=100) String name,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp="^[0-9]{10}$", message="Phone must contain 10 digits") String phone,
        @NotBlank @Size(min=6, max=100) String password
) {}
