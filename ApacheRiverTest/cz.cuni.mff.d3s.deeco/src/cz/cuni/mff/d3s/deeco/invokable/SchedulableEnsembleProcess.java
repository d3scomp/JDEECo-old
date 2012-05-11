package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

public class SchedulableEnsembleProcess extends SchedulableProcess {

	private EnsembleParametrizedMethod mapper;
	private EnsembleParametrizedMethod membership;

	public SchedulableEnsembleProcess(ProcessSchedule scheduling,
			KnowledgeManager km) {
		super(scheduling, km);
	}

	public SchedulableEnsembleProcess(EnsembleParametrizedMethod membership,
			EnsembleParametrizedMethod mapper, ProcessSchedule scheduling,
			KnowledgeManager km) {
		this(scheduling, km);
		this.membership = membership;
		this.mapper = mapper;
	}

	@Override
	protected void invoke() {
		ISession session = km.createSession();
		try {
			String[] ids = null;
			while (session.repeat()) {
				session.begin();
				ids = km.findAllProperties(
						ConstantKeys.ROOT_KNOWLEDGE_ID_FIELD, session);
				session.end();
			}
			if (!(session.repeat() || session.hasSucceeded())) {
				return;
			}
			ISession coordinatorSession = null, memberSession = null;
			Object[] coordinatorParams, memberParams;
			cloop: for (String oid : ids) {
				coordinatorSession = km.createSession();
				while (coordinatorSession.repeat()) {
					coordinatorSession.begin();
					try {
						coordinatorParams = getParameterMethodValues(
								membership.coordinatorIn,
								membership.coordinatorOut,
								membership.coordinatorInOut, oid);
					} catch (KMIllegalArgumentException | KMNotExistentException knee) {
						try {
							coordinatorSession.cancel();
						} catch (SessionException se) {
						}
						continue cloop;
					}
					mloop: for (String iid : ids) {
						memberSession = km.createSession();
						while (memberSession.repeat()) {
							try {
								memberParams = getParameterMethodValues(
										membership.memberIn,
										membership.memberOut,
										membership.memberInOut, iid);
								evaluateEnsemble(memberParams,
										coordinatorParams);
							} catch (KMIllegalArgumentException | KMNotExistentException knee) {
								try {
									memberSession.cancel();
								} catch (SessionException se) {
								}
								continue mloop;
							}
						}
					}
					coordinatorSession.end();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				session.cancel();
			} catch (SessionException se) {
			}
		}
	}

	private void evaluateEnsemble(Object[] memberParams,
			Object[] coordinatorParams) {
		try {
			if ((Boolean) membership.invoke(memberParams, coordinatorParams)) {
				mapper.invoke(memberParams, coordinatorParams);
			}
		} catch (Exception e) {
			System.out.println("Ensemble evaluation exception! - "
					+ e.getMessage());
		}
	}

	public static SchedulableEnsembleProcess extractEnsembleProcesses(Class c, KnowledgeManager km) {
		SchedulableEnsembleProcess result = null;
		if (c != null) {
			result = new SchedulableEnsembleProcess(
					ScheduleHelper.getSchedule(AnnotationHelper.getAnnotation(
							DEECoPeriodicScheduling.class, c.getAnnotations())),
					km);
			Method method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMembership.class);
			if (method != null)
				result.membership = EnsembleParametrizedMethod
						.extractParametrizedMethod(method);
			else
				return null;
			method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMapper.class);
			if (method != null)
				result.mapper = EnsembleParametrizedMethod
						.extractParametrizedMethod(method);
			else
				return null;
			return result;
		}
		return result;
	}
}
