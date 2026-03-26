package com.HealthApp.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication




public class ActivitiesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiesServiceApplication.class, args);
	}

}
