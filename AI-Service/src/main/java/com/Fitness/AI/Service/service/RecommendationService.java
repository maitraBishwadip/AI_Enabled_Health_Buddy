package com.Fitness.AI.Service.service;

import com.Fitness.AI.Service.model.Recommendation;
import com.Fitness.AI.Service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Recommendation entity operations
 * Handles business logic for storing and retrieving recommendations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    /**
     * Saves a recommendation to the database
     * 
     * @param recommendation The recommendation to save
     * @return The saved recommendation with generated ID
     */
    public Recommendation saveRecommendation(Recommendation recommendation) {
        log.debug("Saving recommendation for Activity ID: {}, User ID: {}", 
                recommendation.getActivityId(), recommendation.getUserId());
        
        try {
            Recommendation saved = recommendationRepository.save(recommendation);
            log.info("Recommendation saved successfully with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Failed to save recommendation for Activity ID: {}", 
                    recommendation.getActivityId(), e);
            throw new RuntimeException("Failed to save recommendation", e);
        }
    }

    /**
     * Retrieves all recommendations for a specific user
     * 
     * @param userId The user ID
     * @return List of recommendations for the user
     */
    public List<Recommendation> getRecommendationsForUser(Long userId) {
        log.debug("Fetching recommendations for User ID: {}", userId);
        
        try {
            List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
            log.info("Found {} recommendations for User ID: {}", recommendations.size(), userId);
            return recommendations;
        } catch (Exception e) {
            log.error("Failed to fetch recommendations for User ID: {}", userId, e);
            throw new RuntimeException("Failed to fetch user recommendations", e);
        }
    }

    /**
     * Retrieves all recommendations for a specific activity
     * 
     * @param activityId The activity ID
     * @return List of recommendations for the activity
     */
    public List<Recommendation> getRecommendationsForActivity(Long activityId) {
        log.debug("Fetching recommendations for Activity ID: {}", activityId);
        
        try {
            List<Recommendation> recommendations = recommendationRepository.findByActivityId(activityId);
            log.info("Found {} recommendations for Activity ID: {}", recommendations.size(), activityId);
            return recommendations;
        } catch (Exception e) {
            log.error("Failed to fetch recommendations for Activity ID: {}", activityId, e);
            throw new RuntimeException("Failed to fetch activity recommendations", e);
        }
    }

    /**
     * Retrieves a recommendation by its ID
     * 
     * @param id The recommendation ID
     * @return Optional containing the recommendation if found
     */
    public Optional<Recommendation> getRecommendationById(Long id) {
        log.debug("Fetching recommendation with ID: {}", id);
        
        try {
            Optional<Recommendation> recommendation = recommendationRepository.findById(id);
            if (recommendation.isPresent()) {
                log.info("Found recommendation with ID: {}", id);
            } else {
                log.warn("Recommendation not found with ID: {}", id);
            }
            return recommendation;
        } catch (Exception e) {
            log.error("Failed to fetch recommendation with ID: {}", id, e);
            throw new RuntimeException("Failed to fetch recommendation", e);
        }
    }

    /**
     * Deletes a recommendation by its ID
     * 
     * @param id The recommendation ID
     */
    public void deleteRecommendation(Long id) {
        log.debug("Deleting recommendation with ID: {}", id);
        
        try {
            recommendationRepository.deleteById(id);
            log.info("Recommendation deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete recommendation with ID: {}", id, e);
            throw new RuntimeException("Failed to delete recommendation", e);
        }
    }
}
