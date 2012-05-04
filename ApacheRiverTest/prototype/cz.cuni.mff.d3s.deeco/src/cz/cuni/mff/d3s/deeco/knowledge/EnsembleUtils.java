package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.xml.internal.txw2.TXW;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.ducktype.DuckType;
import cz.cuni.mff.d3s.deeco.invokable.InvokableEnsemble;
import cz.cuni.mff.d3s.deeco.staticTypes.ConstantKeys;

public class EnsembleUtils {
	static public void performEnsemble(InvokableEnsemble iEnsemble,
			IKnowledgeManager km) {
		try {
			JavaSpace05 space = TSUtils.getSpace();
			List<String> knowledgeIds = new ArrayList<String>();
			MatchSet iterator = space.contents(Arrays
					.asList(new Object[] { TSUtils
							.createTemplate(ConstantKeys.ROOT_KNOWLEDGE_ID) }),
					null, Lease.FOREVER, Long.MAX_VALUE);
			Tuple currentTuple = (Tuple) iterator.next();
			while (currentTuple != null) {
				knowledgeIds.add((String) currentTuple.value);
				currentTuple = (Tuple) iterator.next();
			}
			Class outerClass, innerClass;
			Tuple tuple;
			for (String oid : knowledgeIds) {
				tuple = (Tuple) space.readIfExists(
						TSUtils.createTemplate(oid + "."
								+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME),
						null, Lease.FOREVER);
				if (tuple != null) {
					outerClass = (Class) tuple.value;
					for (String iid : knowledgeIds) {
						tuple = (Tuple) space
								.readIfExists(
										TSUtils.createTemplate(oid
												+ "."
												+ ConstantKeys.TYPE_DEFINITION_FIELD_NAME),
										null, Lease.FOREVER);
						if (tuple != null) {
							innerClass = (Class) tuple.value;
							if (DuckType.interfaceMatchesKnowledge(
									iEnsemble.firstParameterClass, outerClass)
									&& DuckType.interfaceMatchesKnowledge(
											iEnsemble.secondParameterClass,
											innerClass)) {
								ensembleTwoKnowledges(oid, iid, iEnsemble, km);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Ensembling Error - " + e.getMessage());
		}
	}

	static private void ensembleTwoKnowledges(String aKnowledgeId, String bKnowledgeId,
			InvokableEnsemble iEnsemble, IKnowledgeManager km) {
		try {
			Transaction tx = TSUtils.createTransaction();
			Knowledge a = km.readKnowledge(aKnowledgeId, tx);
			Knowledge b = km.readKnowledge(bKnowledgeId, tx);
			if (a != null && b != null) {
				Knowledge firstParameter = DuckType.getInterfacedKnowledge(iEnsemble.firstParameterClass, a);
				Knowledge secondParameter = DuckType.getInterfacedKnowledge(iEnsemble.secondParameterClass, b);
				if (firstParameter != null && secondParameter != null) {
					Object [] parameters = new Object[]{firstParameter, secondParameter};
					if ((Boolean) iEnsemble.membership.invoke(null, parameters)) {
						iEnsemble.mapper.invoke(null, parameters);
						
						km.writeKnowledge(firstParameter, tx);
						km.writeKnowledge(secondParameter, tx);
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println("ERROR - Ensembling Error - " + e.getMessage());
		}

	}
}
