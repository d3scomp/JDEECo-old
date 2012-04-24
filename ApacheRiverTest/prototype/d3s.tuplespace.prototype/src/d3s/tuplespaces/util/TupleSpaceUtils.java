package d3s.tuplespaces.util;

import d3s.tuplespaces.knowledge.Tuple;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;

public class TupleSpaceUtils {
	protected final static Long DEFAULT_LEASE_TIMEOUT = 120000L; // 2 min
	private static JavaSpace05 space = null;
	private static TransactionManager txManager = null;
	
	private static Object spaceLock = new Object();
	private static Object transactionLock = new Object();

	public static Transaction createTransaction() throws Exception {
		synchronized (transactionLock) {
			if (txManager == null) {
				Lookup transactionLookup = new Lookup(TransactionManager.class);
				txManager = (TransactionManager) transactionLookup.getService();
			}
			Transaction.Created trc = TransactionFactory.create(txManager,
					DEFAULT_LEASE_TIMEOUT);
			return trc.transaction;
		}
	}

	public static JavaSpace05 getSpace() throws Exception {
		synchronized (spaceLock) {
			if (space == null) {
				Lookup lookup = new Lookup(JavaSpace05.class);
				space = (JavaSpace05) lookup.getService();
			}
			return space;
		}
	}
	
	public static Tuple createTuple(String key, Object value) {
		Tuple result = new Tuple();
		result.key = key;
		result.value = value;
		return result;
	}
	
	public static Tuple createTemplate(String key) {
		Tuple result = new Tuple();
		result.key = key;
		return result;
	}
}
