//package com.redis_timeseries.utility;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.redis_timeseries.service.RedisTimeSeriesService;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class MarketDataSimulator {
//
//    private final RedisTimeSeriesService redisService;
//    private final Map<String, Double> prices = new ConcurrentHashMap<>();
//    private final Random random = new Random();
//    private final List<String> scrips = List.of("ASIANPAINT", "ASHOKLEY", "BAJFINANCE");
//
//    public MarketDataSimulator(RedisTimeSeriesService redisService) {
//        this.redisService = redisService;
//
//        // Setup only once
//        scrips.forEach(symbol -> {
//            redisService.setupSymbol(symbol);
//            prices.put(symbol, 225.0 + random.nextDouble() * 10);
//        });
//        System.out.println("Market simulator initialized with " + scrips.size() + " symbols.");
//    }
//
////    @Scheduled(fixedRate = 1000)
////    public void generateTicks() {
////        long ts = System.currentTimeMillis();
////
////        for (String symbol : scrips) {
////            double oldPrice = prices.get(symbol);
////            double change = (random.nextDouble() - 0.5) * 0.5;
////            double newPrice = Math.max(0.01, oldPrice + change); // Prevent negative prices
////            prices.put(symbol, newPrice);
////
////            double volume = 50 + random.nextInt(450);
////
////            redisService.insertTick(symbol, ts, newPrice, volume);
////            System.out.printf(" [%s] %d | Price=%.2f | Vol=%.0f%n", symbol, ts, newPrice, volume);
////        }
////    }
//    
////    @Scheduled(fixedRate = 1000)
//    public void generateTicks() {
//        long ts = System.currentTimeMillis();
//
//        for (String symbol : scrips) {
//            double oldPrice = prices.get(symbol);
//            double change = (random.nextDouble() - 0.5) * 0.5;
//            double newPrice = Math.max(0.01, oldPrice + change);
//            prices.put(symbol, newPrice);
//            double volume = 50 + random.nextInt(450);
//
//            redisService.insertTick(symbol, ts, newPrice, volume);
//        }
//
//        // FORCE bucket closure every 10 seconds (for testing)
//        if (ts % 10000 < 1000) {
//            long future = ts + 70_000; // jump 70 seconds
//            System.out.println("Fast-forwarding time to force aggregation...");
//            for (String symbol : scrips) {
//                redisService.insertTick(symbol, future, prices.get(symbol), 100);
//            }
//        }
//    }
//}