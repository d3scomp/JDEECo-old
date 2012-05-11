package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.invokable.ComponentManager;
import cz.cuni.mff.d3s.deeco.invokable.EnsembleManager;
import cz.cuni.mff.d3s.deeco.invokable.InvokableManager;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class Runtime {

	private InvokableManager<? extends SchedulableProcess> ensembleManager;
	private InvokableManager<? extends SchedulableProcess> componentManager;

	public Runtime(Class[] components, Class[] ensembles,
		KnowledgeManager km) {
		this.componentManager = new ComponentManager(km);
		this.ensembleManager = new EnsembleManager(km);
	}

	public synchronized void startRuntime() {
		componentManager.startInvokables();
		ensembleManager.startInvokables();
	}

	public synchronized void stopRuntime() {
		componentManager.stopInvokables();
		ensembleManager.stopInvokables();
	}
}
