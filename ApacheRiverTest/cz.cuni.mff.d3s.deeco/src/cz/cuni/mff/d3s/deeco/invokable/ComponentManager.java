package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

/**
 * Class providing component managing functionalities.
 * 
 * @author Michal Kit
 *
 */
public class ComponentManager extends
		InvokableManager<SchedulableKnowledgeProcess> {

	public ComponentManager(KnowledgeManager km) {
		super(km);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.invokable.InvokableManager#addInvokable(java.lang.Class)
	 */
	@Override
	public void addInvokable(Class invokableDefinition) {
		if (invokableDefinition != null) {
			RootKnowledge ik = getInitialKnowledge(invokableDefinition);
			if (ik != null) {
				if (writeRootKnowledge(ik)) {
					List<SchedulableKnowledgeProcess> invokables = SchedulableKnowledgeProcess
							.extractKnowledgeProcesses(invokableDefinition,
									ik.id, km);
					invokables.addAll(invokables);
				}
			}
		}
	}

	private RootKnowledge getInitialKnowledge(Class invokableDefinition) {
		RootKnowledge rk = null;
		try {
			Method init = RootKnowledge.getInitMethod(invokableDefinition);
			if (init != null) {
				rk = (RootKnowledge) init.invoke(null, new Object[] {});
				if (rk != null) {
					if (rk.id == null || rk.id.equals(""))
						rk.id = UUID.randomUUID().toString();
				}
			}
		} catch (Exception e) {
		}
		return rk;
	}

	private boolean writeRootKnowledge(RootKnowledge rootKnowledge) {
		if (rootKnowledge != null) {
			ISession session = km.createSession();
			try {
				while (session.repeat()) {
					session.begin();
					km.putKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID_FIELD,
							rootKnowledge.id, session);
					km.putKnowledge(rootKnowledge.id, rootKnowledge, session);
					session.end();
				}
				return true;
			} catch (Exception e) {
				System.out.println("Error when writing root knowledge: "
						+ e.getMessage());
			}
		}
		return false;
	}
}
