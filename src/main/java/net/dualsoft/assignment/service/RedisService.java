package net.dualsoft.assignment.service;

import net.dualsoft.assignment.model.MatchResult;

public interface RedisService {

	void storeMatchResult(MatchResult matchResult);
	MatchResult getMatchResult(String redisKey);

}