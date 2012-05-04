package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.staticTypes.EnumScheduling;

public class DEECoProcessPeriodicSchedule implements DEECoProcessSchedule {
	public long interval;
	
	public DEECoProcessPeriodicSchedule() {
		this.interval = 1000;
	}
	
	public DEECoProcessPeriodicSchedule(long interval) {
		this.interval = interval;
	}
}
