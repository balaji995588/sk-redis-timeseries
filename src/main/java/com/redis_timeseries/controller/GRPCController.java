package com.redis_timeseries.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis_timeseries.feed.FeedInfo;
import com.redis_timeseries.feed.LTPDataResponse;
import com.redis_timeseries.grpcservice.GrpcDataListenerService;
import com.redis_timeseries.grpcservice.GrpcSubscriptionManager;


@RestController
@RequestMapping(value = { "/grpc" })
public class GRPCController {

	@Autowired
	private GrpcSubscriptionManager subscriptionManager;

	@Autowired
	private GrpcDataListenerService dataListenerService;

	@GetMapping("/subscribe/{exchange}/{flag}/{host}/{port}")
	public ResponseEntity<?> subscribe(@PathVariable String exchange,@PathVariable String flag, @PathVariable String host,
			@PathVariable int port) {

		try {
			// 1. Create the connection (subscribe)
			if("true".equalsIgnoreCase(flag)) {
				subscriptionManager.createSubscription(exchange, host, port);

				// 2. Start listening
				dataListenerService.startListening(exchange).get();

				return ResponseEntity.ok("Subscribed to " + exchange + " at " + host + ":" + port);
			}else {
				subscriptionManager.unsubscribe(exchange);
				return ResponseEntity.ok("Unsubscribed from exchange " + exchange);
			}
			
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("gRPC subscription failed: " + cause.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
		}
	}

//	@GetMapping("/unsubscribe/{exchange}")
//	public ResponseEntity<?> unsubscribe(@PathVariable String exchange) {
//		subscriptionManager.unsubscribe(exchange);
//		return ResponseEntity.ok("Unsubscribed from exchange " + exchange);
//	}

	@GetMapping("/data/{exchange}/{scripcode}")
	public ResponseEntity<?> getData(@PathVariable int exchange, @PathVariable int scripcode) {
		FeedInfo feedData = dataListenerService.getFeedData(exchange,scripcode);
		return ResponseEntity.ok(feedData);
	}

	@GetMapping(value = { "/disconnect" })
	public ResponseEntity<?> disconnect() {
		try {
			subscriptionManager.disconnectAll();
			return new ResponseEntity<>("Successfully disconnected to grpc server", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
}
