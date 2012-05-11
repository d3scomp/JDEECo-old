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

/**
 * Base class defining common functionalities for all schedulable processes.
 * 
 * @author Michal Kit
 * 
 */
public abstract class SchedulableProcess {

	private Thread processThread;

	protected KnowledgeManager km;

	public ProcessSchedule scheduling;

	public SchedulableProcess(ProcessSchedule scheduling, KnowledgeManager km) {
		this.scheduling = scheduling;
		this.km = km;
	}

	/**
	 * Function used by the processes to retrieve all required parameters
	 * necessary for process method execution.
	 * 
	 * @param in
	 *            list of the input parameters. Those are retrieved from the
	 *            knowledge manager.
	 * @param out
	 *            list of the output parameters. Those are instantiated
	 *            according to the types and passed to the method. When the
	 *            process method execution finishes they are stored by the
	 *            knowledge manager.
	 * @param inOut
	 *            list of parameters which are both input and output. Those are
	 *            retrieved from the knowledge manager before process execution
	 *            and stored back when it finishes.
	 * @param root
	 *            knowledge level for which parameters should be retrieved or
	 *            stored.
	 * @return An array of parameter instances
	 * @throws KMNotExistentException
	 *             thrown whenever required input parameter is not available in
	 *             the knowledge repository
	 * @throws KMIllegalArgumentException
	 *             thrown whenever there is type conflict between the required
	 *             parameter and the one stored in the knowledge repository
	 */
	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> out, List<Parameter> inOut, String root)
			throws KMNotExistentException, KMIllegalArgumentException {
		return getParameterMethodValues(in, out, inOut, root, null);
	}

	/**
	 * Function used by the processes to retrieve all required parameters
	 * necessary for process method execution. This version is session oriented.
	 * 
	 * @param in
	 *            list of the input parameters. Those are retrieved from the
	 *            knowledge manager.
	 * @param out
	 *            list of the output parameters. Those are instantiated
	 *            according to the types and passed to the method. When the
	 *            process method execution finishes they are stored by the
	 *            knowledge manager.
	 * @param inOut
	 *            list of parameters which are both input and output. Those are
	 *            retrieved from the knowledge manager before process execution
	 *            and stored back when it finishes.
	 * @param root
	 *            knowledge level for which parameters should be retrieved or
	 *            stored.
	 * @param session
	 *            session instance within which all the retrieval operations
	 *            should be performed.
	 * @return An array of parameter instances
	 * @throws KMNotExistentException
	 *             thrown whenever required input parameter is not available in
	 *             the knowledge repository
	 * @throws KMIllegalArgumentException
	 *             thrown whenever there is type conflict between the required
	 *             parameter and the one stored in the knowledge repository
	 */
	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> out, List<Parameter> inOut, String root,
			ISession session) throws KMNotExistentException,
			KMIllegalArgumentException {
		ISession localSession = (session == null) ? km.createSession()
				: session;
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(in);
		parameters.addAll(inOut);
		Object[] result = new Object[parameters.size()
				+ ((out != null) ? out.size() : 0)];
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

	/**
	 * Function used to store computed values during the process method
	 * execution in the knowledge repository.
	 * 
	 * @param parameterValues
	 *            list of method all parameters
	 * @param out
	 *            list of output parameter descriptions
	 * @param inOut
	 *            list of both output and input parameter descriptions
	 * @param root
	 *            knowledge level for which parameters should stored.
	 */
	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> out, List<Parameter> inOut, String root) {
		putParameterMethodValues(parameterValues, out, inOut, root, null);
	}

	/**
	 * Function used to store computed values during the process method
	 * execution in the knowledge repository. This version is session oriented.
	 * 
	 * @param parameterValues
	 *            list of method all parameters
	 * @param out
	 *            list of output parameter descriptions
	 * @param inOut
	 *            list of both output and input parameter descriptions
	 * @param root
	 *            knowledge level for which parameters should stored
	 * @param session
	 *            session instance within which all the storing operations
	 *            should be performed.
	 */
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

	/**
	 * Function invokes single process execution.
	 */
	protected abstract void invoke();

	/**
	 * Function starts the process execution. In case its scheduling is periodic
	 * it creates a thread within which it invokes process execution
	 * periodically.
	 */
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

	/**
	 * Stops the process execution. In case its scheduling is periodic it
	 * interrupts the thread.
	 */
	public void stop() {
		if (processThread != null) {
			processThread.interrupt();
		}
	}
}
