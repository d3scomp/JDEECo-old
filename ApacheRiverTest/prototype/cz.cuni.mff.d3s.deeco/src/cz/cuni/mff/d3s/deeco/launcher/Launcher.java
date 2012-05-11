package cz.cuni.mff.d3s.deeco.launcher;

import cz.cuni.mff.d3s.deeco.demo.ConvoyEnsemble;
import cz.cuni.mff.d3s.deeco.demo.RobotFollowerComponent;
import cz.cuni.mff.d3s.deeco.demo.RobotLeaderComponent;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class [] classes = {RobotLeaderComponent.class, RobotFollowerComponent.class};
		Class [] ensembles = {ConvoyEnsemble.class};
		KnowledgeManager km = new RepositoryKnowledgeManager(new TSKnowledgeRepository());
		Runtime runtime = new Runtime(classes, ensembles, km);
	}

}
