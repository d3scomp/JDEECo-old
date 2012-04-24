package d3s.tuplespaces.components;

import java.rmi.RMISecurityManager;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.process.AbstractProcess;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class ComponentC extends Component {

	@Override
	protected void registerInTupleSpaces() {
		try {
			Transaction tx = TupleSpaceUtils.createTransaction();
			JavaSpace space = TupleSpaceUtils.getSpace();
			Tuple t = TupleSpaceUtils.createTuple("PropertyC", new Integer(0));
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
					Thread.sleep(1000L);// 1 second transaction
					writeOutputKnowledge(new Object[] { new Integer(1)}, tx);
					tx.commit();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		process.inputProperties = new String[] { "PropertyD" };
		process.outputProperties = new String[] { "PropertyC" };
		return process;
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());

		ComponentC c = new ComponentC();
	}

}
