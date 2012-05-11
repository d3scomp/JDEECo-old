package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * KMIllegalArgumentException is thrown whenever there is a type matching
 * related problem, while retrieving knowledge.
 * 
 * @author Michal Kit
 * 
 */
public class KMIllegalArgumentException extends KMException {
	public KMIllegalArgumentException(String message) {
		super(message);
	}
}
