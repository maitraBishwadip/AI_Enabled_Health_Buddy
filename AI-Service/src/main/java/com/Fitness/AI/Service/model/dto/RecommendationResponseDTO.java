package com.Fitness.AI.Service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for extracting structured recommendation data from OpenAI API response
 * This class helps parse the JSON response from OpenAI into our domain model
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDTO {

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("suggestions")
    private List<String> suggestions;

    @JsonProperty("improvements")
    private List<String> improvements;

    @JsonProperty("safety_alerts")
    private List<String> safetyAlerts;
}

