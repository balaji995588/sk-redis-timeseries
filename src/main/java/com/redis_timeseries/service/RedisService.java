//package com.redis_timeseries.service;
//
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.UnifiedJedis;
//import redis.clients.jedis.timeseries.*;
//
//import java.util.*;
//
//@Service
//public class RedisTimeSeriesService {
//
//    private final UnifiedJedis jedis;
//
//    public RedisTimeSeriesService(UnifiedJedis jedis) {
//        this.jedis = jedis;
//    }
//
//    private static final Map<String, Long> TIMEFRAMES = Map.of(
//            "1s", 1000L,
//            "1m", 60000L,
//            "1h", 3600000L
//    );
//
//    // --- Base Series Creation ---
//    public void createBaseSeries(String symbol) {
//        String priceKey = "stock:" + symbol + ":price";
//        String volumeKey = "stock:" + symbol + ":volume";
//
//        if (!jedis.exists(priceKey)) {
//            jedis.tsCreate(priceKey, TSCreateParams.createParams()
//                    .labels(Map.of("symbol", symbol, "type", "price")));
//        }
//        if (!jedis.exists(volumeKey)) {
//            jedis.tsCreate(volumeKey, TSCreateParams.createParams()
//                    .labels(Map.of("symbol", symbol, "type", "volume")));
//            
//        }
//        System.out.println("Created base series for " + symbol);
//    }
//
//    // --- OHLCV Series Creation ---
//    public void createOHLCVSeries(String symbol, String timeframe) {
//        String[] metrics = {"open", "high", "low", "close", "volume"};
//        for (String metric : metrics) {
//            String key = "stock:" + symbol + ":" + timeframe + ":" + metric;
//            if (!jedis.exists(key)) {
//                jedis.tsCreate(key, TSCreateParams.createParams()
//                        .labels(Map.of("symbol", symbol, "timeframe", timeframe, "metric", metric)));
//            }
//        }
//        System.out.println("Created OHLCV series for " + symbol + " @" + timeframe);
//    }
//
//    // --- Aggregation Rules ---
//    public void createAggregationRules(String symbol, String timeframe, long bucketSize) {
//        String srcPrice = "stock:" + symbol + ":price";
//        String srcVolume = "stock:" + symbol + ":volume";
//
//        jedis.tsCreateRule(srcPrice, "stock:" + symbol + ":" + timeframe + ":open", AggregationType.FIRST, bucketSize);
//        jedis.tsCreateRule(srcPrice, "stock:" + symbol + ":" + timeframe + ":high", AggregationType.MAX, bucketSize);
//        jedis.tsCreateRule(srcPrice, "stock:" + symbol + ":" + timeframe + ":low", AggregationType.MIN, bucketSize);
//        jedis.tsCreateRule(srcPrice, "stock:" + symbol + ":" + timeframe + ":close", AggregationType.LAST, bucketSize);
//        jedis.tsCreateRule(srcVolume, "stock:" + symbol + ":" + timeframe + ":volume", AggregationType.SUM, bucketSize);
//
//        System.out.println("Aggregation rules created for " + symbol + " @" + timeframe);
//    }
//
//    // --- Initialize full OHLCV setup ---
//    public void setupSymbol(String symbol) {
//        createBaseSeries(symbol);
//        
//        System.out.println("Setup complete for " + symbol);
//    }
//
//    // --- Insert Tick Data ---
//    public void insertTick(String symbol, long timestamp, double price, double volume) {
//        jedis.tsAdd("stock:" + symbol + ":price", timestamp, price);
//        jedis.tsAdd("stock:" + symbol + ":volume", timestamp, volume);
//    }
//
//    // --- Query Aggregated OHLCV Data ---
//    public Map<String, TSMRangeElements> queryOHLCV(String symbol, String timeframe, long fromTimestamp, long toTimestamp) {
//        String[] filters = {
//                "symbol=" + symbol,
//                "timeframe=" + timeframe
//        };
//
//        TSMRangeParams params = new TSMRangeParams()
//                .filter(filters)
//                .fromTimestamp(fromTimestamp)
//                .toTimestamp(toTimestamp)
//                .withLabels();
//        Map<String, TSMRangeElements> tsMRange = jedis.tsMRange(params);
//        return tsMRange;
//    }
//
//    // Overloaded version for string-based timestamps (e.g., "-", "+")
//    public Map<String, TSMRangeElements> queryOHLCV(String symbol, String timeframe, String start, String end) {
//        long from = start.equals("-") ? 0 : Long.parseLong(start);
//        long to = end.equals("+") ? System.currentTimeMillis() + 86_400_000 : Long.parseLong(end); // +1 day buffer
//        return queryOHLCV(symbol, timeframe, from, to);
//    }
//    
// // Add this method to RedisTimeSeriesService
//    public void backfillHistorical(String symbol) {
//        long now = System.currentTimeMillis();
//        long start = now - 3600000; // 1 hour ago
//        Random r = new Random();
//
//        for (long ts = start; ts < now; ts += 1000) {
//            double price = 225 + r.nextDouble() * 10;
//            double volume = 100 + r.nextInt(400);
//            insertTick(symbol, ts, price, volume);
//        }
//        System.out.println("Backfilled 1 hour of data for " + symbol);
//    }
//}

