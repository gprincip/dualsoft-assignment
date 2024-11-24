package net.dualsoft.assignment.datastore.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.dualsoft.assignment.model.MatchResult;

@Repository
public interface MatchResultRepository extends CrudRepository<MatchResult, UUID>{

	@Query(value = "INSERT INTO match_result (match_id, team_a, team_b, score_a, score_b, result_timestamp) VALUES (:matchId, :teamA, :teamB, :scoreA, :scoreB, :resultTimestamp)", nativeQuery = true)
	void insertMatchResult(UUID matchId, String teamA, String teamB, Integer scoreA, Integer scoreB, LocalDateTime resultTimestamp);

}
