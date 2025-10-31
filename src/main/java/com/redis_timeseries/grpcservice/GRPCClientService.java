//package com.reportservice.grpcservice;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Service;
//
//import com.reportservice.feed.FeedGrpcServiceGrpc;
//import com.reportservice.feed.LTPDataResponse;
//import com.reportservice.feed.SubscribeRequest;
//
//import io.grpc.Context;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import io.grpc.stub.StreamObserver;
////import net.devh.boot.grpc.client.inject.GrpcClient;
//
//@Service
//public class GRPCClientService {
//
////	private ConcurrentMap<String, List<LTPDataResponse>> feedData = new ConcurrentHashMap<>();
////	
////	private static final Logger LOGGER = LogManager.getLogger();
////	
////
////	@GrpcClient("feedService")
////	private FeedGrpcServiceGrpc.FeedGrpcServiceStub feedGrpcServiceStub;
////	
////	public void subscribeLtp(String exchange) {
////		SubscribeRequest subscribeRequest = SubscribeRequest.newBuilder().setExchange(exchange).build();
////		feedGrpcServiceStub.subscribeLTP(subscribeRequest, new StreamObserver<LTPDataResponse>() {
////			
////			@Override
////			public void onNext(LTPDataResponse ltpDataResponse) {
////				LOGGER.info("LTPDataResponse {}",ltpDataResponse.getLtpDataMapMap().toString());
////				feedData.compute(exchange, (key, existingList) -> {
////					List<LTPDataResponse> updatedList = (existingList != null) ? existingList : new LinkedList<>();
//////					LTPDataResponse first = ((List<LTPDataResponse>) updatedList).get(0);
//////					Map<String, LTPData> ltpDataMapMap = first.getLtpDataMapMap();
////					updatedList.add(ltpDataResponse);
////					return updatedList;
////				});
////				
////			}
////			
////			@Override
////			public void onError(Throwable t) {
////				LOGGER.error("GRPC Response failed for Exchange {}", subscribeRequest.getExchange(), t);
////				t.printStackTrace();
////			}
////			
////			@Override
////			public void onCompleted() {
////				LOGGER.info("GRPC Response completed for Exchange {}", subscribeRequest.getExchange());
////			}
////		});
////	}
//
////	private final ConcurrentMap<String, List<LTPDataResponse>> feedData = new ConcurrentHashMap<>();
////
////	private final ConcurrentMap<String, Context.CancellableContext> subscriptionContexts = new ConcurrentHashMap<>();
////
////	private static final Logger LOGGER = LogManager.getLogger();
////
////	@GrpcClient("feedService")
////	private FeedGrpcServiceGrpc.FeedGrpcServiceStub feedGrpcServiceStub;
////
////	public void subscribeLtp(String exchange) {
////		// If already subscribed, ignore or unsubscribe first
////		if (subscriptionContexts.containsKey(exchange)) {
////			LOGGER.warn("Already subscribed to exchange {}, ignoring duplicate subscribe", exchange);
////			return;
////		}
////
////		SubscribeRequest subscribeRequest = SubscribeRequest.newBuilder().setExchange(exchange).build();
////
////		// Create a cancellable context for this subscription
////		Context.CancellableContext context = Context.current().withCancellation();
////		subscriptionContexts.put(exchange, context);
////
////		context.run(() -> {
////			feedGrpcServiceStub.subscribeLTP(subscribeRequest, new StreamObserver<LTPDataResponse>() {
////				@Override
////				public void onNext(LTPDataResponse ltpDataResponse) {
////					LOGGER.info("LTPDataResponse for {}: {}", exchange, ltpDataResponse.getLtpDataMapMap());
////					feedData.compute(exchange, (key, existingList) -> {
////						List<LTPDataResponse> updatedList = (existingList != null) ? existingList : new LinkedList<>();
////						updatedList.add(ltpDataResponse);
////						return updatedList;
////					});
////				}
////
////				@Override
////				public void onError(Throwable t) {
////					LOGGER.error("GRPC Response failed for Exchange {}", subscribeRequest.getExchange(), t);
//////					t.printStackTrace();
////					// Cleanup on error
////					unsubscribe(exchange);
////				}
////
////				@Override
////				public void onCompleted() {
////					LOGGER.info("GRPC Response completed for Exchange {}", subscribeRequest.getExchange());
////					// Cleanup on complete
//////					unsubscribe(exchange);
////				}
////			});
////		});
////	}
////
////	public void unsubscribe(String exchange) {
////		Context.CancellableContext context = subscriptionContexts.remove(exchange);
////		if (context != null) {
////			context.cancel(new RuntimeException("Unsubscribed from exchange " + exchange));
////			LOGGER.info("Unsubscribed from exchange {}", exchange);
////		} else {
////			LOGGER.warn("No active subscription found for exchange {}", exchange);
////		}
////	}
////
////	public void disconnect() {
////		// Cancel all active subscriptions
////		subscriptionContexts.keySet().forEach(this::unsubscribe);
////		LOGGER.info("Disconnected all subscriptions");
////	}
//
//	private final ConcurrentMap<String, List<LTPDataResponse>> feedData = new ConcurrentHashMap<>();
//
//	// Map from exchange to subscription info (channel + context)
//	private final ConcurrentMap<String, SubscriptionInfo> subscriptions = new ConcurrentHashMap<>();
//
//	private static final Logger LOGGER = LogManager.getLogger();
//
//	public CompletableFuture<Void> subscribeLtp(String exchange, String host, int port) throws Exception {
//
//		CompletableFuture<Void> resultFuture = new CompletableFuture<>();
//
//		// If already subscribed, ignore or unsubscribe first
//		if (subscriptions.containsKey(exchange)) {
//			resultFuture.completeExceptionally(new RuntimeException("Already subscribed to exchange " + exchange));
//			return resultFuture;
//		}
//
//		// 1. Build channel dynamically
//		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build(); // assuming
//																										// PLAINTEXT,
//		// handle TLS if needed
//
//		// 2. Create stub from channel
//		FeedGrpcServiceGrpc.FeedGrpcServiceStub stub = FeedGrpcServiceGrpc.newStub(channel);
//
//		// 3. Create cancellable context
//		Context.CancellableContext context = Context.current().withCancellation();
//
//		// 4. Keep subscription info
//		SubscriptionInfo subInfo = new SubscriptionInfo(channel, context);
//		subscriptions.put(exchange, subInfo);
//
//		SubscribeRequest subscribeRequest = SubscribeRequest.newBuilder().setExchange(exchange).build();
//
//		// 5. Run gRPC call in cancellable context
//		context.run(() -> {
//			stub.subscribeLTP(subscribeRequest, new StreamObserver<LTPDataResponse>() {
//				@Override
//				public void onNext(LTPDataResponse ltpDataResponse) {
//					LOGGER.info("LTPDataResponse for {}: {}", exchange, ltpDataResponse.getLtpDataMapMap());
//					feedData.compute(exchange, (key, existingList) -> {
//						List<LTPDataResponse> updatedList = (existingList != null) ? existingList : new LinkedList<>();
//						updatedList.add(ltpDataResponse);
//						return updatedList;
//					});
//					resultFuture.complete(null);
//				}
//
//				@Override
//				public void onError(Throwable t) {
//					LOGGER.error("GRPC Response failed for Exchange {}", exchange, t.getMessage());
//					unsubscribe(exchange); // cleanup on error
//					resultFuture.completeExceptionally(new RuntimeException(
//							"GRPC Response failed for Exchange " + exchange + " " + t.getMessage()));
//				}
//
//				@Override
//				public void onCompleted() {
//					LOGGER.info("GRPC Response completed for Exchange {}", exchange);
////                    unsubscribe(exchange); // cleanup on complete
//				}
//			});
//		});
//		return resultFuture;
//
//	}
//
//	public void unsubscribe(String exchange) {
//		SubscriptionInfo subInfo = subscriptions.remove(exchange);
//		if (subInfo != null) {
//			subInfo.context.cancel(new RuntimeException("Unsubscribed from exchange " + exchange));
//			subInfo.channel.shutdown();
//			LOGGER.info("Unsubscribed and shutdown channel for exchange {}", exchange);
//		} else {
//			LOGGER.warn("No active subscription found for exchange {}", exchange);
//		}
//	}
//
//	public void disconnect() {
//		subscriptions.keySet().forEach(this::unsubscribe);
//		LOGGER.info("Disconnected all subscriptions");
//	}
//
//	// Helper class to keep channel and context per subscription
//	private static class SubscriptionInfo {
//		final ManagedChannel channel;
//		final Context.CancellableContext context;
//
//		SubscriptionInfo(ManagedChannel channel, Context.CancellableContext context) {
//			this.channel = channel;
//			this.context = context;
//		}
//	}
//}





