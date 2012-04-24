package d3s.deeco.prototype.runtime;

import java.lang.reflect.Method;

public class DEECoProcessInstance {
	
	private DEECoRuntime runtime;
	private Thread thread;
	
	public String [] inputParameters;
	public String [] outputParameters;
	public Method method;
	
	public DEECoProcessInstance(DEECoRuntime runtime) {
		this.runtime = runtime;
	}
	
	public void startProcess() {
		
	}
	
	public void stopProcess() {
		
	}
}
