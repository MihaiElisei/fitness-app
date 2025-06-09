package com.fitness.activity_service.service;

import com.fitness.activity_service.dto.ActivityRequest;
import com.fitness.activity_service.dto.ActivityResponse;
import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

       Activity savedActivity = activityRepository.save(activity);
       return mapToActivityResponse(savedActivity);
    }

    private ActivityResponse mapToActivityResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
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

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapToActivityResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId).map(this::mapToActivityResponse).orElseThrow(() ->new RuntimeException("Activity not found with id: " + activityId));
    }
}
