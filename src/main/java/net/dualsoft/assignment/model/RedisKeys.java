package net.dualsoft.assignment.model;

public enum RedisKeys {
	
	MATCH_RESULT_PRODUCED("cache:rabbitmq:produced_match_result");
	
	String name;
	
	private RedisKeys(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
