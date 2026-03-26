package com.HealthApp.demo.services.ActivityService;

import com.HealthApp.demo.dto.ActivityRequest;
import com.HealthApp.demo.dto.ActivityResponse;
import com.HealthApp.demo.model.Activity;
import com.HealthApp.demo.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class ActivityService {

    private final ActivityRepository activityRepository;


    public ActivityResponse trackActivity(ActivityRequest request) {


        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

      Activity savedActivity =   activityRepository.save(activity);



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
