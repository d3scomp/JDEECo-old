package d3s.tuplespaces.process;

import java.util.ArrayList;
import java.util.List;

import d3s.tuplespaces.knowledge.Tuple;
import d3s.tuplespaces.util.TupleSpaceUtils;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

public abstract class AbstractProcess {

	protected List<Tuple> readInputKnowledge(Transaction tx) {
		try {
			List<Tuple> result = new ArrayList<Tuple>();
			JavaSpace space = TupleSpaceUtils.getSpace();
			Tuple t;
			for (String pn : inputProperties) {
				t = (Tuple) space.readIfExists(TupleSpaceUtils.createTemplate(pn), tx,
						JavaSpace.NO_WAIT);
				if (t != null)
					result.add(t);
//				else
//					return null;
			}
			return result;
		} catch (Exception ex) {
			System.out.println("ERROR - Error when reading knowledge.");
			System.out.println(ex.getMessage());
		}
		return null;
	}

	protected boolean writeOutputKnowledge(Object outputValues[], Transaction tx) {
		if (outputProperties != null && outputValues != null
				&& outputValues.length == outputProperties.length) {
			try {
				JavaSpace space = TupleSpaceUtils.getSpace();
				String key;
				for (int i = 0; i < outputProperties.length; i++) {
					key = outputProperties[i];
					if (space.takeIfExists(TupleSpaceUtils.createTemplate(key), tx, Lease.FOREVER) != null)
						space.write(TupleSpaceUtils.createTuple(key, outputValues[i]), tx, Lease.FOREVER);
					else {
						return false;
					}
				}
			} catch (Exception ex) {
				System.out.println("ERROR - Error when writing knowledge.");
				System.out.println(ex.getMessage());
			}
		}
		return true;
	}

	public String [] inputProperties; // array of input property names
	public String [] outputProperties; // array of output property names

	public abstract void step();
}
