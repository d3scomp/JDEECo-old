package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * Thrown by the knowledge repository whenever the
 * entry specified by the key is unavailable.
 * 
 * @author Michal Kit
 * 
 */
public class UnavailableEntryException extends Exception {

	public UnavailableEntryException(String message) {
		super(message);
	}
}
