package com.redis_timeseries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redis_timeseries.utility.PropertyContainer;
import com.redis_timeseries.utility.Utility;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class BeansConfig {

//	@Bean
//	public UnifiedJedis unifiedJedis() {
//		return new UnifiedJedis(new HostAndPort(PropertyContainer.getInstance().config.get("REDIS_CONN_IP"),
//				Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_PORT"))));
//	}
	
	 @Bean
	    public UnifiedJedis unifiedJedis() {
	        // Define connection host and port
	        HostAndPort hostAndPort = new HostAndPort(PropertyContainer.getInstance().config.get("REDIS_CONN_IP"), Utility.getInt(PropertyContainer.getInstance().config.get("REDIS_CONN_PORT")));

	        // Define username and password
	        DefaultJedisClientConfig config = DefaultJedisClientConfig.builder()
	                .user("default")       // Replace with your Redis username (omit if not using ACL)
	                .password(PropertyContainer.getInstance().config.get("REDIS_PD"))   // Replace with your Redis password
	                .build();

	        // Return UnifiedJedis instance
	        return new UnifiedJedis(hostAndPort, config);
	    }
}
