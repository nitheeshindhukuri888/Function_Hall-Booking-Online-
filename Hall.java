package com.example.functionhall.model;

import java.math.BigDecimal;

public record Hall(
        Long id,
        String name,
        String city,
        String address,
        int capacity,
        BigDecimal pricePerDay,
        String description,
        String imageUrl,
        String amenities,
        boolean active
) {}
