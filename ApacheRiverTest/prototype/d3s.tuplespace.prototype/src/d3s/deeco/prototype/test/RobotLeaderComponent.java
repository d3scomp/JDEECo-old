package d3s.deeco.prototype.test;

import d3s.deeco.prototype.annotations.DEECoComponent;
import d3s.deeco.prototype.annotations.DEECoInitialKnowledge;
import d3s.deeco.prototype.annotations.DEECoInitialize;
import d3s.deeco.prototype.annotations.DEECoProcess;
import d3s.deeco.prototype.annotations.DEECoScheduling;
import d3s.deeco.prototype.annotations.SchedulingType;
import d3s.deeco.prototype.interfaces.Interface;
import d3s.deeco.prototype.interfaces.Path;

@DEECoComponent
public class RobotLeaderComponent {
	
	@DEECoInitialKnowledge
	class InitialKnowledge extends Interface {
		Integer battery;
		Path path;
		Integer convoyRobot; //0 if there is no robot ahead 1 otherwise
		Path [] crossingRobots;
	}
	
	@DEECoInitialize
	public static void init(InitialKnowledge k) {
		k.battery = 100;
		k.path = new Path();
		k.path.currentPosition = 12;
		k.path.remainingPath = "0,1,2,3,4,5,6,7,8,9";
		k.convoyRobot = 0;
		k.crossingRobots = null;
	}
	
	/*
	 * Input: path, crossingRobots, convoyRobot
	 * Output: path
	 * 
	 */
	@DEECoScheduling(interval=500, type=SchedulingType.PERIODIC)
	@DEECoProcess(input="path, crossingRobots, convoyRobot", output="path")
	public static Object [] process(Path p, Path [] crossingR, Integer convoyR) {
		if (convoyR != 1) {
			String [] fields = p.remainingPath.split(",");
			if (fields.length > 0) {
				p.currentPosition = Integer.parseInt(fields[0]);
				if (fields.length > 1) {
					p.remainingPath = p.remainingPath.substring(p.remainingPath.indexOf(","),p.remainingPath.length());
				} else {
					p.remainingPath = "";
				}
			}
		}
		return new Object[]{p};
	}
}
