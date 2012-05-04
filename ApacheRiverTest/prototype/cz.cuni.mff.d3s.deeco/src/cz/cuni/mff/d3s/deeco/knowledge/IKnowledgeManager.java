package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

import net.jini.core.transaction.Transaction;

import cz.cuni.mff.d3s.deeco.invokable.InvokableEnsemble;
import cz.cuni.mff.d3s.deeco.invokable.InvokableProcess;


/**
 * @author Dell
 *
 */
public interface IKnowledgeManager {
	
	/**
	 * Generates identifier which is unique among the distributed application.
	 * 
	 * @return Unique identifier;
	 */
	public String generateUniqueRootKnowledgeId();
	
	
	/**
	 * Returns interface id from the knowledge reposytory.
	 * 
	 * @param path dot seperated property nesting
	 * @return id or -1 if not match has been found
	 */
	public Long getKnowledgeIdFromPath(String path, Long rootId);
	
	
	/**
	 * Store interface in the distributed knowledge.
	 * 
	 * @param k interface that needs to be persisted
	 */
	public void writeKnowledge(Knowledge k);
	
	public void writeKnowledge(Knowledge k, Transaction tx);
	
	/**
	 * Retrieve whole interface from the knowledge.
	 * 
	 * @param id unique identifier of the interface
	 * @return found interface
	 */
	public Knowledge readKnowledge(String id);
	
	public Knowledge readKnowledge(String id, Transaction tx);
	/**
	 * Writes the values to the distributed knowledge.
	 * 
	 * @param process requester of the method call
	 * @param values values that need to be stored
	 */
	public void writeProperties(InvokableProcess process, List<Object> values);
	
	public void writeProperties(InvokableProcess process, List<Object> values, Transaction tx);
	
	/**
	 * Reads properties for the given process.
	 * 
	 * @param process requester of the method call
	 * @return values returned from the distributed knowledge
	 */
	public List<Object> readProperties(InvokableProcess process);
	
	public List<Object> readProperties(InvokableProcess process, Transaction tx);
	
	/**
	 * Mark specified interface as a top level knowledge.
	 * 
	 * @param id interface id that needs to be marked as knowledge
	 */
	public void registerRootKnowledge(String id);
	
	/**
	 * Unmark specified interface a top level knowledge.
	 * 
	 * @param id interface id that needs to be unmarked as knowledge
	 */
	public void unregisterKnowledge(Long id);
	
	public void performEnsembling(InvokableEnsemble iEnsemble);
}
