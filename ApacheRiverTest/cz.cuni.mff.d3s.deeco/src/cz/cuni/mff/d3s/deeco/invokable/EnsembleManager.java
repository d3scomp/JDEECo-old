package cz.cuni.mff.d3s.deeco.invokable;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Class providing ensemble managing functionalities.
 * 
 * @author Michal Kit
 *
 */
public class EnsembleManager extends
		InvokableManager<SchedulableEnsembleProcess> {

	public EnsembleManager(KnowledgeManager km) {
		super(km);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.invokable.InvokableManager#addInvokable(java.lang.Class)
	 */
	@Override
	public void addInvokable(Class invokableDefinition) {
		if (invokableDefinition != null) {
			SchedulableEnsembleProcess invokable = SchedulableEnsembleProcess
					.extractEnsembleProcesses(invokableDefinition, km);
			invokables.add(invokable);
		}
	}

}
