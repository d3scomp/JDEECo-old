package d3s.deeco.prototype.test;

import d3s.deeco.prototype.annotations.DEECoComponent;
import d3s.deeco.prototype.annotations.DEECoInitialize;
import d3s.deeco.prototype.annotations.DEECoInput;
import d3s.deeco.prototype.annotations.DEECoInputOutput;
import d3s.deeco.prototype.annotations.DEECoPeriodicScheduling;
import d3s.deeco.prototype.annotations.DEECoProcess;
import d3s.deeco.prototype.knowledge.Knowledge;
import d3s.deeco.prototype.primitives.MutableInteger;

@DEECoComponent
public class RobotFollowerComponent extends Knowledge {

	public MutableInteger battery;
	public Path path;
	public Long convoyRobot; // 0 if there is no robot ahead 1 otherwise
	public Path[] crossingRobots;

	@DEECoInitialize
	public static Knowledge getInitialKnowledge() {
		RobotFollowerComponent k = new RobotFollowerComponent();
		k.battery = new MutableInteger(new Integer(100));
		k.path = new Path();
		k.path.currentPosition = 1;
		k.path.remainingPath = "2,3,4,5,6,7,8,9";
		k.convoyRobot = 0L;
		k.crossingRobots = null;
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(interval = 500)
	public static void process(@DEECoInputOutput(name = "path") Path path,
			@DEECoInputOutput(name = "battery") MutableInteger battery,
			@DEECoInput(name = "convoyRobot") Long convoyRobot) {
		if (convoyRobot != 0 && path.remainingPath.length() > 0) {
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
	
	//------------------------- Getters and Setters -------------------------------------------

	public MutableInteger getBattery() {
		return battery;
	}

	public void setBattery(MutableInteger battery) {
		this.battery = battery;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Long getConvoyRobot() {
		return convoyRobot;
	}

	public void setConvoyRobot(Long convoyRobot) {
		this.convoyRobot = convoyRobot;
	}

	public Path[] getCrossingRobots() {
		return crossingRobots;
	}

	public void setCrossingRobots(Path[] crossingRobots) {
		this.crossingRobots = crossingRobots;
	}
}
