package com.HealthApp.demo.repository;

import com.HealthApp.demo.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ActivityRepository extends MongoRepository<Activity, String> {



}