package com.redis_timeseries.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.timeseries.*;
import redis.clients.jedis.timeseries.TSInfo.Rule;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;

@Service
public class RedisService {

	private final UnifiedJedis jedis;

	public RedisService(UnifiedJedis jedis) {
		this.jedis = jedis;
	}

	private static final Map<String, Long> TIMEFRAMES = Map.of("1s", 1_000L, "1m", 60_000L, "1h", 3_600_000L);

	/* ----------------------------------------------------- */
	/* 1. Base series – idempotent */
	/* ----------------------------------------------------- */
	public void createBaseSeries(String symbol) {
		String priceKey = "stock:" + symbol + ":price";
		String volKey = "stock:" + symbol + ":volume";

		createIfMissing(priceKey, TSCreateParams.createParams().labels(Map.of("symbol", symbol, "type", "price")));

		createIfMissing(volKey, TSCreateParams.createParams().labels(Map.of("symbol", symbol, "type", "volume")));
	}

	/* ----------------------------------------------------- */
	/* 2. OHLCV series – idempotent */
	/* ----------------------------------------------------- */
	public void createOHLCVSeries(String symbol, String timeframe) {
		String[] metrics = { "open", "high", "low", "close", "volume" };
		for (String m : metrics) {
			String key = "stock:" + symbol + ":" + timeframe + ":" + m;
			createIfMissing(key, TSCreateParams.createParams()
					.labels(Map.of("symbol", symbol, "timeframe", timeframe, "metric", m)));
		}
	}

	/* ----------------------------------------------------- */
	/* 3. Aggregation rules – **idempotent** */
	/* ----------------------------------------------------- */
	public void createAggregationRules(String symbol, String timeframe, long bucketSize) {
		String srcPrice = "stock:" + symbol + ":price";
		String srcVolume = "stock:" + symbol + ":volume";

		// price → open, high, low, close
		safeCreateRule(srcPrice, destKey(symbol, timeframe, "open"), AggregationType.FIRST, bucketSize);
		safeCreateRule(srcPrice, destKey(symbol, timeframe, "high"), AggregationType.MAX, bucketSize);
		safeCreateRule(srcPrice, destKey(symbol, timeframe, "low"), AggregationType.MIN, bucketSize);
		safeCreateRule(srcPrice, destKey(symbol, timeframe, "close"), AggregationType.LAST, bucketSize);

		// volume → volume (SUM)
		safeCreateRule(srcVolume, destKey(symbol, timeframe, "volume"), AggregationType.SUM, bucketSize);
	}

	private String destKey(String symbol, String tf, String metric) {
		return "stock:" + symbol + ":" + tf + ":" + metric;
	}

