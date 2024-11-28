package net.dualsoft.assignment.datastore.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SequenceGeneratorRepositoryImpl implements SequenceGeneratorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
	public Long getNextSequenceValue(String sequenceName) {
        return (Long) entityManager
                .createNativeQuery("SELECT nextval(:sequenceName)")
                .setParameter("sequenceName", sequenceName)
                .getSingleResult();
    }
	
}
