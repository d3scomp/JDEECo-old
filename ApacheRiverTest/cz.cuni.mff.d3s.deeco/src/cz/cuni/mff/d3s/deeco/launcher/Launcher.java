package cz.cuni.mff.d3s.deeco.launcher;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.test.ConvoyEnsemble;
import cz.cuni.mff.d3s.deeco.test.RobotFollowerComponent;
import cz.cuni.mff.d3s.deeco.test.RobotLeaderComponent;

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
