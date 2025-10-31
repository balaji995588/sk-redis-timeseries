package com.redis_timeseries.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redis_timeseries.feed.Server;

import jakarta.annotation.PostConstruct;

@Component
public class WebInitializr {
	
	@Autowired
	private Server server;

	@PostConstruct
	public void startThreads() {
		this.server.Init();
	}
}
