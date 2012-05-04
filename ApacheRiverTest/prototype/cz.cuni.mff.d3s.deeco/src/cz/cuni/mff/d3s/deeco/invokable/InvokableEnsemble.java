package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.DEECoProcessSchedule;


public class InvokableEnsemble extends Invokable {

	public Method mapper;
	public Method membership;
	public Class firstParameterClass;
	public Class secondParameterClass;
	private IKnowledgeManager km;

	public InvokableEnsemble(Method membership, Method mapper,
			IKnowledgeManager km, DEECoProcessSchedule scheduling)
			throws Exception {
		this.membership = membership;
		this.mapper = mapper;
		this.km = km;
		this.scheduling = scheduling;

		Class[] membershipTypes = membership.getParameterTypes();
		Class[] mapperTypes = mapper.getParameterTypes();
		if (membershipTypes.length == 2 && mapperTypes.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (membershipTypes[i] != mapperTypes[i])
					throw new Exception(
							"Invalid ensemble definition! - List of parameters must be the same for mapper and membership function.");
				else if (i == 0)
					firstParameterClass = membershipTypes[i];
				else if (i == 1)
					secondParameterClass = membershipTypes[i];
			}
		} else {
			throw new Exception(
					"Invalid ensemble definition! - List of parameters must be the same for mapper and membership function.");
		}
	}

	@Override
	protected void invoke() {
		km.performEnsembling(this);
	}

}
