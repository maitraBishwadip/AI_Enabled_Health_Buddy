package com.Fitness.AI.Service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;




@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Activity {


    private Long ActivityId;
    private Long userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate







    private LocalDateTime updatedAt;




    @Field("metrics")
    private Map<String, Object> additionalMetrics;







}
