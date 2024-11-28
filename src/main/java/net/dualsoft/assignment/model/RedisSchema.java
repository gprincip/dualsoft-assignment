package net.dualsoft.assignment.model;

public class RedisSchema {

	public static String createMatchResultProducedCacheKey(String id) {
		return RedisKeys.MATCH_RESULT_PRODUCED.getName() + ":" + id;
	}

}
