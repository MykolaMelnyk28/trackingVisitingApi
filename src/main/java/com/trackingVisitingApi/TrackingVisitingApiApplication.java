package com.trackingVisitingApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TrackingVisitingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingVisitingApiApplication.class, args);
	}

}
