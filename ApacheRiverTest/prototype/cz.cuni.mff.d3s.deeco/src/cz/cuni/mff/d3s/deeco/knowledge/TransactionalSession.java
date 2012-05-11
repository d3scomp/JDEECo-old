package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import net.jini.core.transaction.Transaction;

public class TransactionalSession implements ISession {

	private static final int MAX_REPETITIONS = 5;

	private Transaction tx;
	private boolean succeeded = false;
	private int count = MAX_REPETITIONS;

	@Override
	public void begin() {
		if (tx != null)
			try {
				cancel();
			} catch (SessionException se) {}
		;
		tx = TransactionUtils.createTransaction();
	}

	@Override
	public void end() throws SessionException {
		if (tx != null)
			try {
				count--;
				tx.commit();
				succeeded = true;
			} catch (Exception e) {
			}
		else
			throw new SessionException("Session not started!");
	}

	@Override
	public void cancel() throws SessionException {
		if (tx != null)
			if (!succeeded)
				try {
					tx.abort();
				} catch (Exception e) {
				}
			else
				throw new SessionException("Session has already ended!");
	}

	@Override
	public boolean repeat() {
		return !succeeded && count > 0;
	}

	public Transaction getTransaction() {
		return tx;
	}

	@Override
	public boolean hasSucceeded() {
		return succeeded;
	}

}
