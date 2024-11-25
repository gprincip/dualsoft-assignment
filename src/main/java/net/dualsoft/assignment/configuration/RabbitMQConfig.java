package net.dualsoft.assignment.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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

	private static final String RABBITMQ_HOST_ENV_VARIABLE = "RABBITMQ_HOST";

	private static final String RABBITMQ_PASSWORD_ENV_VARIABLE = "RABBITMQ_PASSWORD";

	private static final String RABBITMQ_USERNAME_ENV_VARIABLE = "RABBITMQ_USERNAME";

	private static final Logger log = LoggerFactory.getLogger(MatchQueueProducer.class);

	private static final String X_DEAD_LETTER_ROUTING_KEY_ARG_VALUE = "DLQ-match-result-updates";
	private static final String X_DEAD_LETTER_ROUTING_KEY_ARG_KEY = "x-dead-letter-routing-key";
	private static final String X_DEAD_LETTER_EXCHANGE_ARG_VALUE = "DLX-match-result-updates";
	private static final String X_DEAD_LETTER_EXCHANGE_ARG_KEY = "x-dead-letter-exchange";
	
	private static final String MATCH_RESULT_UPDATES_QUEUE_NAME = "match-result-updates";
    private static final String MATCH_RESULT_UPDATES_EXCHANGE_NAME = "amq.direct";
    public static final String MATCH_RESULT_UPDATES_ROUTING_KEY = "match-result-updates-routing-key";

    @Autowired
    MatchResultPublisherNackHandler publisherNackHandler;
    
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(System.getenv(RABBITMQ_HOST_ENV_VARIABLE));
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setUsername(System.getenv(RABBITMQ_USERNAME_ENV_VARIABLE));
        connectionFactory.setPassword(System.getenv(RABBITMQ_PASSWORD_ENV_VARIABLE));
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setExchange(MATCH_RESULT_UPDATES_EXCHANGE_NAME);
        rabbitTemplate.setRoutingKey(MATCH_RESULT_UPDATES_ROUTING_KEY);
        
        // Set up confirm callback for producer
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
            	log.info("Message confirmed: " + correlationData);
            } else {            	
            	log.warn("Message not confirmed: " + cause);
            	publisherNackHandler.handlePublishNack(correlationData.getId());
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
        container.setQueueNames(MATCH_RESULT_UPDATES_QUEUE_NAME);
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
        return QueueBuilder.durable(MATCH_RESULT_UPDATES_QUEUE_NAME)
                .withArgument(X_DEAD_LETTER_EXCHANGE_ARG_KEY, X_DEAD_LETTER_EXCHANGE_ARG_VALUE)
                .withArgument(X_DEAD_LETTER_ROUTING_KEY_ARG_KEY, X_DEAD_LETTER_ROUTING_KEY_ARG_VALUE)
                .build();
    }

    @Bean
    DirectExchange exchange() {
      return new DirectExchange(MATCH_RESULT_UPDATES_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with(MATCH_RESULT_UPDATES_ROUTING_KEY);
    }
    
}
