package d3s.tuplespaces.components;

import java.rmi.RMISecurityManager;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.process.AbstractProcess;
import d3s.tuplespaces.util.TupleSpaceUtils;

public class ComponentB extends Component {

	@Override
	protected void registerInTupleSpaces() {
		try {
			Transaction tx = TupleSpaceUtils.createTransaction();
			JavaSpace space = TupleSpaceUtils.getSpace();
			Tuple t = TupleSpaceUtils.createTuple("RobotId", "B");
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("B.path", "1,2,3,4,5,6,7,8");
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("B.battery", new Integer(56));
			space.write(t, tx, Lease.FOREVER);
			t = TupleSpaceUtils.createTuple("B.others", null);
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
						System.out.println("Moving robot B: " + path);
						writeOutputKnowledge(new Object[]{path, battery});
					} else {
						System.out.println("B: No other robots");
					}
					
				}
			}
		};
		process.inputProperties = new String[]{"B.others", "B.path", "B.battery"};
		process.outputProperties = new String[]{"B.path", "B.battery"};
		return process;
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
		
		ComponentB c = new ComponentB();
	}
	
}
