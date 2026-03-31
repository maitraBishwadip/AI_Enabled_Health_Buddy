package com.Fitness.AI.Service.service;

import com.Fitness.AI.Service.model.Activity;
import com.Fitness.AI.Service.model.Recommendation;
import com.Fitness.AI.Service.model.dto.RecommendationResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating AI-powered fitness recommendations using OpenAI API
 * Handles prompt engineering, API communication, and response parsing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {
    
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    /**
     * Generates a comprehensive fitness recommendation based on activity data
     * Uses structured prompt engineering to ensure consistent JSON responses
     * 
     * @param activity The activity data to analyze
     * @return Recommendation object with parsed AI insights
     */
    public Recommendation generateRecommendation(Activity activity) {
        log.info("Generating recommendation for Activity ID: {}", activity.getActivityId());
        
        try {
            // Build structured prompt for OpenAI
            String prompt = buildStructuredPrompt(activity);
            
            // Call OpenAI API through Spring AI ChatClient
            String aiResponse = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();
            
            log.debug("Received AI response: {}", aiResponse);
            
            // Extract JSON from response
            RecommendationResponseDTO parsedResponse = parseAIResponse(aiResponse);
            
            // Map to Recommendation entity
            Recommendation recommendation = mapToRecommendation(activity, parsedResponse);
            
            log.info("Successfully generated recommendation for Activity ID: {}", activity.getActivityId());
            return recommendation;
            
        } catch (Exception e) {
            log.error("Error generating recommendation for Activity ID: {}", activity.getActivityId(), e);
            // Return default recommendation with error handling
            return buildDefaultRecommendation(activity);
        }
    }

    /**
     * Builds a structured prompt that encourages JSON-formatted responses
     * Uses chain-of-thought prompting for better results
     */
    private String buildStructuredPrompt(Activity activity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return String.format("""
                Analyze the following fitness activity and provide a detailed recommendation.
                
                ACTIVITY DATA:
                - Activity Type: %s
                - Duration: %d minutes
                - Calories Burned: %d
                - Start Time: %s
                - User ID: %d
                - Additional Metrics: %s
                
                Please provide your analysis in the following JSON format (and ONLY return valid JSON, no additional text):
                {
                    "overview": "A brief 1-2 sentence overview of the workout performance",
                    "suggestions": ["Suggestion 1", "Suggestion 2", "Suggestion 3"],
                    "improvements": ["Improvement 1", "Improvement 2", "Improvement 3"],
                    "safety_alerts": ["Alert 1 if applicable", "Alert 2 if applicable"]
                }
                
                Ensure:
                1. The overview is concise but informative
                2. Provide 3 specific, actionable suggestions
                3. Identify 3 areas where the user can improve
                4. List any safety concerns or alerts
                5. Base recommendations on the activity type and metrics provided
                
                Return ONLY the JSON object, no markdown formatting or additional text.
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime() != null ? activity.getStartTime().format(formatter) : "N/A",
                activity.getUserId(),
                activity.getAdditionalMetrics() != null ? activity.getAdditionalMetrics().toString() : "None"
        );
    }

    /**
     * Parses the AI response and extracts JSON
     * Handles potential formatting issues in the response
     */
    private RecommendationResponseDTO parseAIResponse(String response) throws Exception {
        // Clean up the response - remove markdown code blocks if present
        String cleanedResponse = response
                .replace("```json", "")
                .replace("```", "")
                .trim();
        
        // Try to find JSON in the response
        int jsonStart = cleanedResponse.indexOf("{");
        int jsonEnd = cleanedResponse.lastIndexOf("}");
        
        if (jsonStart == -1 || jsonEnd == -1) {
            log.warn("No JSON found in response, creating default recommendation");
            return RecommendationResponseDTO.builder()
                    .overview("Workout completed successfully")
                    .suggestions(List.of("Continue with consistent training", "Track your progress", "Stay hydrated"))
                    .improvements(List.of("Increase workout duration gradually", "Monitor heart rate", "Improve form"))
                    .safetyAlerts(new ArrayList<>())
                    .build();
        }
        
        String jsonString = cleanedResponse.substring(jsonStart, jsonEnd + 1);
        return objectMapper.readValue(jsonString, RecommendationResponseDTO.class);
    }

    /**
     * Maps the parsed AI response to a Recommendation entity
     */
    private Recommendation mapToRecommendation(Activity activity, RecommendationResponseDTO dto) {
        return Recommendation.builder()
                .activityId(activity.getActivityId())
                .userId(activity.getUserId())
                .recommendation(dto.getOverview())
                .suggestions(dto.getSuggestions() != null ? dto.getSuggestions() : new ArrayList<>())
                .improvements(dto.getImprovements() != null ? dto.getImprovements() : new ArrayList<>())
                .safety(dto.getSafetyAlerts() != null ? dto.getSafetyAlerts() : new ArrayList<>())
                .build();
    }

    /**
     * Builds a default recommendation when AI generation fails
     */
    private Recommendation buildDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getActivityId())
                .userId(activity.getUserId())
                .recommendation("Workout completed. Keep up the consistent exercise routine!")
                .suggestions(List.of(
                        "Continue with your current training schedule",
                        "Stay hydrated and maintain proper nutrition",
                        "Monitor your progress over time"
                ))
                .improvements(List.of(
                        "Track additional metrics like heart rate",
                        "Vary your workout types for balanced fitness",
                        "Consider working with a trainer for form analysis"
                ))
                .safety(List.of("Ensure proper warm-up and cool-down"))
                .build();
    }
}
