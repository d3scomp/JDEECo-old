package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * KMException is the base class for the the Knowledge Manager related
 * exceptions.
 * 
 * @author Michal Kit
 * 
 */
public class KMException extends Exception {
	public KMException(String message) {
		super(message);
	}
}
