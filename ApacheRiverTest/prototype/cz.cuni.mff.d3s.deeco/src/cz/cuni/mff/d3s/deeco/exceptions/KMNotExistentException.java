package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * KMNotExistentException is thrown whenever the knowledge manager is unable to
 * retreive required property in the knowledge structure.
 * 
 * @author Michal Kit
 * 
 */
public class KMNotExistentException extends KMException {
	public KMNotExistentException(String message) {
		super(message);
	}
}
