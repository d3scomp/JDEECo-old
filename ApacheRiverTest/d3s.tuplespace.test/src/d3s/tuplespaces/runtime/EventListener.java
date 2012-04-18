package d3s.tuplespaces.runtime;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
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

class EventListener implements RemoteEventListener {

    /**
	 * 
	 */
	private RemoteEventListener theStub;
    private JavaSpace05 space;

    EventListener(JavaSpace05 space) throws RemoteException {
        Exporter myDefaultExporter = 
            new BasicJeriExporter(TcpServerEndpoint.getInstance(0),
                                  new BasicILFactory(), false, true);

        theStub = (RemoteEventListener) myDefaultExporter.export(this);
        this.space = space;
    }

    public RemoteEventListener getStub() {
        return theStub;
    }

    public void notify(RemoteEvent anEvent) {
        try {
        	List<String> rIds = new ArrayList<String>();
			MatchSet readResult = space.contents(
					Arrays.asList(TupleSpaceUtils.createTemplate("RobotId")), null,
					Lease.FOREVER, 100L);
			Tuple t;
			while ((t = (Tuple) readResult.next()) != null) {
				rIds.add((String) t.value);
			}
			System.out.println("notified");
			if (rIds.size() > 1) {
				Transaction tx = TupleSpaceUtils
						.createTransaction();
				String id;
				for (Integer i = 0; i < rIds.size(); i++) {
					id = rIds.get(i);
					t = TupleSpaceUtils.createTemplate(id
							+ ".others");
					t = (Tuple) space.takeIfExists(t, tx,
							Lease.FOREVER);
					if (t != null) {
						t.value = new ArrayList<String>(
								rIds);
						((List<String>) t.value).remove(i);
						space.write(t, tx, Lease.FOREVER);
					}
				}
				tx.commit();
			}
        } catch (Exception anE) {
            System.out.println("Got event but couldn't display it");
            anE.printStackTrace(System.out);
        }
    }
}
