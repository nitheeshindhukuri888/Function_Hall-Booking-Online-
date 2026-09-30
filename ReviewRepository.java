package com.example.functionhall.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewRepository {
    private final JdbcTemplate jdbc;
    public ReviewRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}

    public void add(long userId,long hallId,int rating,String comment){
        jdbc.update("""
          INSERT INTO reviews(user_id,hall_id,rating,comment) VALUES(?,?,?,?)
          ON DUPLICATE KEY UPDATE rating=VALUES(rating), comment=VALUES(comment)
        """,userId,hallId,rating,comment);
    }

    public List<Map<String,Object>> forHall(long hallId){
        return jdbc.queryForList("""
          SELECT r.rating,r.comment,r.created_at,u.name
          FROM reviews r JOIN users u ON u.id=r.user_id
          WHERE r.hall_id=? ORDER BY r.created_at DESC
        """,hallId);
    }
}
