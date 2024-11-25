package net.dualsoft.assignment.datastore.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.dualsoft.assignment.model.MatchResult;

@Repository
public interface MatchResultRepository extends CrudRepository<MatchResult, UUID>{

}
