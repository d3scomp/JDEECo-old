package cz.cuni.mff.d3s.deeco.knowledge;

import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

public class TSKnowledgeRepository extends KnowledgeRepository {
	
	public TSKnowledgeRepository() {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
	}

	@Override
	public Object get(String entryKey, ISession session) throws UnavailableEntryException, KnowledgeRepositoryException {
		Tuple tuple = null;
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session).getTransaction() : null;
			tuple = (Tuple) space.readIfExists(TSUtils.createTemplate(entryKey), tx, Lease.FOREVER);
			if (tuple == null)
				throw new UnavailableEntryException("Entry " + entryKey + "unavailable!");
			return tuple.value;
		} catch (UnavailableEntryException uee) {
			throw uee;
		} catch (Exception e) {
			throw new KnowledgeRepositoryException("TSKnowledgeRepository error when reading property: "
							+ entryKey + " - "
							+ e.getMessage());
		}
	}
	
	@Override
	public void put(String entryKey, Object value, ISession session) throws KnowledgeRepositoryException {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session).getTransaction() : null;
			space.write(TSUtils.createTuple(entryKey, value), tx, Lease.FOREVER);
		} catch (Exception e) {
			throw new KnowledgeRepositoryException("TSKnowledgeRepository error when writing property: "
					+ entryKey + " - "
					+ e.getMessage());
		}
	}
	
	@Override
	public Object take(String entryKey, ISession session) throws KnowledgeRepositoryException {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session).getTransaction() : null;
			Tuple tuple = (Tuple) space.takeIfExists(TSUtils.createTemplate(entryKey), tx, Lease.FOREVER);
			return (tuple != null) ? tuple.value : null;
		} catch (Exception e) {
			throw new KnowledgeRepositoryException("TSKnowledgeRepository error when taking property: "
					+ entryKey + " - "
					+ e.getMessage());
		}
	}
	
	@Override
	public Object [] takeAll(String entryKey, ISession session) throws KnowledgeRepositoryException {
		try {
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session).getTransaction() : null;
			Collection<Tuple> tuples = space.take(Arrays.asList(new Tuple []{TSUtils.createTemplate(entryKey)}), tx, Lease.FOREVER, Long.MAX_VALUE);
			if (tuples.size() > 0) {
				Object [] result = new Object[tuples.size()];
				Iterator<Tuple> iterator = tuples.iterator();
				for (int i = 0; i < tuples.size(); i++) {
					result[i] = iterator.next().value;
				}
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new KnowledgeRepositoryException("TSKnowledgeRepository error when takingAll properties: "
					+ entryKey + " - "
					+ e.getMessage());
		}
	}

	@Override
	public Object[] findAll(String entryKey, ISession session) throws KnowledgeRepositoryException {
		try {
			List<Object> resultList = new ArrayList<Object>();
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session).getTransaction() : null;
			MatchSet tuples = space.contents(Arrays.asList(new Tuple []{TSUtils.createTemplate(entryKey)}), tx, Lease.FOREVER, Long.MAX_VALUE);
			Tuple tuple = (Tuple) tuples.next();
			while (tuple != null) {
				resultList.add(tuple.value);
				tuples.next();
			}
			return (resultList.size() > 0) ? resultList.toArray() : null;
		} catch (Exception e) {
			throw new KnowledgeRepositoryException("TSKnowledgeRepository error when readingAll properties: "
					+ entryKey + " - "
					+ e.getMessage());
		}
	}

	@Override
	public ISession createSession() {
		return new TransactionalSession();
	}
}
