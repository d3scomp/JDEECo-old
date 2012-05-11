package cz.cuni.mff.d3s.deeco.scheduling;


public class ProcessPeriodicSchedule implements ProcessSchedule {
	public long interval;
	
	public ProcessPeriodicSchedule() {
		this.interval = 1000;
	}
	
	public ProcessPeriodicSchedule(long interval) {
		this.interval = interval;
	}
}
