package d3s.tuplespaces.components;

import java.rmi.RMISecurityManager;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.process.AbstractProcess;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class ComponentD extends Component {

	@Override
	protected void registerInTupleSpaces() {
		try {
			Transaction tx = TupleSpaceUtils.createTransaction();
			JavaSpace space = TupleSpaceUtils.getSpace();
			Tuple t = TupleSpaceUtils.createTuple("PropertyD", new Integer(1));
			space.write(t, tx, Lease.FOREVER);
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - when initializing a component!");
		}
	}

	@Override
	protected AbstractProcess createProcess() {
		AbstractProcess process = new AbstractProcess() {
			@Override
			public void step() {
				try {
					Transaction tx = TupleSpaceUtils.createTransaction();
					readInputKnowledge(tx);
					Thread.sleep(10000L); //10 seconds transaction
					writeOutputKnowledge(new Object[] { new Integer(0) }, tx);
					tx.commit();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		process.inputProperties = new String[] { "PropertyC" };
		process.outputProperties = new String[] { "PropertyD" };
		return process;
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());

		ComponentD c = new ComponentD();
	}

}
