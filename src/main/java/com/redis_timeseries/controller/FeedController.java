package com.redis_timeseries.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.redis_timeseries.service.FeedServerService;
import com.redis_timeseries.utility.CxfResponse;

@RestController
public class FeedController {

	@Autowired
	private FeedServerService feedServerService;

	@GetMapping(value = { "feedserver/startExchange/{exName}/{start}/{ip}/{port}/{sport}" })
	public ResponseEntity<?> startExchange(@PathVariable("exName") String exName, @PathVariable("start") Boolean start,
			@PathVariable("ip") String ip, @PathVariable("port") int port, @PathVariable("sport") int sport) {
		try {
			CxfResponse cxfResponse = feedServerService.startExchangeByName(exName, start, ip, port, sport);
			return new ResponseEntity<>(cxfResponse, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
