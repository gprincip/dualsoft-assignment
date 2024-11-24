package net.dualsoft.assignment.datastore.redis.util;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisConnectionProvider {
	
	JedisPool jedisPool;
	
	@PostConstruct
	public void init() {
		jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 63079);
	}
	
	@PreDestroy
	public void preDestroy() {
		jedisPool.close();
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
}

