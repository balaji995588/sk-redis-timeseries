package com.redis_timeseries.grpcservice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.redis_timeseries.feed.FeedGrpcServiceGrpc;

import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class GrpcSubscriptionManager {

	private final ConcurrentMap<String, SubscriptionInfo> subscriptions = new ConcurrentHashMap<>();
	private static final Logger logger = LogManager.getLogger();

	public boolean isSubscribed(String exchange) {
		return subscriptions.containsKey(exchange);
	}

	public void createSubscription(String exchange, String host, int port) {
		if (subscriptions.containsKey(exchange)) {
			throw new RuntimeException("Already subscribed to exchange: " + exchange);
		}

		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		FeedGrpcServiceGrpc.FeedGrpcServiceStub stub = FeedGrpcServiceGrpc.newStub(channel);
		Context.CancellableContext context = Context.current().withCancellation();

		subscriptions.put(exchange, new SubscriptionInfo(channel, context, stub));
	}

	public SubscriptionInfo getSubscription(String exchange) {
		return subscriptions.get(exchange);
	}

	public void unsubscribe(String exchange) {
		SubscriptionInfo subInfo = subscriptions.remove(exchange);
		if (subInfo != null) {
			subInfo.context.cancel(new RuntimeException("Unsubscribed from exchange " + exchange));
			subInfo.channel.shutdown();
			logger.info("Unsubscribed and shut down channel for exchange {}", exchange);
		} else {
			logger.warn("No active subscription found for exchange {}", exchange);
		}
	}

	public void disconnectAll() {
		subscriptions.keySet().forEach(this::unsubscribe);
		logger.info("Disconnected all subscriptions");
	}

	public static class SubscriptionInfo {
		public final ManagedChannel channel;
		public final Context.CancellableContext context;
		public final FeedGrpcServiceGrpc.FeedGrpcServiceStub stub;

		public SubscriptionInfo(ManagedChannel channel, Context.CancellableContext context,
				FeedGrpcServiceGrpc.FeedGrpcServiceStub stub) {
			this.channel = channel;
			this.context = context;
			this.stub = stub;
		}
	}

}
