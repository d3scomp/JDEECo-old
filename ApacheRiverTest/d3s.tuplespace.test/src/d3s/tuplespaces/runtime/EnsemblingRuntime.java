package d3s.tuplespaces.runtime;

import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;

import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace05;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class EnsemblingRuntime {
	protected static final long DEFAULT_PERIOD = 4000L;

	public static void main(String args[]) throws Exception {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());

		JavaSpace05 space = TupleSpaceUtils.getSpace();
		EventListener el = new EventListener(space);
		space.notify(TupleSpaceUtils.createTemplate("RobotId"), null, el.getStub(), Lease.FOREVER, new MarshalledObject<Integer>(new Integer(1)));
	}
}
