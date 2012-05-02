package d3s.deeco.prototype.runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import d3s.deeco.prototype.knowledge.IKnowledgeManager;
import d3s.deeco.prototype.knowledge.Knowledge;

public class DEECoRuntime {

	private List<DEECoInvokableProcess> knowledgeProcesses;
	private List<DEECoInvokableEnsemble> ensembleProcesses;
	private IKnowledgeManager km;

	public DEECoRuntime(Class[] components, Class[] ensembles,
			IKnowledgeManager km) {
		try {
			this.km = km;
			processComponents(components);
			processEnsembles(ensembles);
		} catch (Exception e) {
			System.out.println("ERROR - Runtime initialization exception: "
					+ e.getMessage());
		}
	}
	
	private void processEnsembles(Class [] ensembles) {
		if (ensembles != null && ensembles.length > 0) {
			ensembleProcesses = new ArrayList<DEECoInvokableEnsemble>();
			for (Class c : ensembles) {
				ensembleProcesses.add(DEECoClassParser.getEnsemble(c, km));
			}
			startEnsembles();
		}
	}
	
	private void startEnsembles() {
		for (DEECoInvokableEnsemble ie: ensembleProcesses) {
			ie.start();
		}
	}

	private void processComponents(Class[] components)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (components != null && components.length > 0) {
			knowledgeProcesses = new ArrayList<DEECoInvokableProcess>();
			Knowledge initialKnowledge;
			for (Class c : components) {
				initialKnowledge = (Knowledge) DEECoClassParser
						.getInitMethod(c).invoke(null, new Object[] {});
				km.writeInterface(initialKnowledge);
				km.registerKnowledgeId(initialKnowledge.id);
				knowledgeProcesses.addAll(DEECoClassParser.getProcess(c,
						initialKnowledge.id, km));
			}
			startProcesses();
		}
	}
	
	/*
	 * Start processes executions
	 */
	private void startProcesses() {
		for (DEECoInvokableProcess ip : knowledgeProcesses) {
			ip.start();
		}
	}
}
