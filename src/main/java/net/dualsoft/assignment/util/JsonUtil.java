package net.dualsoft.assignment.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.dualsoft.assignment.model.MatchResult;

public class JsonUtil {

	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	
	public static MatchResult jsonToMatchResult(String json) {
		ObjectMapper obj = new ObjectMapper();
		try {
			return obj.readValue(json, MatchResult.class);
		} catch (IOException e) {
			log.error("Error deserializing MatchResult: " + json, e);
			return null;
		}
	}
	
	public static String matchResultToJson(MatchResult matchResult) {
		ObjectMapper obj = new ObjectMapper();
		obj.registerModule(new JavaTimeModule());
		try {
			return obj.writeValueAsString(matchResult);
		} catch (JsonProcessingException e) {
			log.error("Error serializing MatchResult: " + matchResult, e);
			return null;
		}
	}
	
	public static void main(String args[]) {
		
		MatchResult matchResult = new MatchResult();
		matchResult.setMatchId(UUID.randomUUID());
		matchResult.setResultTimestamp(LocalDateTime.now());
		matchResult.setScoreA(2);
		matchResult.setScoreB(1);
		matchResult.setTeamA("Team A");
		matchResult.setTeamB("Team B");
		
		log.info(matchResultToJson(matchResult));
		
	}
	
}
