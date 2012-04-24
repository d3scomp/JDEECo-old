package d3s.tuplespaces.components;

import java.rmi.RMISecurityManager;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.process.AbstractProcess;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class Observer extends Component {

	@Override
	protected void registerInTupleSpaces() {

	}

	@Override
	protected AbstractProcess createProcess() {
		AbstractProcess process = new AbstractProcess() {

			@Override
			public void step() {
				try {
					Transaction tx = TupleSpaceUtils.createTransaction();
					List<Tuple> inputKnowledge = readInputKnowledge(tx);
					System.out.println("New reading:");
					if (inputKnowledge != null) {
						for (Tuple t : inputKnowledge) {
							System.out.println(t.key + ": " + t.value);
						}
					}
					tx.commit();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		process.inputProperties = new String[] { "PropertyC", "PropertyD" };
		process.outputProperties = new String[] {};
		return process;
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());

		Observer c = new Observer();
	}

}
