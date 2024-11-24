package net.dualsoft.assignment.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.dualsoft.assignment.datastore.repository.MatchResultRepository;
import net.dualsoft.assignment.model.MatchResult;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	MatchResultRepository repository;
	
    @GetMapping("/testDbSave")
    public void testDbSave() {
    	
    	MatchResult result = new MatchResult();
    	result.setMatchId(UUID.randomUUID());
    	result.setScoreA(2);
    	result.setScoreB(0);
    	result.setTeamA("TeamA");
    	result.setTeamB("TeamB");
    	
    	repository.save(result);
    	
    }
	
}
