package net.dualsoft.assignment.model;

public class RedisSchema {

	public static String createMatchResultProducedCacheKey(String matchUUID) {
		return RedisKeys.MATCH_RESULT_PRODUCED.getName() + ":" + matchUUID;
	}

}
