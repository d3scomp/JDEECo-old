package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Generic class used to provide common functionalities for all
 * {@link SchedulableProcess}.
 * 
 * @author Michal Kit
 * 
 * @param class extending {@link SchedulableProcess} - either
 *        {@link SchedulableKnowledgeProcess} or
 *        {@link SchedulableEnsembleProcess}
 */
public abstract class InvokableManager<T extends SchedulableProcess> {

	protected List<T> invokables;
	protected KnowledgeManager km;

	public InvokableManager(KnowledgeManager km) {
		invokables = new ArrayList<T>();
		this.km = km;
	}

	/**
	 * Adds a schedulable process to the managed list.
	 * 
	 * @param invokableDefinition
	 *            class having a definitions of potential schedulable processes.
	 */
	public abstract void addInvokable(Class invokableDefinition);

	/**
	 * Starts the execution of all schedulable processes.
	 */
	public void startInvokables() {
		for (T i : invokables) {
			i.start();
		}
	}

	/**
	 * Stops the execution of all schedulable processes.
	 */
	public void stopInvokables() {
		for (T i : invokables) {
			i.stop();
		}
	}
}
