package net.dualsoft.assignment.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.dualsoft.assignment.events.producer.MatchQueueProducer;
import net.dualsoft.assignment.model.MatchResult;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

    @Autowired
    private MatchQueueProducer producer;

    @PostMapping("/addMatchToQueue")
    public void sendMessage(@RequestBody MatchResult matchResult) {
    	    	
        producer.sendMessage(matchResult);

    }
}
