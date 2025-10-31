package com.redis_timeseries.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.redis_timeseries.service.RedisTimeSeriesService;

import redis.clients.jedis.timeseries.TSMRangeElements;

@RestController
public class RedisController {

	@Autowired
	private RedisTimeSeriesService redisService;

	@PostMapping("/setup/{symbol}")
	public String setup(@PathVariable String symbol) {
		redisService.createBaseSeries(symbol);

		Map<String, Long> timeframes = Map.of("1s", 1000L, "1m", 60000L, "1h", 3600000L);

		timeframes.forEach((tf, bucket) -> {
			redisService.createOHLCVSeries(symbol, tf);
			redisService.createAggregationRules(symbol, tf, bucket);
		});

		return "✅ Setup completed for " + symbol;
	}

	@PostMapping("/tick/{symbol}")
	public String addTick(@PathVariable String symbol, @RequestBody Map<String, Object> tick) {
		long ts = ((Number) tick.get("timestamp")).longValue();
		double price = ((Number) tick.get("price")).doubleValue();
		double volume = ((Number) tick.get("volume")).doubleValue();
		redisService.insertTick(symbol, ts, price, volume);
		return "📈 Tick inserted for " + symbol;
	}

	@GetMapping("/{symbol}/{timeframe}")
	public Map<String, TSMRangeElements> getOHLCV(@PathVariable String symbol, @PathVariable String timeframe) {
		return redisService.queryOHLCV(symbol, timeframe, "3600000", "0");
	}

	@GetMapping("/backfillHistorical/{symbol}")
	public ResponseEntity<?> backfillHistorical(@PathVariable String symbol) {
		redisService.backfillHistorical(symbol);
		return ResponseEntity.ok("Success");
	}
}
