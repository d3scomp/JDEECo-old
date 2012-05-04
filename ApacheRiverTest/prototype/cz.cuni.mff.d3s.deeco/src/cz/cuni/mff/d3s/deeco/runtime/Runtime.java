package cz.cuni.mff.d3s.deeco.runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.InvokableEnsemble;
import cz.cuni.mff.d3s.deeco.invokable.InvokableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;


public class Runtime {

	private List<InvokableProcess> knowledgeProcesses;
	private List<InvokableEnsemble> ensembleProcesses;
	private IKnowledgeManager km;

	public Runtime(Class[] components, Class[] ensembles,
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
			ensembleProcesses = new ArrayList<InvokableEnsemble>();
			for (Class c : ensembles) {
				ensembleProcesses.add(ClassParser.getEnsemble(c, km));
			}
			startEnsembles();
		}
	}
	
	private void startEnsembles() {
		for (InvokableEnsemble ie: ensembleProcesses) {
			ie.start();
		}
	}

	private void processComponents(Class[] components)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (components != null && components.length > 0) {
			knowledgeProcesses = new ArrayList<InvokableProcess>();
			Knowledge initialKnowledge;
			for (Class c : components) {
				initialKnowledge = (Knowledge) ClassParser
						.getInitMethod(c).invoke(null, new Object[] {});
				km.writeKnowledge(initialKnowledge);
				km.registerRootKnowledge(initialKnowledge.id);
				knowledgeProcesses.addAll(ClassParser.getProcess(c,
						initialKnowledge.id, km));
			}
			startProcesses();
		}
	}
	
	/*
	 * Start processes executions
	 */
	private void startProcesses() {
		for (InvokableProcess ip : knowledgeProcesses) {
			ip.start();
		}
	}
}
