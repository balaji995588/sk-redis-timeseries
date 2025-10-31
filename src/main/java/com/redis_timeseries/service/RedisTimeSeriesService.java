package com.redis_timeseries.service;

import redis.clients.jedis.*;
import redis.clients.jedis.timeseries.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.redis_timeseries.feed.GraphSummerizedTicks;
import com.redis_timeseries.utility.PropertyContainer;
import com.redis_timeseries.utility.Utility;

@Service
public class RedisTimeSeriesService {

	private static final Logger logger = LogManager.getLogger();

	private static final RedisTimeSeriesService INSTANCE = new RedisTimeSeriesService();

	private UnifiedJedis jedis;

	private static final long RAW_RETENTION_MS = 30L * 24 * 60 * 60 * 1000; // 30 days
	private static final long M1_RETENTION_MS = 7L * 24 * 60 * 60 * 1000; // 7 days
	private static final long M5_RETENTION_MS = 30L * 24 * 60 * 60 * 1000; // 30 days

	private static final String INITIALIZED_SET = "ts:initialized"; // Redis SET

	public RedisTimeSeriesService() {
		HostAndPort hostAndPort = new HostAndPort(PropertyContainer.getInstance().config.get("REDIS_CONN_IP"),
				Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_PORT")));

		DefaultJedisClientConfig config = DefaultJedisClientConfig.builder().user("default")
				.password(PropertyContainer.getInstance().config.get("REDIS_PD")).build();

		this.jedis = new UnifiedJedis(hostAndPort, config);
	}

	public static RedisTimeSeriesService getInstance() {
		return INSTANCE;
	}

	@PreDestroy
	public void close() {
		if (jedis != null)
			jedis.close();
	}

	// --------------------------------------------------------------------- //
	// KEY HELPERS
	// --------------------------------------------------------------------- //
	private String key(String scripCode, String field) {
		return scripCode + "_" + field;
	}

	private String rawKey(String scripCode, String field) {
		return key(scripCode, field);
	}

	private String m1Key(String scripCode, String field) {
		return key(scripCode, field + "_1m");
	}

	private String m5Key(String scripCode, String field) {
		return key(scripCode, field + "_5m");
	}


	private void ensureKey(String key, String type, String scripcode, long retentionMs) {
		try {
			jedis.tsCreate(key, TSCreateParams.createParams().retention(retentionMs)
					.labels(Map.of("type", type, "scrip", scripcode)));
		} catch (Exception e) {
//			if (!e.getMessage().contains("TSDB: key already exists")) {
//				// unexpected error – log it
////				logger.info("Failed to create TS key " + key + ": " + e.getMessage());
//			}
		}
	}

	// --------------------------------------------------------------------- //
	// ENSURE RULE (idempotent)
	// --------------------------------------------------------------------- //
	private void ensureRule(String source, String dest, AggregationType agg, long bucket) {
		try {
			jedis.tsCreateRule(source, dest, agg, bucket);
		} catch (Exception e) {
//			if (!e.getMessage().contains("TSDB: rule already exists")) {
////				logger.info("Failed to create rule " + source + " → " + dest + ": " + e.getMessage());
//			}
		}
	}

	private void initializeScrip(String scripCode) {
		// 1. Raw keys (30-day retention)
		ensureKey(rawKey(scripCode, "open"), "open", scripCode, RAW_RETENTION_MS);
		ensureKey(rawKey(scripCode, "high"), "high", scripCode, RAW_RETENTION_MS);
		ensureKey(rawKey(scripCode, "low"), "low", scripCode, RAW_RETENTION_MS);
		ensureKey(rawKey(scripCode, "close"), "close", scripCode, RAW_RETENTION_MS);
		ensureKey(rawKey(scripCode, "qty"), "qty", scripCode, RAW_RETENTION_MS);

		// 2. 1-minute keys
		ensureKey(m1Key(scripCode, "open"), "open", scripCode, M1_RETENTION_MS);
		ensureKey(m1Key(scripCode, "high"), "high", scripCode, M1_RETENTION_MS);
		ensureKey(m1Key(scripCode, "low"), "low", scripCode, M1_RETENTION_MS);
		ensureKey(m1Key(scripCode, "close"), "close", scripCode, M1_RETENTION_MS);
		ensureKey(m1Key(scripCode, "qty"), "qty", scripCode, M1_RETENTION_MS);

		// 3. 5-minute keys
		ensureKey(m5Key(scripCode, "open"), "open", scripCode, M5_RETENTION_MS);
		ensureKey(m5Key(scripCode, "high"), "high", scripCode, M5_RETENTION_MS);
		ensureKey(m5Key(scripCode, "low"), "low", scripCode, M5_RETENTION_MS);
		ensureKey(m5Key(scripCode, "close"), "close", scripCode, M5_RETENTION_MS);
		ensureKey(m5Key(scripCode, "qty"), "qty", scripCode, M5_RETENTION_MS);

		// 4. Rules (1-minute)
		long bucket1m = 60_000L;
		ensureRule(rawKey(scripCode, "open"), m1Key(scripCode, "open"), AggregationType.FIRST, bucket1m);
		ensureRule(rawKey(scripCode, "high"), m1Key(scripCode, "high"), AggregationType.MAX, bucket1m);
		ensureRule(rawKey(scripCode, "low"), m1Key(scripCode, "low"), AggregationType.MIN, bucket1m);
		ensureRule(rawKey(scripCode, "close"), m1Key(scripCode, "close"), AggregationType.LAST, bucket1m);
		ensureRule(rawKey(scripCode, "qty"), m1Key(scripCode, "qty"), AggregationType.SUM, bucket1m);

		// 5. Rules (5-minute)
		long bucket5m = 5 * 60_000L;
		ensureRule(rawKey(scripCode, "open"), m5Key(scripCode, "open"), AggregationType.FIRST, bucket5m);
		ensureRule(rawKey(scripCode, "high"), m5Key(scripCode, "high"), AggregationType.MAX, bucket5m);
		ensureRule(rawKey(scripCode, "low"), m5Key(scripCode, "low"), AggregationType.MIN, bucket5m);
		ensureRule(rawKey(scripCode, "close"), m5Key(scripCode, "close"), AggregationType.LAST, bucket5m);
		ensureRule(rawKey(scripCode, "qty"), m5Key(scripCode, "qty"), AggregationType.SUM, bucket5m);
	}

	// --------------------------------------------------------------------- //
	// PUBLIC: add tick
	// --------------------------------------------------------------------- //
	public synchronized void addGraphTick(GraphSummerizedTicks tick) {
		try {
			if (tick.getScripCode() == 26026) {
				String scripCode = String.valueOf(tick.getScripCode());

				if (scripCode.isEmpty()) {
					throw new IllegalArgumentException("ScripCode is required");
				}

				long tsSec = tick.getLtt();
				long ts = tsSec;
				if (tsSec > 0) {
					ts = tsSec * 1000;
				}
				// ---- 1. Ensure keys + rules exist (once per scrip) ----

				if (!jedis.exists(scripCode)) {
					initializeScrip(scripCode);
				}
				// ---- 2. Merge logic ----
				GraphSummerizedTicks existing = getTickAtTimestamp(scripCode, ts);
				GraphSummerizedTicks merged = existing != null ? mergeTicks(existing, tick) : tick;

				// ---- 3. Write to raw series (no retention param – already set on key) ----
				jedis.tsAdd(rawKey(scripCode, "open"), ts, merged.getOpen());
				jedis.tsAdd(rawKey(scripCode, "high"), ts, merged.getHigh());
				jedis.tsAdd(rawKey(scripCode, "low"), ts, merged.getLow());
				jedis.tsAdd(rawKey(scripCode, "close"), ts, merged.getClose());
				jedis.tsAdd(rawKey(scripCode, "qty"), ts, merged.getQty());
			}
		} catch (Exception e) {
			logger.error("Error while saving tick data of {} scrip for data {}, {}", tick.getScripCode(),
					tick.toString(), e.getMessage());
		}

	}


	public List<GraphSummerizedTicks> getChartData(String scripCode, long from, long to) {
		List<TSElement> open = jedis.tsRange(rawKey(scripCode, "open"), from, to);
		List<TSElement> high = jedis.tsRange(rawKey(scripCode, "high"), from, to);
		List<TSElement> low = jedis.tsRange(rawKey(scripCode, "low"), from, to);
		List<TSElement> close = jedis.tsRange(rawKey(scripCode, "close"), from, to);
		List<TSElement> qty = jedis.tsRange(rawKey(scripCode, "qty"), from, to);

		Map<Long, GraphSummerizedTicks> map = new LinkedHashMap<>();

		fill(map, open, (t, v) -> t.setOpen((float) v), scripCode);
		fill(map, high, (t, v) -> t.setHigh((float) v), scripCode);
		fill(map, low, (t, v) -> t.setLow((float) v), scripCode);
		fill(map, close, (t, v) -> t.setClose((float) v), scripCode);
		fill(map, qty, (t, v) -> t.setQty((long) v), scripCode);

		return new ArrayList<>(map.values());
	}

	private void fill(Map<Long, GraphSummerizedTicks> map, List<TSElement> data, ValueSetterDouble setter,
			String scripCode) {
		for (TSElement e : data) {
			long ts = e.getTimestamp();
			double val = e.getValue();
			GraphSummerizedTicks t = map.computeIfAbsent(ts, k -> {
				GraphSummerizedTicks nt = new GraphSummerizedTicks();
				nt.setLtt(ts);
				nt.setScripCode(Integer.parseInt(scripCode));
				return nt;
			});
			setter.set(t, val);
		}
	}

	@FunctionalInterface
	private interface ValueSetterDouble {
		void set(GraphSummerizedTicks tick, double value);
	}

	// --------------------------------------------------------------------- //
	// GET SINGLE TICK (for merge)
	// --------------------------------------------------------------------- //
	private GraphSummerizedTicks getTickAtTimestamp(String scripCode, long ts) {
		List<TSElement> open = jedis.tsRange(rawKey(scripCode, "open"), ts, ts);
		List<TSElement> high = jedis.tsRange(rawKey(scripCode, "high"), ts, ts);
		List<TSElement> low = jedis.tsRange(rawKey(scripCode, "low"), ts, ts);
		List<TSElement> close = jedis.tsRange(rawKey(scripCode, "close"), ts, ts);
		List<TSElement> qty = jedis.tsRange(rawKey(scripCode, "qty"), ts, ts);

		if (open.isEmpty())
			return null;

		GraphSummerizedTicks t = new GraphSummerizedTicks();
		t.setLtt(ts);
		t.setScripCode(Integer.parseInt(scripCode));
		t.setOpen((float) open.get(0).getValue());
		t.setHigh((float) high.get(0).getValue());
		t.setLow((float) low.get(0).getValue());
		t.setClose((float) close.get(0).getValue());
		t.setQty(qty.isEmpty() ? 0 : (long) qty.get(0).getValue());
		return t;
	}

	private GraphSummerizedTicks mergeTicks(GraphSummerizedTicks prev, GraphSummerizedTicks cur) {
		cur.setQty(cur.getQty() + prev.getQty());

		if ("A".equalsIgnoreCase(prev.getTickType())) {
			cur.setOpen(prev.getOpen());
		}

		switch (cur.getTickType()) {
		case "LH":
		case "HL":
		case "EL":
		case "EH":
		case "E":
			if (prev.getHigh() > cur.getHigh())
				cur.setHigh(prev.getHigh());
			if (prev.getLow() < cur.getLow())
				cur.setLow(prev.getLow());
			break;
		case "H":
			if (prev.getHigh() > cur.getHigh())
				cur.setHigh(prev.getHigh());
			break;
		case "L":
			if (prev.getLow() < cur.getLow())
				cur.setLow(prev.getLow());
			break;
		default:
			switch (prev.getTickType()) {
			case "E":
			case "A":
			case "O":
				cur.setOpen(prev.getOpen());
			case "LH":
			case "HL":
			case "EL":
			case "EH":
				if (prev.getHigh() > cur.getHigh())
					cur.setHigh(prev.getHigh());
				if (prev.getLow() < cur.getLow())
					cur.setLow(prev.getLow());
				cur.setTickType(prev.getTickType());
				break;
			case "H":
				if (prev.getHigh() > cur.getHigh())
					cur.setHigh(prev.getHigh());
				cur.setTickType(prev.getTickType());
				break;
			case "L":
				if (prev.getLow() < cur.getLow())
					cur.setLow(prev.getLow());
				cur.setTickType(prev.getTickType());
				break;
			}
			break;
		}
		return cur;
	}
}