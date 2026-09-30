package com.example.functionhall.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BookingRequest(
        @NotNull Long hallId,
        @NotNull @FutureOrPresent LocalDate eventDate,
        @NotBlank @Size(max=100) String eventType,
        @Min(1) int guestCount,
        @NotBlank @Size(min=2, max=100) String customerName,
        @NotBlank @Pattern(regexp="^[0-9]{10}$", message="Phone must contain 10 digits") String customerPhone,
        @NotBlank @Email String customerEmail,
        @Size(max=1000) String notes
) {}
