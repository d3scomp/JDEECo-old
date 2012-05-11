package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;

public interface ISession {
	public void begin();
	public void end() throws SessionException;
	public void cancel() throws SessionException;
	public boolean repeat();
	public boolean hasSucceeded();
}
