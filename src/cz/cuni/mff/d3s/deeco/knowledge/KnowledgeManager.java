package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;

/**
 * An abstract class providing higher level interface for accessing the
 * knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeManager {

	/**
	 * Retrieves knowledge from the knowledge repository, defined by both
	 * <code>knowledgePath</code> and <code>structure</code>. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the retrieval should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract Object getKnowledge(String knowledgePath, Class structure,
			ISession session) throws KMException;

	/**
	 * Puts knowledge object to the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @param session
	 *            a session object within which all the put operations should be
	 *            performed
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException;

	/**
	 * Withdraws the knowledge from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the withdrawals should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract Object takeKnowledge(String knowledgePath, Class structure,
			ISession session) throws KMException;

	/**
	 * Finds all the flat (not hierarchical structures) properties in the
	 * knowledge repository. This method is session oriented.
	 * 
	 * @param knowledgePath
	 *            absolute name (knowledge path) of the property
	 * @param session
	 *            a session object within which finding should be performed
	 * @return an array of matched objects
	 */
	public abstract String[] findAllProperties(String knowledgePath,
			ISession session);

	/**
	 * Creates {@link ISession} instance used for enclosing all knowledge
	 * operations within a single session.
	 * 
	 * @return {@link ISession} object instance.
	 */
	public abstract ISession createSession();

	/**
	 * Retrieves knowledge from the knowledge repository, defined by both
	 * <code>knowledgePath</code> and <code>structure</code>.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath, Class structure)
			throws KMException {
		return getKnowledge(knowledgePath, structure, null);
	}

	/**
	 * Puts knowledge object to the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public void putKnowledge(String knowledgePath, Object value)
			throws KMException {
		putKnowledge(knowledgePath, value, null);
	}

	/**
	 * Withdraws the knowledge from the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object takeKnowledge(String knowledgePath, Class structure)
			throws KMException {
		return takeKnowledge(knowledgePath, structure, null);
	}

	/**
	 * Retrieves knowledge from the knowledge repository, defined by
	 * <code>knowledgePath</code>.
	 * 
	 * @param absolute
	 *            name (knowledge path) of the property to be retrieved
	 * @return retrieved knowledge object
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath) throws KMException {
		return getKnowledge(knowledgePath, null);
	}

	public Object takeKnowledge(String knowledgePath) throws KMException {
		return takeKnowledge(knowledgePath, null);
	}

	/**
	 * Finds all the flat (not hierarchical structures) properties in the
	 * knowledge repository.
	 * 
	 * @param knowledgePath
	 *            absolute name (knowledge path) of the property
	 * @return an array of matched objects
	 */
	public String[] findAllProperties(String knowledgePath) {
		return findAllProperties(knowledgePath, null);
	}
}
