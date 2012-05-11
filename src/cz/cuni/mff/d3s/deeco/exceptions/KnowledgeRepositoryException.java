package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * Thrown whenever there is a repository access problem.
 * 
 * @author Michal Kit
 * 
 */
public class KnowledgeRepositoryException extends Exception {

	public KnowledgeRepositoryException(String message) {
		super(message);
	}
}
