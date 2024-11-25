package net.dualsoft.assignment.datastore.redis.util;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisConnectionProvider {
	
	private static final String REDIS_PORT_ENV_VARIABLE = "REDIS_PORT";
	private static final String REDIS_HOST_ENV_VARIABLE = "REDIS_HOST";
	
	JedisPool jedisPool;
	
	@PostConstruct
	public void init() {
		jedisPool = new JedisPool(new JedisPoolConfig(), System.getenv(REDIS_HOST_ENV_VARIABLE), Integer.parseInt(System.getenv(REDIS_PORT_ENV_VARIABLE)));
	}
	
	@PreDestroy
	public void preDestroy() {
		jedisPool.close();
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
}

