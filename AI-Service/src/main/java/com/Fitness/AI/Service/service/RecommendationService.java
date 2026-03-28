package com.Fitness.AI.Service.service;

import com.Fitness.AI.Service.model.Recommendation;
import com.Fitness.AI.Service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor


public class RecommendationService {

    private final RecommendationRepository recommendationRepository;



    public List<Recommendation> getRecommendationsForUser(Long userId) {

        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getRecommendationsForActivity(Long activityId) {
        
        return recommendationRepository.findByActivityId(activityId);
    }
    
    
}
