package cz.cuni.mff.d3s.deeco.invokable;

import cz.cuni.mff.d3s.deeco.scheduling.DEECoProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.DEECoProcessSchedule;

public abstract class Invokable {
	
	public DEECoProcessSchedule scheduling;
	private Thread processThread;
	
	protected abstract void invoke();
	
	public void start() {
		final DEECoProcessPeriodicSchedule s = (DEECoProcessPeriodicSchedule) scheduling;
		if (s != null) {
			if (processThread != null)
				stop();
			processThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							invoke();
							Thread.sleep(s.interval);
						}
					} catch (Exception e) {
						System.out.println("ERROR - Process execution error: "
								+ e.getMessage());
					}
				}
			});
			processThread.start();
		}
	}

	/*
	 * To be changed later
	 */
	public void stop() {
		if (processThread != null) {
			processThread.interrupt();
		}
	}
}
