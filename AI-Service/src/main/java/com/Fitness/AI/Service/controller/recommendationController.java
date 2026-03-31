package com.Fitness.AI.Service.controller;

import com.Fitness.AI.Service.model.Recommendation;
import com.Fitness.AI.Service.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Recommendation endpoints
 * Provides API for retrieving AI-generated fitness recommendations
 * 
 * Base URL: /api/recommendations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
@Slf4j
public class recommendationController {

    private final RecommendationService recommendationService;

    /**
     * Get all recommendations for a specific user
     * 
     * @param userId The user ID
     * @return List of recommendations for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRecommendationsForUser(@PathVariable Long userId) {
        log.info("Fetching recommendations for User ID: {}", userId);
        
        try {
            List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
            
            if (recommendations.isEmpty()) {
                log.warn("No recommendations found for User ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "NO_CONTENT",
                                "message", "No recommendations found for this user",
                                "userId", userId
                        ));
            }
            
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Recommendations retrieved successfully",
                    "count", recommendations.size(),
                    "data", recommendations
            ));
            
        } catch (Exception e) {
            log.error("Error fetching recommendations for User ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Failed to retrieve recommendations",
                            "error", e.getMessage()
                    ));
        }
    }

    /**
     * Get all recommendations for a specific activity
     * 
     * @param activityId The activity ID
     * @return List of recommendations for the activity
     */
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<?> getRecommendationsForActivity(@PathVariable Long activityId) {
        log.info("Fetching recommendations for Activity ID: {}", activityId);
        
        try {
            List<Recommendation> recommendations = recommendationService.getRecommendationsForActivity(activityId);
            
            if (recommendations.isEmpty()) {
                log.warn("No recommendations found for Activity ID: {}", activityId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "NO_CONTENT",
                                "message", "No recommendations found for this activity",
                                "activityId", activityId
                        ));
            }
            
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Recommendations retrieved successfully",
                    "count", recommendations.size(),
                    "data", recommendations
            ));
            
        } catch (Exception e) {
            log.error("Error fetching recommendations for Activity ID: {}", activityId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Failed to retrieve recommendations",
                            "error", e.getMessage()
                    ));
        }
    }

    /**
     * Get a specific recommendation by ID
     * 
     * @param recommendationId The recommendation ID
     * @return The recommendation details
     */
    @GetMapping("/{recommendationId}")
    public ResponseEntity<?> getRecommendationById(@PathVariable Long recommendationId) {
        log.info("Fetching recommendation with ID: {}", recommendationId);
        
        try {
            Optional<Recommendation> recommendation = recommendationService.getRecommendationById(recommendationId);
            
            if (recommendation.isEmpty()) {
                log.warn("Recommendation not found with ID: {}", recommendationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "NOT_FOUND",
                                "message", "Recommendation not found",
                                "id", recommendationId
                        ));
            }
            
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Recommendation retrieved successfully",
                    "data", recommendation.get()
            ));
            
        } catch (Exception e) {
            log.error("Error fetching recommendation with ID: {}", recommendationId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Failed to retrieve recommendation",
                            "error", e.getMessage()
                    ));
        }
    }

    /**
     * Get recommendations for a user with detailed statistics
     * 
     * @param userId The user ID
     * @return Recommendations with additional statistics
     */
    @GetMapping("/user/{userId}/with-stats")
    public ResponseEntity<?> getRecommendationsWithStats(@PathVariable Long userId) {
        log.info("Fetching recommendations with stats for User ID: {}", userId);
        
        try {
            List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("userId", userId);
            response.put("totalRecommendations", recommendations.size());
            response.put("data", recommendations);
            
            if (!recommendations.isEmpty()) {
                response.put("latestRecommendation", recommendations.get(0));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching recommendations with stats for User ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Failed to retrieve recommendations",
                            "error", e.getMessage()
                    ));
        }
    }

    /**
     * Health check endpoint
     * 
     * @return Service status
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "AI Recommendation Service",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
