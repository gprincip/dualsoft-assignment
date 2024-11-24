package net.dualsoft.assignment.events.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import net.dualsoft.assignment.datastore.repository.MatchResultRepository;
import net.dualsoft.assignment.model.MatchResult;

@Component
public class MatchQueueConsumer implements ChannelAwareMessageListener{

	private static final Logger log = LoggerFactory.getLogger(MatchQueueConsumer.class);

	@Autowired
	MatchResultRepository repository;
	
	@Autowired
	Jackson2JsonMessageConverter jsonMessageConverter;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
        MatchResult matchResult = (MatchResult) jsonMessageConverter.fromMessage(message);
        
        try {
        	repository.insertMatchResult(
        			matchResult.getMatchId(),
        			matchResult.getTeamA(),
        			matchResult.getTeamB(),
        			matchResult.getScoreA(),
        			matchResult.getScoreB(),
        			matchResult.getResultTimestamp());
        	
			log.info("Match result persisted to database!", matchResult);
			
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			
		} catch (Exception e) {
			try {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
				log.error("Error processing message! Message requeued!", e);
			} catch (IOException exception) {
				log.info("Error sending basic Nack!", exception);
			}
		}

	}
}
