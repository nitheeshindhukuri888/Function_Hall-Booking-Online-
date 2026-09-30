package com.example.functionhall.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Booking(
        Long id, Long userId, Long hallId, LocalDate eventDate, String eventType,
        int guestCount, String customerName, String customerPhone, String customerEmail,
        String notes, BigDecimal totalAmount, String status, String paymentStatus,
        String paymentId, String gatewayOrderId
) {}
