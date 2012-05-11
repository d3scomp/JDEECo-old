package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;

public class RepositoryKnowledgeManager extends KnowledgeManager {

	private KnowledgeRepository kr;

	public RepositoryKnowledgeManager(KnowledgeRepository kr) {
		this.kr = kr;
	}

	@Override
	public ISession createSession() {
		return kr.createSession();
	}

	@Override
	public Object getKnowledge(String knowledgePath, Class structure,
			ISession session) throws KMException {
		return retrieveKnowledge(false, knowledgePath, structure, session);
	}

	@Override
	public Object takeKnowledge(String knowledgePath, Class structure,
			ISession session) throws KMException {
		return retrieveKnowledge(true, knowledgePath, structure, session);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			Class structure = value.getClass();
			while (localSession.repeat()) {
				localSession.begin();
				if (KnowledgeManagerHelper.isHierarchical(structure)) {
					String innerPath, localKnowledgePath = knowledgePath;
					Field[] fields = structure.getFields();
					for (Field f : structure.getFields()) {
						innerPath = KnowledgePathBuilder.appendToRoot(
								localKnowledgePath, f.getName());
						putKnowledge(innerPath, f.get(value), localSession);
					}
				} else {
					Object currentValue = null;
					try {
						currentValue = kr.get(knowledgePath);
					} catch (UnavailableEntryException uee) {
					}
					if ((value != null && !value.equals(currentValue))
							|| (currentValue != null && !currentValue
									.equals(value))) {
						kr.take(knowledgePath, localSession);
						kr.put(knowledgePath, value, localSession);
					}
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
		} catch (KnowledgeRepositoryException kre) {
			throw new KMException(kre.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				session.cancel();
			} catch (SessionException se) {
			}
		}
	}

	@Override
	public String[] findAllProperties(String knowledgePath, ISession session) {
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			String[] result = null;
			while (localSession.repeat()) {
				localSession.begin();
				result = (String[]) kr.findAll(knowledgePath, localSession);
				if (session == null)
					localSession.end();
				else
					break;
			}
			return (result == null || result.length == 0) ? null : result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				session.cancel();
			} catch (SessionException se) {
			}
			return null;
		}
	}

	private Object retrieveKnowledge(boolean withdrawal, String knowledgePath,
			Class structure, ISession session) throws KMException {
		Object result = null;
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			while (localSession.repeat()) {
				localSession.begin();
				if (structure == null) {
					result = (withdrawal) ? kr
							.take(knowledgePath, localSession) : kr.get(
							knowledgePath, localSession);
				} else {
					result = structure.newInstance();
					String innerPath;
					Class innerStructure;
					for (Field f : structure.getFields()) {
						innerPath = KnowledgePathBuilder.appendToRoot(
								knowledgePath, f.getName());
						innerStructure = f.getType();
						if (KnowledgeManagerHelper
								.isHierarchical(innerStructure)) {
							f.set(result,
									retrieveKnowledge(withdrawal, innerPath,
											innerStructure, localSession));
						} else
							f.set(result,
									(withdrawal) ? kr.take(innerPath,
											localSession) : kr.get(innerPath,
											localSession));
					}
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			return result;
		} catch (IllegalArgumentException iae) {
			throw new KMIllegalArgumentException(iae.getMessage());
		} catch (UnavailableEntryException uee) {
			throw new KMNotExistentException(uee.getMessage());
		} catch (KnowledgeRepositoryException kre) {
			throw new KMException(kre.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				session.cancel();
			} catch (SessionException se) {
			}
			return null;
		}
	}
}
