package cz.cuni.mff.d3s.deeco.exceptions;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;

/**
 * SessionException is thrown whenever there is a session related problem.
 * @see ISession
 * 
 * @author Michal Kit
 * 
 */
public class SessionException extends Exception {

	public SessionException(String msg) {
		super(msg);
	}

}
