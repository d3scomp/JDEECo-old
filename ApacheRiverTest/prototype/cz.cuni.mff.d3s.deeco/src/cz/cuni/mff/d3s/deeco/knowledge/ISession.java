package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;

/**
 * Interface representing session, which is used for performing bulk of
 * operations on the knowledge repository. It is used to guarantee data
 * consistency in the knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public interface ISession {

	/**
	 * Function starts the session.
	 */
	public void begin();

	/**
	 * Function ends the session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is required to be stopped before its
	 *             start
	 */
	public void end() throws SessionException;

	/**
	 * Function used to abort the session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is canceled after it was ended
	 */
	public void cancel() throws SessionException;

	/**
	 * Function checks if the session should be repeated due to either it has
	 * not been started yet or an error which occurred during the session
	 * ending.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean repeat();

	/**
	 * Function checks if the session has been successfully ended.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean hasSucceeded();
}
