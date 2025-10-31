package com.redis_timeseries.grpcservice;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis_timeseries.feed.FeedInfo;
import com.redis_timeseries.feed.LTPData;
import com.redis_timeseries.feed.LTPDataResponse;
import com.redis_timeseries.feed.Server;
import com.redis_timeseries.feed.SubscribeRequest;

import io.grpc.stub.StreamObserver;

@Service
public class GrpcDataListenerService {

	@Autowired
	private GrpcSubscriptionManager subscriptionManager;

//	private ConcurrentMap<String, List<LTPDataResponse>> feedData = new ConcurrentHashMap<>();

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private Server server;

//	public GrpcDataListenerService(GrpcSubscriptionManager subscriptionManager) {
//		this.subscriptionManager = subscriptionManager;
//	}

	public CompletableFuture<Void> startListening(String exchange) {
		CompletableFuture<Void> resultFuture = new CompletableFuture<>();

		if (!subscriptionManager.isSubscribed(exchange)) {
			resultFuture
					.completeExceptionally(new RuntimeException("No active subscription for exchange: " + exchange));
			return resultFuture;
		}

		GrpcSubscriptionManager.SubscriptionInfo info = subscriptionManager.getSubscription(exchange);
		SubscribeRequest subscribeRequest = SubscribeRequest.newBuilder().setExchange(exchange).build();

		info.context.run(() -> {
			info.stub.subscribeLTP(subscribeRequest, new StreamObserver<LTPDataResponse>() {
				@Override
				public void onNext(LTPDataResponse ltpDataResponse) {
//					logger.info("LTPDataResponse for {}: {}", exchange, ltpDataResponse.getLtpDataMapMap());
//					feedData.compute(exchange, (key, existingList) -> {
//						List<LTPDataResponse> updatedList = (existingList != null) ? existingList : new LinkedList<>();
//						updatedList.add(ltpDataResponse);
//						return updatedList;
//					});

					if (ltpDataResponse.getLtpDataMapMap() != null && !ltpDataResponse.getLtpDataMapMap().isEmpty()) {
						Map<String, LTPData> map = ltpDataResponse.getLtpDataMapMap();
//						map.forEach((K,V)->{
//							logger.info("Key : {}, Value : {}",K,V.toString());
//						});
//						
//						
//						for(LTPData ltpData: map.values()) {
//							server.pushIntoExchangeServer(ltpData);
//						}
						if (map != null && !map.isEmpty()) {
							map.entrySet().parallelStream().forEach(entry -> {
								String key = entry.getKey();
								LTPData value = entry.getValue();

								logger.info("Key : {}, Value : {}", key, value);
								server.pushIntoExchangeServer(value);
							});
						}
					}
					// Complete on first successful response
					if (!resultFuture.isDone()) {
						resultFuture.complete(null);
					}
				}

				@Override
				public void onError(Throwable t) {
//					t.printStackTrace();
					logger.error("gRPC stream error for exchange {}: {}", exchange, t.getMessage());
					subscriptionManager.unsubscribe(exchange);
					resultFuture.completeExceptionally(new RuntimeException("gRPC error for exchange " + exchange, t));
				}

				@Override
				public void onCompleted() {
					logger.info("gRPC stream completed for exchange {}", exchange);
				}
			});
		});

		return resultFuture;
	}

	public FeedInfo getFeedData(int exchange, int scripcode) {
		return this.server.getFeedData(exchange, scripcode);
	}
}
