package com.redis_timeseries.config;

import org.springframework.context.annotation.Configuration;

import com.redis_timeseries.utility.PropertyContainer;
import com.redis_timeseries.utility.Utility;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@PropertySource("classpath:config.properties")
public class JedisPoolManager {

	String host = PropertyContainer.getInstance().config.get("REDIS_CONN_IP");
	int port = Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_PORT"));
	int connectionTimeout = Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_TIMEOUT"));
	int socketTimeout = Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_SOCKET_TIMEOUT"));
	int infiniteSocketTimeout = Utility
			.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_INFINITE_SOCKET_TIMEOUT"));
	final JedisPoolConfig poolConfig = buildPoolConfig();
	String password = PropertyContainer.getInstance().config.get("REDIS_PD");

	public JedisPool jedisPool = new JedisPool(poolConfig, host, port, connectionTimeout, socketTimeout,
			infiniteSocketTimeout, "default", "Portfolio1", 0, "default");

	private JedisPoolConfig buildPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setJmxEnabled(false);
//		poolConfig.setMaxTotal(Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_POOL")));
//		poolConfig.setMaxIdle(Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_POOL_MAX_IDLE")));
//		poolConfig.setMinIdle(Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_POOL_MIN_IDLE")));
////		    poolConfig.setTestOnBorrow(true);
////		    poolConfig.setTestOnReturn(true);
//		poolConfig.setTestWhileIdle(true);
////		    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
////		    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
//		poolConfig.setNumTestsPerEvictionRun(
//				Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_POOL")));
//		poolConfig.setBlockWhenExhausted(true);
//		poolConfig.setMaxWait(Duration.ofSeconds(60));
		return poolConfig;
	}

	public String getPoolUsage() {
		int active = jedisPool.getNumActive();
		int idle = jedisPool.getNumIdle();
		int total = active + idle;
		return String.format("Pool Usage: Total=%d, Active=%d, Idle=%d,	Waiters=%d", total, active, idle,
				jedisPool.getNumWaiters());
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
