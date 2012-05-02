package d3s.deeco.prototype.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import d3s.deeco.prototype.annotations.DEECoEnsemble;
import d3s.deeco.prototype.annotations.DEECoEnsembleMapper;
import d3s.deeco.prototype.annotations.DEECoEnsembleMembership;
import d3s.deeco.prototype.annotations.DEECoInitialize;
import d3s.deeco.prototype.annotations.DEECoInput;
import d3s.deeco.prototype.annotations.DEECoInputOutput;
import d3s.deeco.prototype.annotations.DEECoOutput;
import d3s.deeco.prototype.annotations.DEECoPeriodicScheduling;
import d3s.deeco.prototype.annotations.DEECoProcess;
import d3s.deeco.prototype.knowledge.IKnowledgeManager;
import d3s.deeco.prototype.properties.DEECoProperty;
import d3s.deeco.prototype.scheduling.DEECoProcessPeriodicSchedule;
import d3s.deeco.prototype.scheduling.DEECoProcessSchedule;

public class DEECoClassParser {

	private static List<Method> getAnnotatedMethod(Class c,
			Class annotationClass) {
		if (c != null) {
			List<Method> result = new ArrayList<Method>();
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annotationClass) != null)
					result.add(m);
			}
			return result;
		}
		return null;
	}

	private static DEECoProperty parseParameterAnnotation(
			Annotation annotation, Class type, int index) {
		if (annotation instanceof DEECoInput) {
			DEECoInput in = (DEECoInput) annotation;
			return new DEECoProperty(in.name(), type, index);
		} else if (annotation instanceof DEECoOutput) {
			DEECoOutput out = (DEECoOutput) annotation;
			return new DEECoProperty(out.name(), type, index);
		} else if (annotation instanceof DEECoInputOutput) {
			DEECoInputOutput inout = (DEECoInputOutput) annotation;
			return new DEECoProperty(inout.name(), type, index);
		} else {
			return null;
		}
	}

	private static Annotation getAnnotation(Class annotationClass,
			Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (annotationClass.isInstance(a)) {
				return a;
			}
		}
		return null;
	}

	private static List<DEECoProperty> getProperties(Method m,
			Class annotationClass) {
		Annotation[][] allAnnotations = m.getParameterAnnotations();
		Class[] parameterTypes = m.getParameterTypes();
		Annotation currentAnnotation;
		List<DEECoProperty> result = new ArrayList<DEECoProperty>();
		DEECoProperty currentProperty;
		Annotation[] parameterAnnotations;
		for (int i = 0; i < allAnnotations.length; i++) {
			parameterAnnotations = allAnnotations[i];
			currentAnnotation = getAnnotation(annotationClass,
					parameterAnnotations);
			if (currentAnnotation != null) {
				currentProperty = parseParameterAnnotation(currentAnnotation,
						parameterTypes[i], i);
				if (currentProperty != null)
					result.add(currentProperty);
			}
		}
		return result;
	}

	public static Method getInitMethod(Class c) {
		List<Method> result = getAnnotatedMethod(c, DEECoInitialize.class);
		if (result.size() == 1) {
			return result.get(0);
		}
		return null;
	}

	public static List<DEECoInvokableProcess> getProcess(Class c,
			Long knowledgeId, IKnowledgeManager km) {
		if (c != null) {
			List<Method> methods = getAnnotatedMethod(c, DEECoProcess.class);
			DEECoPeriodicScheduling schedulingA;
			List<DEECoProperty> properties;
			if (methods != null && methods.size() > 0) {
				List<DEECoInvokableProcess> result = new ArrayList<DEECoInvokableProcess>();
				DEECoInvokableProcess pi;
				for (Method m : methods) {
					pi = new DEECoInvokableProcess(km);
					pi.inputParameters = getProperties(m, DEECoInput.class);
					pi.outputParameters = getProperties(m, DEECoOutput.class);
					properties = getProperties(m, DEECoInputOutput.class);
					pi.inputParameters.addAll(properties);
					pi.outputParameters.addAll(properties);
					pi.rootKnowledgeId = knowledgeId;
					pi.method = m;
					result.add(pi);
					schedulingA = (DEECoPeriodicScheduling) m
							.getAnnotation(DEECoPeriodicScheduling.class);
					if (schedulingA != null) {
						pi.scheduling = new DEECoProcessPeriodicSchedule(
								schedulingA.interval());
					} else {
						pi.scheduling = new DEECoProcessPeriodicSchedule();
					}
				}
				return result;
			}
		}
		return null;
	}

	public static DEECoInvokableEnsemble getEnsemble(Class c,
			IKnowledgeManager km) {
		try {
			if (c != null) {
				List<Method> membershipFunctions = getAnnotatedMethod(c,
						DEECoEnsembleMembership.class);
				if (membershipFunctions.size() == 1) {
					Method membershipFun = membershipFunctions.get(0);
					List<Method> mapperFunctions = getAnnotatedMethod(c,
							DEECoEnsembleMapper.class);
					if (mapperFunctions.size() == 1) {
						Method mapperFun = mapperFunctions.get(0);
						DEECoEnsemble ensembleAnnotation = (DEECoEnsemble) getAnnotation(
								DEECoEnsemble.class, c.getAnnotations());
						DEECoProcessSchedule schedule = new DEECoProcessPeriodicSchedule(
								ensembleAnnotation.interval());
						return new DEECoInvokableEnsemble(membershipFun,
								mapperFun, km, schedule);
					} else {
						System.out
								.println("ERROR - wrong ensemble definition - Mapper function!");
					}
				} else {
					System.out
							.println("ERROR - wrong ensemble definition - Membership function!");
				}
			}
			return null;
		} catch (Exception e) {
			System.out.println("ERROR - parsing ensemble - " + e.getMessage());
			return null;
		}
	}
}
