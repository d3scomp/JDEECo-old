package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * Thrown whenever there is a type matching related problem, while retrieving
 * knowledge the knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public class KMIllegalArgumentException extends KMException {
	public KMIllegalArgumentException(String message) {
		super(message);
	}
}
