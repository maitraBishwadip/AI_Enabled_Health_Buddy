package com.HealthApp.demo.services.ActivityService;

import com.HealthApp.demo.dto.ActivityRequest;
import com.HealthApp.demo.dto.ActivityResponse;
import com.HealthApp.demo.model.Activity;
import com.HealthApp.demo.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;



    public ActivityResponse trackActivity(ActivityRequest request) {

  Boolean isValidUser =    userValidationService.validateUser((request.getUserId()));

  if(!isValidUser){
      throw new RuntimeException("Invalid user"+request.getUserId());
  }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

      Activity savedActivity =   activityRepository.save(activity);


      try {
          kafkaTemplate.send(topicName, String.valueOf(savedActivity.getUserId()), savedActivity);
      } catch(Exception e)
      {
          e.printStackTrace();
      }



        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {

        ActivityResponse response = new ActivityResponse();

        response.setActivityId(activity.getActivityId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;



    }

}
