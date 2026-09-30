package com.example.functionhall.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbc;

    public DataSeeder(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        insert("Sri Lakshmi Convention Hall", "Tirupati",
                "Tiruchanoor Road, Tirupati", 800, 65000,
                "Spacious convention hall suitable for weddings, receptions and large family events.",
                "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80",
                "Parking, AC, Stage, Dining Hall, Generator, Bridal Room");

        insert("Royal Grand Function Hall", "Hyderabad",
                "Madhapur, Hyderabad", 500, 45000,
                "Modern air-conditioned function space for weddings, birthdays and corporate events.",
                "https://images.unsplash.com/photo-1507504031003-b417219a0fde?auto=format&fit=crop&w=1200&q=80",
                "AC, Parking, Stage, Catering Area, Projector");

        insert("Green Garden Banquet", "Vijayawada",
                "Benz Circle, Vijayawada", 300, 30000,
                "Garden-style venue with indoor and outdoor spaces for intimate celebrations.",
                "https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80",
                "Garden, Parking, Dining, Stage, Lighting");

        insert("Nellore Grand Convention Hall", "Nellore",
                "Magunta Layout, Nellore", 700, 55000,
                "Large air-conditioned convention venue for weddings, receptions and family celebrations.",
                "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80",
                "AC, Parking, Stage, Dining Hall, Generator, Bridal Room");

        insert("Sri Venkateswara Function Hall", "Nellore",
                "Dargamitta, Nellore", 350, 28000,
                "Comfortable venue for engagements, birthdays, receptions and medium-sized events.",
                "https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80",
                "Parking, Dining, Stage, Lighting");

        insert("Royal Pearl Banquet", "Nellore",
                "Kondayapalem, Nellore", 1000, 80000,
                "Premium banquet hall for large weddings and grand celebrations.",
                "https://images.unsplash.com/photo-1507504031003-b417219a0fde?auto=format&fit=crop&w=1200&q=80",
                "AC, Valet Parking, Stage, Dining, Generator, Bridal Suite");
    }

    private void insert(String name, String city, String address, int capacity,
                        double price, String description, String image, String amenities) {
        Integer exists = jdbc.queryForObject(
                "SELECT COUNT(*) FROM function_halls WHERE name=?", Integer.class, name);
        if (exists != null && exists > 0) return;

        jdbc.update("""
            INSERT INTO function_halls
            (name, city, address, capacity, price_per_day, description, image_url, amenities, active)
            VALUES (?,?,?,?,?,?,?,?,true)
        """, name, city, address, capacity, price, description, image, amenities);
    }
}
