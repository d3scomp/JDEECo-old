package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

/**
 * Class representing a component process.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableKnowledgeProcess extends SchedulableProcess {

	private ProcessParametrizedMethod process;

	public SchedulableKnowledgeProcess(ProcessParametrizedMethod process,
			ProcessSchedule scheduling, KnowledgeManager km) {
		super(scheduling, km);
		this.process = process;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	protected void invoke() {
		try {
			Object[] processParameters = getParameterMethodValues(process.in,
					process.out, process.inOut, process.root);
			process.invoke(processParameters);
			putParameterMethodValues(processParameters, process.out,
					process.inOut, process.root);
		} catch (KMIllegalArgumentException | KMNotExistentException knee) {
			System.out.println(knee.getMessage());
		}
	}

	/**
	 * Static function used to extract {@link SchedulableKnowledgeProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param root
	 *            component id for which process executes
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return list of {@link SchedulableKnowledgeProcess} instances extracted
	 *         from the class definition
	 */
	public static List<SchedulableKnowledgeProcess> extractKnowledgeProcesses(
			Class c, String root, KnowledgeManager km) {
		List<SchedulableKnowledgeProcess> result = null;
		if (c != null) {
			result = new ArrayList<SchedulableKnowledgeProcess>();
			List<Method> methods = AnnotationHelper.getAnnotatedMethods(c,
					DEECoProcess.class);
			ProcessParametrizedMethod currentMethod;
			if (methods != null && methods.size() > 0) {
				for (Method m : methods) {
					currentMethod = ProcessParametrizedMethod
							.extractParametrizedMethod(m, root);
					if (currentMethod != null) {
						result.add(new SchedulableKnowledgeProcess(
								currentMethod,
								ScheduleHelper.getSchedule(AnnotationHelper
										.getAnnotation(
												DEECoPeriodicScheduling.class,
												m.getAnnotations())), km));
					}
				}
			}
		}
		return result;
	}
}
