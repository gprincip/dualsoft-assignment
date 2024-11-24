package net.dualsoft.assignment.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.dualsoft.assignment.events.consumer.MatchQueueConsumer;
import net.dualsoft.assignment.events.producer.MatchQueueProducer;
import net.dualsoft.assignment.model.MatchResult;
import net.dualsoft.assignment.service.MatchResultPublisherNackHandler;

@Configuration
public class RabbitMQConfig {

	private static final Logger log = LoggerFactory.getLogger(MatchQueueProducer.class);
	
	private static final String QUEUE_NAME = "match-result-updates";
    private static final String EXCHANGE_NAME = "amq.topic";
    private static final String ROUTING_KEY = "match-result-updates-routing-key";

    @Autowired
    MatchResultPublisherNackHandler matchResultPublisherNackHandler;
    
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setExchange(EXCHANGE_NAME);
        rabbitTemplate.setRoutingKey(ROUTING_KEY);
        
        // Set up confirm callback for producer
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
            	log.info("Message confirmed: " + correlationData);
            } else {            	
            	log.warn("Message not confirmed: " + cause);
            	matchResultPublisherNackHandler.handlePublishNack(correlationData.getId());
            }
        });
        
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public SimpleMessageListenerContainer listenerContainer(CachingConnectionFactory connectionFactory,
                                                            MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(messageListenerAdapter);
        
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MatchQueueConsumer consumer) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(consumer);
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }
    
    @Bean
    Queue queue() {
      return new Queue(QUEUE_NAME, true);
    }

    @Bean
    TopicExchange exchange() {
      return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
}
