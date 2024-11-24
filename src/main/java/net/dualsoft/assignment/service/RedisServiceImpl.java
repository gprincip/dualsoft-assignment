package net.dualsoft.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dualsoft.assignment.datastore.redis.util.RedisConnectionProvider;
import net.dualsoft.assignment.model.MatchResult;
import net.dualsoft.assignment.model.RedisSchema;
import net.dualsoft.assignment.util.JsonUtil;
import redis.clients.jedis.Jedis;

@Service
public class RedisServiceImpl implements RedisService {

	private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Autowired
	RedisConnectionProvider redisConnection;
    
    @Override
	public void storeMatchResult(MatchResult matchResult) {
		try (Jedis jedis = redisConnection.getJedisPool().getResource()) {
			
			jedis.setex(RedisSchema.createMatchResultProducedCacheKey(
					matchResult.getMatchId().toString()),
					300,
					JsonUtil.matchResultToJson(matchResult));
	
			log.info("Sent MatchResult to redis cache: " + matchResult);
		}
    }
	
}
