package com.Fitness.AI.Service.service;

import com.Fitness.AI.Service.model.Activity;
import com.Fitness.AI.Service.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer Service
 * Listens to activity events from the Activities Service and processes them
 * Triggers AI recommendation generation for each received activity
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final OpenAIService openAIService;
    private final RecommendationService recommendationService;

    /**
     * Kafka listener that consumes activity messages
     * 
     * Flow:
     * 1. Receive Activity message from Kafka topic
     * 2. Generate AI recommendation using OpenAI
     * 3. Save recommendation to MongoDB
     * 4. Log completion
     * 
     * @param activity The activity event received from Kafka
     */
    @KafkaListener(
            topics = "${kafka.topic.name}",
            groupId = "activity-processor-group"
    )
    public void processActivity(Activity activity) {
        log.info("Received Activity from Kafka - User ID: {}, Activity ID: {}", 
                activity.getUserId(), activity.getActivityId());
        
        try {
            // Validate activity data
            if (!isValidActivity(activity)) {
                log.warn("Received invalid activity data: {}", activity);
                return;
            }
            
            // Generate AI recommendation
            log.debug("Generating AI recommendation for Activity ID: {}", activity.getActivityId());
            Recommendation recommendation = openAIService.generateRecommendation(activity);
            
            // Save recommendation to database
            log.debug("Saving recommendation to database for Activity ID: {}", activity.getActivityId());
            recommendationService.saveRecommendation(recommendation);
            
            log.info("Successfully processed Activity ID: {} - Recommendation saved", 
                    activity.getActivityId());
            
        } catch (Exception e) {
            log.error("Error processing activity ID: {} for user: {}", 
                    activity.getActivityId(), activity.getUserId(), e);
            // Continue processing other messages - don't fail the whole consumer
        }
    }

    /**
     * Validates the activity data before processing
     */
    private boolean isValidActivity(Activity activity) {
        if (activity == null) {
            log.warn("Activity is null");
            return false;
        }
        
        if (activity.getActivityId() == null || activity.getUserId() == null) {
            log.warn("Activity or User ID is null");
            return false;
        }
        
        if (activity.getType() == null) {
            log.warn("Activity Type is null");
            return false;
        }
        
        if (activity.getDuration() == null || activity.getDuration() <= 0) {
            log.warn("Invalid activity duration: {}", activity.getDuration());
            return false;
        }
        
        return true;
    }
}
