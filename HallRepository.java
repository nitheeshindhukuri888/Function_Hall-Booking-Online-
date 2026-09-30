package com.example.functionhall.repository;

import com.example.functionhall.model.Hall;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HallRepository {
    private final JdbcTemplate jdbc;

    public HallRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final org.springframework.jdbc.core.RowMapper<Hall> mapper = (rs, row) ->
            new Hall(
                    rs.getLong("id"), rs.getString("name"), rs.getString("city"),
                    rs.getString("address"), rs.getInt("capacity"),
                    rs.getBigDecimal("price_per_day"), rs.getString("description"),
                    rs.getString("image_url"), rs.getString("amenities"),
                    rs.getBoolean("active")
            );

    public List<Hall> findAll(String city) {
        if (city == null || city.isBlank()) {
            return jdbc.query("SELECT * FROM function_halls WHERE active=true ORDER BY id DESC", mapper);
        }
        return jdbc.query(
                "SELECT * FROM function_halls WHERE active=true AND city LIKE ? ORDER BY id DESC",
                mapper, "%" + city.trim() + "%"
        );
    }

    public Hall findById(long id) {
        List<Hall> list = jdbc.query("SELECT * FROM function_halls WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public long create(Hall h) {
        jdbc.update("""
            INSERT INTO function_halls
            (name,city,address,capacity,price_per_day,description,image_url,amenities,active)
            VALUES (?,?,?,?,?,?,?,?,?)
        """, h.name(), h.city(), h.address(), h.capacity(), h.pricePerDay(),
                h.description(), h.imageUrl(), h.amenities(), h.active());
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public void delete(long id) {
        jdbc.update("UPDATE function_halls SET active=false WHERE id=?", id);
    }
}
