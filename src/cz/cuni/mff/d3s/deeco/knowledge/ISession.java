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
	 * Starts this session.
	 */
	public void begin();

	/**
	 * Ends this session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is required to be stopped before its
	 *             start
	 */
	public void end() throws SessionException;

	/**
	 * Cancels this session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is canceled after it was ended
	 */
	public void cancel() throws SessionException;

	/**
	 * Checks if this session should be repeated due to either it has
	 * not been started yet or an error which occurred during the session
	 * ending.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean repeat();

	/**
	 * Checks if this session has been successfully ended.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean hasSucceeded();
}
