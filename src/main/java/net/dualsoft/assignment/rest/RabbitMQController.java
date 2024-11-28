package net.dualsoft.assignment.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.dualsoft.assignment.configuration.RabbitMQConfig;
import net.dualsoft.assignment.datastore.repository.SequenceGeneratorRepository;
import net.dualsoft.assignment.events.producer.MatchQueueProducer;
import net.dualsoft.assignment.model.MatchResult;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQController.class);
	
    @Autowired
    private MatchQueueProducer producer;
    
    @Autowired
    SequenceGeneratorRepository sequenceGenerator;

    @PostMapping("/addMatchToQueue")
    public void sendMessage(@RequestBody MatchResult matchResult) {
    	matchResult.setId(sequenceGenerator.getNextSequenceValue("match_result_id_seq"));
        producer.sendMessage(matchResult);

    }
}
