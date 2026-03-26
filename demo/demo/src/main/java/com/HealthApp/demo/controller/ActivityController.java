package com.HealthApp.demo.controller;

import com.HealthApp.demo.dto.ActivityRequest;
import com.HealthApp.demo.dto.ActivityResponse;
import com.HealthApp.demo.services.ActivityService.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor

@RestController
@RequestMapping("/activities")


public class ActivityController {

    private final ActivityService activityService ;


    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request)
    {
        return ResponseEntity.ok(activityService.trackActivity(request));

    }


}
