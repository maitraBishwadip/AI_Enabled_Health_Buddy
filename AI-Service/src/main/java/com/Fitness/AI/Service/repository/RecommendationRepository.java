package com.Fitness.AI.Service.repository;

import com.Fitness.AI.Service.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository  extends MongoRepository<Recommendation, Long> {


    List<Recommendation> findByUserId(Long userId);

    List<Recommendation> findByActivityId(Long activityId);
}
