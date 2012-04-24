package d3s.tuplespaces.components;

import d3s.tuplespaces.process.AbstractProcess;

public abstract class Component {
	
	protected static final long DEFAULT_PERIOD = 4000L;
	
	public Component() {
		registerInTupleSpaces();
		startProcess(createProcess());
	}
	
	private void startProcess(final AbstractProcess process) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					while (true) {
						process.step();
						Thread.sleep(DEFAULT_PERIOD);
					}
				} catch (Exception e) {
					System.out.println("Interrupted");
				}
			}
		});
		thread.start();
	}
	
	protected abstract void registerInTupleSpaces();
	protected abstract AbstractProcess createProcess();
}
