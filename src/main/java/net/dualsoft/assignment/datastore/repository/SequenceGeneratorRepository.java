package net.dualsoft.assignment.datastore.repository;

public interface SequenceGeneratorRepository {

	Long getNextSequenceValue(String sequenceName);

}