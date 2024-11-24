package net.dualsoft.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import net.dualsoft.assignment.datastore.redis.util.RedisConnectionProvider;
import net.dualsoft.assignment.events.consumer.MatchQueueConsumer;
import net.dualsoft.assignment.model.MatchResult;
import net.dualsoft.assignment.model.RedisSchema;
import net.dualsoft.assignment.util.JsonUtil;
import redis.clients.jedis.Jedis;

@Service
public class MatchResultPublisherNackHandlerImpl implements MatchResultPublisherNackHandler {

	private static final Logger log = LoggerFactory.getLogger(MatchResultPublisherNackHandlerImpl.class); 
	
	@Autowired
	RedisConnectionProvider redisConnection;
	
	@Override
	public void handlePublishNack(String matchId) {
		
		String redisKey = RedisSchema.createMatchResultProducedCacheKey(matchId);
		
		try (Jedis jedis = redisConnection.getJedisPool().getResource()) {
			String matchResultJson = jedis.get(redisKey);
			
			if(StringUtils.isNotBlank(matchResultJson)) {
				MatchResult matchResult = JsonUtil.jsonToMatchResult(matchResultJson);
				log.error("Handling failed match result: " + matchResult);
			}
			
		}
		
	}
	
}