	/* ----------------------------------------------------- */
	/* Helper: create key only if it does not exist */
	/* ----------------------------------------------------- */
	private void createIfMissing(String key, TSCreateParams params) {
		if (!jedis.exists(key)) {
			jedis.tsCreate(key, params);
		}
	}

	/* ----------------------------------------------------- */
	/* Helper: create rule only if destination has NO src rule */
	/* ----------------------------------------------------- */
	private void safeCreateRule(String src, String dest, AggregationType agg, long bucketMs) {
		// 1. Does the destination key exist?
		if (!jedis.exists(dest)) {
			// Key missing → we can safely create the rule (it will also create the key)
			try {
				jedis.tsCreateRule(src, dest, agg, bucketMs);
				return;
			} catch (JedisDataException e) {
				// fall-through – maybe another thread created it concurrently
			}
		}

		// 2. Destination exists → check its rules via TS.INFO
		TSInfo info = jedis.tsInfo(dest);
		Map<String, Rule> rules = info.getRules(); // List<List<Object>>

		for (Map.Entry<String, Rule> entryMap : rules.entrySet()) {
			String ruleSrc = entryMap.getKey();
//            String ruleSrc = (String) rule.get(0);           // source key
			Rule value = entryMap.getValue();

			long ruleBucket = value.getBucketDuration();
			;
			String ruleAgg = value.getCompactionKey(); // e.g. "MAX"

			if (ruleSrc.equals(src) && ruleBucket == bucketMs && ruleAgg.equalsIgnoreCase(agg.name())) {
				// rule already present – nothing to do
				return;
			}
		}

		// 3. No matching rule → create it (Redis will reject duplicate src rule,
		// but we already filtered, so it should succeed)
		try {
			jedis.tsCreateRule(src, dest, agg, bucketMs);
		} catch (JedisDataException e) {
			if (!e.getMessage().contains("already has a src rule")) {
				throw e; // unexpected error
			}
			// ignore – another thread beat us
		}
	}

	/* ----------------------------------------------------- */
	/* 4. Full idempotent setup */
	/* ----------------------------------------------------- */
	public void setupSymbol(String symbol) {
		createBaseSeries(symbol);
		TIMEFRAMES.forEach((tf, bucket) -> {
			createOHLCVSeries(symbol, tf);
			createAggregationRules(symbol, tf, bucket);
		});
		System.out.println("Setup complete for " + symbol);
	}

	/* ----------------------------------------------------- */
	/* 5. Insert tick */
	/* ----------------------------------------------------- */
	public void insertTick(String symbol, long ts, double price, double volume) {
		jedis.tsAdd("stock:" + symbol + ":price", ts, price);
		jedis.tsAdd("stock:" + symbol + ":volume", ts, volume);
	}

	/* ----------------------------------------------------- */
	/* 6. Query OHLCV – works with real timestamps */
	/* ----------------------------------------------------- */
	public Map<String, TSMRangeElements> queryOHLCV(String symbol, String timeframe, long fromTs, long toTs) {
		String[] filters = { "symbol=" + symbol, "timeframe=" + timeframe };

		TSMRangeParams params = new TSMRangeParams().filter(filters).fromTimestamp(fromTs).toTimestamp(toTs)
				.withLabels();

		return jedis.tsMRange(params);
	}

	/* overload for strings "-" / "+" */
	public Map<String, TSMRangeElements> queryOHLCV(String symbol, String timeframe, String start, String end) {
		long from = start.equals("-") ? 0L : Long.parseLong(start);
		long to = end.equals("+") ? Long.MAX_VALUE : Long.parseLong(end);
		return queryOHLCV(symbol, timeframe, from, to);
	}

	public void backfillHistorical(String symbol) {
		long now = System.currentTimeMillis();
		long start = now - 3600000; // 1 hour ago
		Random r = new Random();

		for (long ts = start; ts < now; ts += 1000) {
			double price = 225 + r.nextDouble() * 10;
			double volume = 100 + r.nextInt(400);
			insertTick(symbol, ts, price, volume);
		}
		System.out.println("Backfilled 1 hour of data for " + symbol);
	}
}