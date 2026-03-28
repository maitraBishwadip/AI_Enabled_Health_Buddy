package com.Fitness.AI.Service.controller;

import com.Fitness.AI.Service.model.Recommendation;
import com.Fitness.AI.Service.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")

public class recommendationController {

    private final RecommendationService recommendationService;

@GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsForUser(@PathVariable Long userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }

  @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsForActivity(@PathVariable Long activityId) {
        List<Recommendation> recommendations = recommendationService.getRecommendationsForActivity(activityId);
        return ResponseEntity.ok(recommendations);
    }
}
