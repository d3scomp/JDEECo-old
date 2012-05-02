package d3s.deeco.prototype.launcher;

import d3s.deeco.prototype.knowledge.IKnowledgeManager;
import d3s.deeco.prototype.knowledge.KnowledgeManager;
import d3s.deeco.prototype.runtime.DEECoRuntime;
import d3s.deeco.prototype.test.ConvoyEnsemble;
import d3s.deeco.prototype.test.RobotFollowerComponent;
import d3s.deeco.prototype.test.RobotLeaderComponent;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class [] classes = {RobotLeaderComponent.class, RobotFollowerComponent.class};
		Class [] ensembles = {ConvoyEnsemble.class};
		IKnowledgeManager km = new KnowledgeManager();
		DEECoRuntime runtime = new DEECoRuntime(classes, ensembles, km);
	}

}
