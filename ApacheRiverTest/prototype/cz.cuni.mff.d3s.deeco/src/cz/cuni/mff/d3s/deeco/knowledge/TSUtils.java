package cz.cuni.mff.d3s.deeco.knowledge;

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;

public class TSUtils {
	protected final static Long DEFAULT_LEASE_TIMEOUT = 25000L; // 25 sec
	private static JavaSpace05 space = null;
	private static TransactionManager txManager = null;

	private static Object spaceLock = new Object();
	private static Object transactionLock = new Object();

	public static Transaction createTransaction() {
		synchronized (transactionLock) {
			try {
				if (txManager == null) {
					Lookup transactionLookup = new Lookup(
							TransactionManager.class);
					txManager = (TransactionManager) transactionLookup
							.getService();
				}
				Transaction.Created trc = TransactionFactory.create(
						txManager, DEFAULT_LEASE_TIMEOUT);
				return trc.transaction;
			} catch (Exception e) {
				System.out.println("ERROR - Transaction retrieval error: "
						+ e.getMessage());
				return null;
			}
		}
	}

	public static JavaSpace05 getSpace() {
		synchronized (spaceLock) {
			try {
				if (space == null) {
					Lookup lookup = new Lookup(JavaSpace05.class);
					space = (JavaSpace05) lookup.getService();
				}
				return space;
			} catch (Exception e) {
				System.out.println("ERROR - Space retrieval error: "
						+ e.getMessage());
				return null;
			}
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
