package net.dualsoft.assignment.datastore.dto;

import java.util.UUID;

public class MatchResultDTO {

	private UUID matchId;

	private String teamA;

	private String teamB;

	private Integer scoreA;

	private Integer scoreB;

	public UUID getMatchId() {
		return matchId;
	}

	public void setMatchId(UUID matchId) {
		this.matchId = matchId;
	}

	public String getTeamA() {
		return teamA;
	}

	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}

	public Integer getScoreA() {
		return scoreA;
	}

	public void setScoreA(Integer scoreA) {
		this.scoreA = scoreA;
	}

	public Integer getScoreB() {
		return scoreB;
	}

	public void setScoreB(Integer scoreB) {
		this.scoreB = scoreB;
	}
	
}
