package com.example.functionhall.repository;

import com.example.functionhall.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public User findByEmail(String email) {
        List<User> list = jdbc.query("""
            SELECT id,name,email,phone,role FROM users WHERE email=?
        """, (rs, row) -> new User(
                rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("role")), email);
        return list.isEmpty() ? null : list.get(0);
    }

    public String passwordHash(String email) {
        List<String> list = jdbc.query("SELECT password_hash FROM users WHERE email=?",
                (rs, row) -> rs.getString(1), email);
        return list.isEmpty() ? null : list.get(0);
    }

    public long create(String name, String email, String phone, String hash) {
        jdbc.update("INSERT INTO users(name,email,phone,password_hash) VALUES(?,?,?,?)",
                name, email, phone, hash);
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }
}
