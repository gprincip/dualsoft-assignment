package net.dualsoft.assignment.events.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dualsoft.assignment.model.MatchResult;
import net.dualsoft.assignment.service.RedisService;

@Service
public class MatchQueueProducer {

	private static final Logger log = LoggerFactory.getLogger(MatchQueueProducer.class);
	
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RedisService redisService;
    
    public void sendMessage(MatchResult matchResult) {
    	
        CorrelationData correlationData = new CorrelationData(matchResult.getMatchId().toString());
    	
        rabbitTemplate.convertAndSend("amq.topic", "match-result-updates-routing-key", matchResult, correlationData);
        log.info("Sent MatchResult to rabbitmq queue: " + matchResult);
        
        redisService.storeMatchResult(matchResult);
        
    }
}

