package net.dualsoft.assignment.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
@Table(name = "match_result")
public class MatchResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "match_id")
	private UUID matchId;
	
	@Column(name="team_a")
	private String teamA;
	
	@Column(name="team_b")
	private String teamB;
	
	@Column(name="score_a")
	private Integer scoreA;
	
	@Column(name="score_b")
	private Integer scoreB;
	
	@Column(name="result_timestamp")
	private LocalDateTime resultTimestamp;

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

	public LocalDateTime getResultTimestamp() {
		return resultTimestamp;
	}

	public void setResultTimestamp(LocalDateTime resultTimestamp) {
		this.resultTimestamp = resultTimestamp;
	}

}
