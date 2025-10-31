package com.redis_timeseries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedisTimeseriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisTimeseriesApplication.class, args);
	}

}
