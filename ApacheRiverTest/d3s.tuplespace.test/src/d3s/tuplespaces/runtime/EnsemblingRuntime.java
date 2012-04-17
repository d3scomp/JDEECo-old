package d3s.tuplespaces.runtime;

import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

import d3s.tuplespaces.knowledge.Tuple;
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
