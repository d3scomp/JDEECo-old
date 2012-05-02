package d3s.deeco.prototype.knowledge;

import java.util.List;

import d3s.deeco.prototype.interfaces.KnowledgeInterface;
import d3s.deeco.prototype.runtime.DEECoInvokableEnsemble;
import d3s.deeco.prototype.runtime.DEECoInvokableProcess;

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
	public Long generateUniqueKnowledgeId();
	
	
	/**
	 * Returns interface id from the knowledge reposytory.
	 * 
	 * @param path dot seperated property nesting
	 * @return id or -1 if not match has been found
	 */
	public Long getInterfaceIdFromPath(String path, Long rootId);
	
	
	/**
	 * Store interface in the distributed knowledge.
	 * 
	 * @param k interface that needs to be persisted
	 */
	public Long writeInterface(KnowledgeInterface k);
	
	/**
	 * Retrieve whole interface from the knowledge.
	 * 
	 * @param id unique identifier of the interface
	 * @return found interface
	 */
	public KnowledgeInterface readInterface(Long id);
	
	
	/**
	 * Writes the values to the distributed knowledge.
	 * 
	 * @param process requester of the method call
	 * @param values values that need to be stored
	 */
	public void writeProperties(DEECoInvokableProcess process, List<Object> values);
	
	
	/**
	 * Reads properties for the given process.
	 * 
	 * @param process requester of the method call
	 * @return values returned from the distributed knowledge
	 */
	public List<Object> readProperties(DEECoInvokableProcess process);
	
	/**
	 * Mark specified interface as a top level knowledge.
	 * 
	 * @param id interface id that needs to be marked as knowledge
	 */
	public void registerKnowledgeId(Long id);
	
	/**
	 * Unmark specified interface a top level knowledge.
	 * 
	 * @param id interface id that needs to be unmarked as knowledge
	 */
	public void unregisterKnowledge(Long id);
	
	public void performEnsembling(DEECoInvokableEnsemble iEnsemble);
}
