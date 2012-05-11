package cz.cuni.mff.d3s.deeco.test;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;
import cz.cuni.mff.d3s.deeco.typeholders.MutableInteger;

@DEECoComponent
public class RobotFollowerComponent extends RootKnowledge {

	public MutableInteger battery;
	public Path path;
	public String convoyRobot; // 0 if there is no robot ahead 1 otherwise
	public Path[] crossingRobots;

	@DEECoInitialize
	public static RootKnowledge getInitialKnowledge() {
		RobotFollowerComponent k = new RobotFollowerComponent();
		k.battery = new MutableInteger(new Integer(100));
		k.path = new Path();
		k.path.currentPosition = 1;
		k.path.remainingPath = "2,3,4,5,6,7,8,9";
		k.convoyRobot = null;
		k.crossingRobots = null;
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(500)
	public static void process(@DEECoProcessInOut("path") Path path,
			@DEECoProcessInOut("battery") MutableInteger battery,
			@DEECoProcessIn("convoyRobot") String convoyRobot) {
		if (convoyRobot != null && path.remainingPath.length() > 0) {
			String[] fields = path.remainingPath.split(",");
			if (fields.length > 0) {
				path.currentPosition = Integer.parseInt(fields[0]);
				if (fields.length > 1) {
					path.remainingPath = path.remainingPath.substring(
							path.remainingPath.indexOf(",") + 1,
							path.remainingPath.length());
				} else {
					path.remainingPath = "";
				}
				battery.value = new Integer(battery.value - 1);
				System.out.println("Follower is moving: " + path.remainingPath);
			}
		}
	}
}
