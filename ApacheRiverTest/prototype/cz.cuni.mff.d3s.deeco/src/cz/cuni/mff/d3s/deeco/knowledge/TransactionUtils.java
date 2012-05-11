package cz.cuni.mff.d3s.deeco.knowledge;

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;

public class TransactionUtils {
	protected final static Long DEFAULT_LEASE_TIMEOUT = 10000L; // 10 sec
	private static TransactionManager txManager = null;
	
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
}
