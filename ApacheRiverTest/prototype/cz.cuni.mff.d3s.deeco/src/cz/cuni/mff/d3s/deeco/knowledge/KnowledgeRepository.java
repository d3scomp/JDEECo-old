package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;


public abstract class KnowledgeRepository {

	public abstract Object get(String entryKey, ISession session) throws UnavailableEntryException, KnowledgeRepositoryException;
	
	public abstract Object [] findAll(String entryKey, ISession session) throws KnowledgeRepositoryException;

	public abstract void put(String entryKey, Object value, ISession session) throws KnowledgeRepositoryException;

	public abstract Object take(String entryKey, ISession session) throws KnowledgeRepositoryException;
	
	public abstract Object [] takeAll(String entryKey, ISession session) throws KnowledgeRepositoryException;

	public abstract ISession createSession();
	
	public Object get(String entryKey) throws UnavailableEntryException, KnowledgeRepositoryException {
		return get(entryKey, null);
	}
	
	public void put(String entryKey, Object value) throws KnowledgeRepositoryException {
		put(entryKey, value, null);
	}
	
	public Object take(String entryKey) throws KnowledgeRepositoryException {
		return take(entryKey, null);
	}
	
	public Object [] takeAll(String entryKey) throws KnowledgeRepositoryException {
		return takeAll(entryKey, null);
	}
	
	public Object [] findAll(String entryKey) throws KnowledgeRepositoryException {
		return findAll(entryKey, null);
	}
}
