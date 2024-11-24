package net.dualsoft.assignment.service;

public interface MatchResultPublisherNackHandler {

	void handlePublishNack(String matchId);

}