package com.Fitness.AI.Service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Data
@Builder

public class Recommendation {
    @Id

    private Long id;
    private Long activityId;
    private Long userId;
    private String recommendation;
    @CreatedDate
    private LocalDateTime createdAt;

    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;


}
