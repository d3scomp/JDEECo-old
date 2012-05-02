package d3s.deeco.prototype.runtime;

import d3s.deeco.prototype.scheduling.DEECoProcessPeriodicSchedule;
import d3s.deeco.prototype.scheduling.DEECoProcessSchedule;

public abstract class DEECoInvokable {
	
	protected DEECoProcessSchedule scheduling;
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
