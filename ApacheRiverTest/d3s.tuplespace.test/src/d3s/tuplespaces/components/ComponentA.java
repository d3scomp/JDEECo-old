package d3s.tuplespaces.components;

import java.rmi.RMISecurityManager;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.process.AbstractProcess;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class ComponentA extends Component {

	@Override
	protected void registerInTupleSpaces() {
		try {
			Transaction tx = TupleSpaceUtils.createTransaction();
			JavaSpace space = TupleSpaceUtils.getSpace();
			Tuple t = TupleSpaceUtils.createTuple("RobotId", "A");
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("A.path", "9,8,7,6,5,4,3,2");
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("A.battery", new Integer(11));
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("A.others", null);
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
				List<Tuple> inputKnowledge = readInputKnowledge();
				if (inputKnowledge != null && inputKnowledge.size() == 3) {
					Object others = inputKnowledge.get(0).value;
					if (others != null) {
						String path = (String) inputKnowledge.get(1).value;
						Integer battery = (Integer) inputKnowledge.get(2).value;
						battery--;
						if (path.length() > 2)
							path = path.substring(2);
						else
							path = "";
						System.out.println("Moving robot A: " + path);
						writeOutputKnowledge(new Object[]{path, battery});
					} else {
						System.out.println("A: No other robots");
					}
					
				}
			}
		};
		process.inputProperties = new String[]{"A.others", "A.path", "A.battery"};
		process.outputProperties = new String[]{"A.path", "A.battery"};
		return process;
	}
	
	public static void main(String args[]) {
		if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
		
		ComponentA c = new ComponentA();
	}

}
