package d3s.deeco.prototype.knowledge;

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
import d3s.deeco.prototype.ducktype.DuckType;
import d3s.deeco.prototype.interfaces.KnowledgeInterface;
import d3s.deeco.prototype.properties.DEECoProperty;
import d3s.deeco.prototype.runtime.DEECoInvokableEnsemble;
import d3s.deeco.prototype.runtime.DEECoInvokableProcess;
import d3s.deeco.prototype.staticTypes.ConstantKeys;

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
	public Long generateUniqueKnowledgeId() {
		try {
			Long value = null;
			JavaSpace space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			Tuple template = TupleSpaceUtils
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
			return value;
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
	public Long writeInterface(KnowledgeInterface k) {
		boolean newInterface = k.getId() == null;
		if (newInterface) {
			k.setId(generateUniqueKnowledgeId());
		}
		if (k.getId() != null) {
			try {
				JavaSpace space = TupleSpaceUtils.getSpace();
				Transaction tx = TupleSpaceUtils.createTransaction();
				String tId = k.getId() + "."
						+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME;
				space.takeIfExists(TupleSpaceUtils.createTemplate(tId), tx,
						Lease.FOREVER);
				Class c = k.getClass();
				space.write(TupleSpaceUtils.createTuple(tId, c), tx,
						Lease.FOREVER);
				Long subInterfaceId;
				for (Field f : c.getFields()) {
					/*
					 * We check if the field is the type of Interface. If so we
					 * call writeInterface recursively. Otherwise we simply
					 * write a new value to the space.
					 */
					if (Knowledge.class.isAssignableFrom(f.getType())) {
						subInterfaceId = writeInterface((Knowledge) f.get(k));
						if (newInterface)
							space.write(
									TupleSpaceUtils.createTuple(k.getId() + "."
											+ f.getName(), subInterfaceId), tx,
									Lease.FOREVER);
					} else {
						tId = k.getId() + "." + f.getName();
						space.takeIfExists(TupleSpaceUtils.createTemplate(tId),
								tx, Lease.FOREVER);
						space.write(TupleSpaceUtils.createTuple(tId, f.get(k)),
								tx, Lease.FOREVER);
					}
				}
				tx.commit();
			} catch (Exception e) {
				System.out.println("ERROR - writing interface error: "
						+ e.getMessage());
			}
		}
		return k.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * d3s.deeco.prototype.knowledge.IKnowledgeManager#readInterface(java.lang
	 * .Long)
	 */
	@Override
	public KnowledgeInterface readInterface(Long id) {
		try {
			JavaSpace space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			Class c = (Class) ((Tuple) space.readIfExists(
					TupleSpaceUtils.createTemplate(id + "."
							+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME), tx,
					Lease.FOREVER)).value;
			KnowledgeInterface result = null;
			if (c != null) {
				result = (Knowledge) c.newInstance();
				result.setId(id);
				result.setTypeDefinition(c);
				for (Field f : c.getFields()) {
					Object value = ((Tuple) space.readIfExists(
							TupleSpaceUtils.createTemplate(id + "."
									+ f.getName()), tx, Lease.FOREVER)).value;
					/*
					 * We check if the field is the type of Interface. If so we
					 * call readInterface recursively. Otherwise we simply
					 * assign a new value to the field.
					 */
					if (Knowledge.class.isAssignableFrom(f.getType())) {
						f.set(result, readInterface((Long) value));
					} else {
						f.set(result, value);
					}
				}
			}
			tx.commit();
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
	public void writeProperties(DEECoInvokableProcess process,
			List<Object> values) {
		if (process != null) {
			try {
				JavaSpace space = TupleSpaceUtils.getSpace();
				Transaction tx = TupleSpaceUtils.createTransaction();
				DEECoProperty dp;
				String tId;
				for (int i = 0; i < values.size(); i++) {
					dp = process.outputParameters.get(i);
					if (Knowledge.class.isAssignableFrom(dp.type)) {// is
																	// Interface
						writeInterface((Knowledge) values.get(i));
					} else {
						tId = process.rootKnowledgeId + "." + dp.name;
						space.takeIfExists(TupleSpaceUtils.createTemplate(tId),
								tx, Lease.FOREVER);
						space.write(
								TupleSpaceUtils.createTuple(tId, values.get(i)),
								tx, Lease.FOREVER);
					}
				}
				tx.commit();
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
	public List<Object> readProperties(DEECoInvokableProcess process) {
		if (process != null) {
			try {
				JavaSpace space = TupleSpaceUtils.getSpace();
				Transaction tx = TupleSpaceUtils.createTransaction();
				List<Object> result = new ArrayList<Object>();
				Object value;
				for (DEECoProperty dp : process.inputParameters) {
					value = ((Tuple) space.readIfExists(TupleSpaceUtils
							.createTemplate(process.rootKnowledgeId + "."
									+ dp.name), tx, Lease.FOREVER)).value;
					if (Knowledge.class.isAssignableFrom(dp.type)) {// is
																	// Interface
						value = readInterface((Long) value);
					}
					result.add(value);
				}
				tx.commit();
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
	public void registerKnowledgeId(Long id) {
		try {
			JavaSpace space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			Tuple entry = TupleSpaceUtils.createTuple(
					ConstantKeys.KNOWLEDGE_ID, id);
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
			JavaSpace space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			Tuple entry = TupleSpaceUtils.createTuple(
					ConstantKeys.KNOWLEDGE_ID, id);
			space.takeIfExists(entry, tx, Lease.FOREVER);
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - registering knowledge error: "
					+ e.getMessage());
		}
	}

	/**
	 * Initializes TupleSpace and inserts unique identifier token to the space.
	 * This token is later used for identifier generation.
	 */
	private void initializeTupleSpace() {
		try {
			JavaSpace space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			Tuple uniqueId = (Tuple) space.readIfExists(TupleSpaceUtils
					.createTemplate(ConstantKeys.DISTRIBUTED_UNIQUE_ID), tx,
					1000);
			if (uniqueId == null) {
				uniqueId = TupleSpaceUtils.createTuple(
						ConstantKeys.DISTRIBUTED_UNIQUE_ID, new Long(1));
				space.write(uniqueId, tx, Lease.FOREVER);
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - Knowledge service error: "
					+ e.getMessage());
		}
	}

	@Override
	public Long getInterfaceIdFromPath(String path, Long rootId) {
		try {
			String[] parts = path.split(".");
			if (parts.length > 1) {
				JavaSpace space = TupleSpaceUtils.getSpace();
				Transaction tx = TupleSpaceUtils.createTransaction();
				Tuple template;
				Long currentInterfaceId = rootId;
				for (int i = 0; i < parts.length; i++) {
					template = TupleSpaceUtils
							.createTemplate(currentInterfaceId + "." + parts[i]);
					currentInterfaceId = (Long) ((Tuple) space
							.readIfExists(
									TupleSpaceUtils
											.createTemplate(ConstantKeys.DISTRIBUTED_UNIQUE_ID),
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
	public void performEnsembling(DEECoInvokableEnsemble iEnsemble) {
		try {
			JavaSpace05 space = TupleSpaceUtils.getSpace();
			List<Long> knowledgeIds = new ArrayList<Long>();
			MatchSet iterator = space.contents(Arrays
					.asList(new Object[] { TupleSpaceUtils
							.createTemplate(ConstantKeys.KNOWLEDGE_ID) }), null,
					Lease.FOREVER, Long.MAX_VALUE);
			Tuple currentTuple = (Tuple) iterator.next();
			while (currentTuple != null) {
				knowledgeIds.add((Long) currentTuple.value);
				currentTuple = (Tuple) iterator.next();
			}
			Class outerClass, innerClass;
			KnowledgeInterface[] parameters;
			Object [] duckObjects;
			for (Long oid : knowledgeIds) {
				outerClass = (Class) ((Tuple) space.readIfExists(
						TupleSpaceUtils.createTemplate(oid + "."
								+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME), null,
						Lease.FOREVER)).value;
				for (Long iid : knowledgeIds) {
					innerClass = (Class) ((Tuple) space.readIfExists(
							TupleSpaceUtils.createTemplate(oid + "."
									+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME),
							null, Lease.FOREVER)).value;
					if (DuckType.instanceOf(iEnsemble.firstParameterClass,
							outerClass)
							&& DuckType.instanceOf(
									iEnsemble.secondParameterClass, innerClass)) {

						parameters = readKnowledgeInterfaces(new Long[] { oid,
								iid });
						duckObjects = getDuckObjects(new Class[]{iEnsemble.firstParameterClass, iEnsemble.secondParameterClass}, parameters);
						if ((Boolean) iEnsemble.membership.invoke(null,
								duckObjects)) {
							iEnsemble.mapper.invoke(null, duckObjects);
							writeKnowledgeInterfaces(parameters);
						}

					}
					if (DuckType.instanceOf(iEnsemble.firstParameterClass,
							innerClass)
							&& DuckType.instanceOf(
									iEnsemble.secondParameterClass, outerClass)) {
						parameters = readKnowledgeInterfaces(new Long[] { iid,
								oid });
						duckObjects = getDuckObjects(new Class[]{iEnsemble.firstParameterClass, iEnsemble.secondParameterClass}, parameters);
						if ((Boolean) iEnsemble.membership.invoke(null,
								duckObjects)) {
							iEnsemble.mapper.invoke(null, duckObjects);
							writeKnowledgeInterfaces(parameters);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Ensembling error: " + e.getMessage());
		}
	}

	private Object[] getDuckObjects(Class [] interfacesToImplement, Object[] targets) {
		Object[] result = null;
		if (interfacesToImplement.length == targets.length) {
			result = new Object[interfacesToImplement.length];
			for (int i = 0; i < targets.length; i++) {
				result[i] = DuckType.implement(interfacesToImplement[i],
						targets[i]);
			}
		}
		return result;
	}

	private void writeKnowledgeInterfaces(KnowledgeInterface[] knowledges) {
		for (KnowledgeInterface k : knowledges) {
			writeInterface(k);
		}
	}

	private KnowledgeInterface[] readKnowledgeInterfaces(Long[] ids) {
		KnowledgeInterface[] result = new KnowledgeInterface[ids.length];
		for (int i = 0; i < ids.length; i++) {
			result[i] = readInterface(ids[i]);
		}
		return result;
	}
}
