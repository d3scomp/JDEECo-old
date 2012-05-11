package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * KnowledgeRepositoryException is thrown whenever there is a repository
 * communication problem.
 * 
 * @author Michal Kit
 * 
 */
public class KnowledgeRepositoryException extends Exception {

	public KnowledgeRepositoryException(String message) {
		super(message);
	}
}
