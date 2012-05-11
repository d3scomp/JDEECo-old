package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathBuilder;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

public abstract class SchedulableProcess {

	private Thread processThread;

	protected KnowledgeManager km;

	public ProcessSchedule scheduling;

	public SchedulableProcess(ProcessSchedule scheduling, KnowledgeManager km) {
		this.scheduling = scheduling;
		this.km = km;
	}

	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> out, List<Parameter> inOut, String root)
			throws KMNotExistentException, KMIllegalArgumentException {
		return getParameterMethodValues(in, out, inOut, root, null);
	}

	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> out, List<Parameter> inOut, String root,
			ISession session) throws KMNotExistentException,
			KMIllegalArgumentException {
		ISession localSession = (session == null) ? km.createSession()
				: session;
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(in);
		parameters.addAll(inOut);
		Object[] result = new Object[parameters.size() + out.size()];
		try {
			while (!localSession.repeat()) {
				localSession.begin();
				for (Parameter dp : parameters) {
					result[dp.index] = km.getKnowledge(
							KnowledgePathBuilder.appendToRoot(root, dp.name),
							dp.type, localSession);

				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			parameters = out;
			for (Parameter dp : parameters) {
				result[dp.index] = dp.type.newInstance();
			}
			return result;
		} catch (KMIllegalArgumentException | KMNotExistentException knee) {
			throw knee;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> out, List<Parameter> inOut, String root) {
		putParameterMethodValues(parameterValues, out, inOut, root, null);
	}

	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> out, List<Parameter> inOut, String root,
			ISession session) {
		if (parameterValues != null) {
			List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.addAll(out);
			parameters.addAll(inOut);
			ISession localSession = (session == null) ? km.createSession()
					: session;
			Object value;
			String completeName;
			try {
				while (localSession.repeat()) {
					localSession.begin();
					for (Parameter dp : parameters) {
						value = parameterValues[dp.index];
						completeName = KnowledgePathBuilder.appendToRoot(root,
								dp.name);
						km.takeKnowledge(completeName, value.getClass(),
								localSession);
						km.putKnowledge(completeName, value, localSession);
					}
					if (session == null)
						localSession.end();
					else
						break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	protected abstract void invoke();

	public void start() {
		final ProcessPeriodicSchedule s = (ProcessPeriodicSchedule) scheduling;
		if (s != null) {
			if (processThread != null)
				stop();
			processThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							invoke();
							Thread.sleep(s.interval);
						}
					} catch (Exception e) {
						System.out.println("ERROR - Process execution error: "
								+ e.getMessage());
					}
				}
			});
			processThread.start();
		}
	}

	/*
	 * To be changed later
	 */
	public void stop() {
		if (processThread != null) {
			processThread.interrupt();
		}
	}
}
