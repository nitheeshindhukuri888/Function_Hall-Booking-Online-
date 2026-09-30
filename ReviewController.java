package com.example.functionhall.controller;

import com.example.functionhall.repository.ReviewRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository reviews;
    public ReviewController(ReviewRepository reviews){this.reviews=reviews;}

    @GetMapping("/hall/{hallId}")
    public List<Map<String,Object>> list(@PathVariable long hallId){return reviews.forHall(hallId);}

    @PostMapping
    public Map<String,String> add(@RequestBody ReviewRequest r){
        reviews.add(r.userId(),r.hallId(),r.rating(),r.comment());
        return Map.of("message","Review saved");
    }

    public record ReviewRequest(long userId,long hallId,@Min(1) @Max(5) int rating,
                                @Size(max=1000) String comment){}
}
