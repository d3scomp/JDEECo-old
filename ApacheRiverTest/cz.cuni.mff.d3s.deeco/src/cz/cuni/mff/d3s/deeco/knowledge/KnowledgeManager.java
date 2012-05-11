package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;


public abstract class KnowledgeManager {
	
	public abstract Object getKnowledge(String knowledgePath, Class structure, ISession session) throws KMException;
	
	public abstract void putKnowledge(String knowledgePath, Object value, ISession session) throws KMException;
	
	public abstract Object takeKnowledge(String knowledgePath, Class structure, ISession session) throws KMException;
	
	public abstract String [] findAllProperties(String knowledgePath, ISession session);
	
	public abstract ISession createSession();
	
	public Object getKnowledge(String knowledgePath, Class structure) throws KMException  {
		return  getKnowledge(knowledgePath, structure, null);
	}
	
	public void putKnowledge(String knowledgePath, Object value) throws KMException {
		putKnowledge(knowledgePath, value, null);
	}
	
	public Object takeKnowledge(String knowledgePath, Class structure) throws KMException {
		return takeKnowledge(knowledgePath, structure, null);
	}
	
	public Object getKnowledge(String knowledgePath) throws KMException {
		return  getKnowledge(knowledgePath, null);
	}
	
	public void putKnowledge(String knowledgePath) throws KMException {
		putKnowledge(knowledgePath, null);
	}
	
	public Object takeKnowledge(String knowledgePath) throws KMException {
		return takeKnowledge(knowledgePath, null);
	}
	
	public String [] findAllProperties(String knowledgePath) {
		return findAllProperties(knowledgePath, null);
	}
}
