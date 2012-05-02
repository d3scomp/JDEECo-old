package d3s.deeco.prototype.scheduling;

import d3s.deeco.prototype.staticTypes.EnumScheduling;

public class DEECoProcessPeriodicSchedule implements DEECoProcessSchedule {
	public long interval;
	
	public DEECoProcessPeriodicSchedule() {
		this.interval = 1000;
	}
	
	public DEECoProcessPeriodicSchedule(long interval) {
		this.interval = interval;
	}
}
