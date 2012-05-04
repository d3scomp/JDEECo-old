package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.ducktype.DuckType;
import cz.cuni.mff.d3s.deeco.invokable.InvokableEnsemble;
import cz.cuni.mff.d3s.deeco.invokable.InvokableProcess;
import cz.cuni.mff.d3s.deeco.properties.DEECoProperty;
import cz.cuni.mff.d3s.deeco.staticTypes.ConstantKeys;

public class KnowledgeManager implements IKnowledgeManager {

	public KnowledgeManager() {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		initializeTupleSpace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#generateUniqueKnowledgeId
	 * ()
	 */
	@Override
	public String generateUniqueRootKnowledgeId() {
		try {
			Long value = null;
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = TSUtils.createTransaction();
			Tuple template = TSUtils
					.createTemplate(ConstantKeys.DISTRIBUTED_UNIQUE_ID);
			Tuple uniqueId = (Tuple) space.readIfExists(template, tx, 1000);
			if (uniqueId != null) {
				value = ((Long) uniqueId.value);
				value++;
				uniqueId.value = value;
				space.takeIfExists(template, tx, 1000);
				space.write(uniqueId, tx, Lease.FOREVER);
			}
			tx.commit(); // needs to repeat in case of failure
			return value.toString();
		} catch (Exception e) {
			System.out.println("ERROR - Unique id generation error: "
					+ e.getMessage());
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#writeInterface(d3s.deeco
	 * .prototype.interfaces.Interface)
	 */
	@Override
	public void writeKnowledge(Knowledge k) {
		writeKnowledge(k, null);
	}

	@Override
	public void writeKnowledge(Knowledge k, Transaction tx) {
		boolean newKnowledge = k.id == null;
		if (newKnowledge) {
			k.id = generateUniqueRootKnowledgeId();
		}
		if (k.id != null) {
			try {
				Class c = k.getClass();
				JavaSpace space = TSUtils.getSpace();
				Transaction localTx = (tx == null) ? TSUtils
						.createTransaction() : tx;
				String tId = k.id + "."
						+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME;
				Tuple tuple = (Tuple) space.readIfExists(
						TSUtils.createTemplate(tId), localTx, Lease.FOREVER);
				if (tuple != null && !((Class) tuple.value).equals(c)) {
					space.takeIfExists(TSUtils.createTemplate(tId), localTx,
							Lease.FOREVER);
				}
				space.write(TSUtils.createTuple(tId, c), localTx, Lease.FOREVER);
				Knowledge fieldKnowledge;
				Object newValue;
				for (Field f : c.getFields()) {
					/*
					 * We check if the field is the type of Interface. If so we
					 * call writeInterface recursively. Otherwise we simply
					 * write a new value to the space.
					 */
					tId = k.id + "." + f.getName();
					if (Knowledge.class.isAssignableFrom(f.getType())) {
						fieldKnowledge = (Knowledge) f.get(k);
						if (fieldKnowledge.id == null)
							fieldKnowledge.id = tId;
						writeKnowledge((Knowledge) f.get(k), (tx == null) ? null : localTx);
					} else {
						tuple = (Tuple) space.readIfExists(
								TSUtils.createTemplate(tId), localTx,
								Lease.FOREVER);
						newValue = f.get(k);
						if ((tuple == null)
								|| (tuple.value != null && tuple.value
										.equals(newValue))
								|| (newValue != null && !newValue
										.equals(tuple.value))) {
							space.takeIfExists(TSUtils.createTemplate(tId),
									localTx, Lease.FOREVER);
							space.write(TSUtils.createTuple(tId, newValue),
									localTx, Lease.FOREVER);
						}
					}
				}
				if (tx == null)
					localTx.commit();
			} catch (Exception e) {
				System.out.println("ERROR - writing interface error: "
						+ e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#readInterface(java.lang
	 * .Long)
	 */
	@Override
	public Knowledge readKnowledge(String id) {
		return readKnowledge(id, null);
	}

	@Override
	public Knowledge readKnowledge(String id, Transaction tx) {
		try {
			Tuple tuple;
			Knowledge result = null;
			JavaSpace space = TSUtils.getSpace();
			Transaction localTx = (tx == null) ? TSUtils.createTransaction()
					: tx;
			tuple = (Tuple) space.readIfExists(
					TSUtils.createTemplate(id + "."
							+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME),
					localTx, Lease.FOREVER);
			if (tuple != null) {
				Class c = (Class) tuple.value;
				if (c != null) {
					result = (Knowledge) c.newInstance();
					result.id = id;
					result.typeDefinition = c;
					for (Field f : c.getFields()) {

						/*
						 * We check if the field is the type of Interface. If so
						 * we call readInterface recursively. Otherwise we
						 * simply assign a new value to the field.
						 */

						if (Knowledge.class.isAssignableFrom(f.getType())) {
							f.set(result,
									readKnowledge(id + "." + f.getName(),
											localTx));
						} else {
							tuple = (Tuple) space.readIfExists(
									TSUtils.createTemplate(id + "."
											+ f.getName()), localTx,
									Lease.FOREVER);
							if (tuple != null) {
								f.set(result, tuple.value);
							}
						}

					}
				}
			}
			if (tx == null)
				localTx.commit();
			return result;
		} catch (Exception e) {
			System.out.println("ERROR - Reading interface error: "
					+ e.getMessage());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#writeProperties(d3s.deeco
	 * .prototype.runtime.DEECoInvokableProcess, java.lang.Object[])
	 */
	@Override
	public void writeProperties(InvokableProcess process, List<Object> values) {
		writeProperties(process, values, null);
	}

	@Override
	public void writeProperties(InvokableProcess process, List<Object> values,
			Transaction tx) {
		if (process != null) {
			try {
				JavaSpace space = TSUtils.getSpace();
				Transaction localTx = (tx == null) ? TSUtils
						.createTransaction() : tx;
				DEECoProperty dp;
				String tId;
				Tuple tuple;
				Object newValue;
				for (int i = 0; i < values.size(); i++) {
					dp = process.outputParameters.get(i);
					if (Knowledge.class.isAssignableFrom(dp.type)) {
						writeKnowledge((Knowledge) values.get(i), (tx == null) ? null : localTx);
					} else {
						tId = process.rootKnowledgeId + "." + dp.name;
						tuple = (Tuple) space.readIfExists(
								TSUtils.createTemplate(tId), localTx,
								Lease.FOREVER);
						newValue = values.get(i);
						if ((tuple == null)
								|| (tuple.value != null && tuple.value
										.equals(newValue))
								|| (newValue != null && !newValue
										.equals(tuple.value))) {
							space.takeIfExists(TSUtils.createTemplate(tId),
									localTx, Lease.FOREVER);
							space.write(TSUtils.createTuple(tId, newValue),
									localTx, Lease.FOREVER);
						}
					}
				}
				if (tx == null)
					localTx.commit();
			} catch (Exception e) {
				System.out.println("ERROR - reading properties: "
						+ e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#readProperties(d3s.deeco
	 * .prototype.runtime.DEECoInvokableProcess)
	 */
	@Override
	public List<Object> readProperties(InvokableProcess process) {
		return readProperties(process, null);
	}

	@Override
	public List<Object> readProperties(InvokableProcess process, Transaction tx) {
		if (process != null) {
			try {
				JavaSpace space = TSUtils.getSpace();
				Transaction localTx = (tx == null) ? TSUtils
						.createTransaction() : tx;
				List<Object> result = new ArrayList<Object>();
				Tuple tuple;
				Object value = null;
				String tId;
				for (DEECoProperty dp : process.inputParameters) {
					tId = process.rootKnowledgeId + "." + dp.name;
					if (Knowledge.class.isAssignableFrom(dp.type)) {
						value = readKnowledge(tId, localTx);
					} else {
						tuple = (Tuple) space.readIfExists(
								TSUtils.createTemplate(tId), localTx,
								Lease.FOREVER);
						if (tuple != null) {
							value = tuple.value;
						}
					}
					result.add(value);
				}
				if (tx == null)
					localTx.commit();
				return result;
			} catch (Exception e) {
				System.out.println("ERROR - reading properties: "
						+ e.getMessage());
				return null;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#registerKnowledgeId(java
	 * .lang.Long)
	 */
	@Override
	public void registerRootKnowledge(String id) {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = TSUtils.createTransaction();
			Tuple entry = TSUtils.createTuple(ConstantKeys.ROOT_KNOWLEDGE_ID,
					id);
			space.takeIfExists(entry, tx, Lease.FOREVER);
			space.write(entry, tx, Lease.FOREVER);
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - registering knowledge error: "
					+ e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#unregisterKnowledge(java
	 * .lang.Long)
	 */
	@Override
	public void unregisterKnowledge(Long id) {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = TSUtils.createTransaction();
			Tuple entry = TSUtils.createTuple(ConstantKeys.ROOT_KNOWLEDGE_ID,
					id);
			space.takeIfExists(entry, tx, Lease.FOREVER);
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - registering knowledge error: "
					+ e.getMessage());
		}
	}

	@Override
	public Long getKnowledgeIdFromPath(String path, Long rootId) {
		try {
			String[] parts = path.split(".");
			if (parts.length > 1) {
				JavaSpace space = TSUtils.getSpace();
				Transaction tx = TSUtils.createTransaction();
				Long currentInterfaceId = rootId;
				Tuple template;
				for (int i = 0; i < parts.length; i++) {
					template = TSUtils.createTemplate(currentInterfaceId + "."
							+ parts[i]);
					currentInterfaceId = (Long) ((Tuple) space
							.readIfExists(
									TSUtils.createTemplate(ConstantKeys.DISTRIBUTED_UNIQUE_ID),
									tx, 1000)).value;
					if (currentInterfaceId == null)
						break;
				}
				tx.commit();
				if (currentInterfaceId != null) {
					return currentInterfaceId;
				}
			}
			return -1L;
		} catch (Exception e) {
			System.out.println("ERROR - Interface id retrieval error: "
					+ e.getMessage());
			return -1L;
		}
	}

	@Override
	public void performEnsembling(InvokableEnsemble iEnsemble) {
		EnsembleUtils.performEnsemble(iEnsemble, this);
	}

	/**
	 * Initializes TupleSpace and inserts unique identifier token to the space.
	 * This token is later used for identifier generation.
	 */
	private void initializeTupleSpace() {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = TSUtils.createTransaction();
			Tuple uniqueId = (Tuple) space.readIfExists(
					TSUtils.createTemplate(ConstantKeys.DISTRIBUTED_UNIQUE_ID),
					tx, 1000);
			if (uniqueId == null) {
				uniqueId = TSUtils.createTuple(
						ConstantKeys.DISTRIBUTED_UNIQUE_ID, new Long(1));
				space.write(uniqueId, tx, Lease.FOREVER);
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - Knowledge service error: "
					+ e.getMessage());
		}
	}
}
