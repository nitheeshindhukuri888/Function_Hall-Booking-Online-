package com.example.functionhall.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {
    private final JdbcTemplate jdbc;
    public HealthController(JdbcTemplate jdbc){this.jdbc=jdbc;}

    @GetMapping("/api/health")
    public Map<String,Object> health(){
        Integer count=jdbc.queryForObject("SELECT COUNT(*) FROM function_halls",Integer.class);
        return Map.of("status","UP","hallCount",count==null?0:count);
    }
}
