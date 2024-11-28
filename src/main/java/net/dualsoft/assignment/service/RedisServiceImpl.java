package net.dualsoft.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import net.dualsoft.assignment.datastore.redis.util.RedisConnectionProvider;
import net.dualsoft.assignment.model.MatchResult;
import net.dualsoft.assignment.model.RedisSchema;
import net.dualsoft.assignment.util.JsonUtil;
import redis.clients.jedis.Jedis;

@Service
public class RedisServiceImpl implements RedisService {

	private static final int PRODUCED_MATCH_RESULT_TTL = 300;

	private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Autowired
	RedisConnectionProvider redisConnection;
    
    @Override
	public void storeMatchResult(MatchResult matchResult) {
		try (Jedis jedis = redisConnection.getJedisPool().getResource()) {
			
			jedis.setex(RedisSchema.createMatchResultProducedCacheKey(
					matchResult.getId().toString()),
					PRODUCED_MATCH_RESULT_TTL,
					JsonUtil.matchResultToJson(matchResult));
	
			log.info("Sent MatchResult to redis cache: " + matchResult);
		}
    }
    
    public MatchResult getMatchResult(String redisKey) {
	    try (Jedis jedis = redisConnection.getJedisPool().getResource()) {
			String matchResultJson = jedis.get(redisKey);
			
			if(StringUtils.isNotBlank(matchResultJson)) {
				MatchResult matchResult = JsonUtil.jsonToMatchResult(matchResultJson);
				return matchResult;
			}
			
		}
	    return null;
    }
	
}
