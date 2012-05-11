package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.AnnotationProxy;
import cz.cuni.mff.d3s.deeco.annotations.IValuedAnnotation;

/**
 * AnnotationHelper provides static methods which are used for annotation
 * parsing.
 * 
 * @author Michal Kit
 * 
 */
public class AnnotationHelper {

	/**
	 * Searches for annotation instance of the type "annotationClass" in the
	 * array of annotations.
	 * 
	 * @param annotationClass
	 *            type of the annotation to be searched for.
	 * @param annotations
	 *            array of annotation instances where the search is performed
	 * @return found annotation instance or null if the search fails
	 */
	public static Annotation getAnnotation(Class annotationClass,
			Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (annotationClass.isInstance(a)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Returns list of methods in the class definition which are annotated with
	 * the given annotation class.
	 * 
	 * @param c class definition to be parsed
	 * @param annotationClass
	 *            class of annotation that should be considered during the
	 *            parsing
	 * @return list of found methods
	 */
	public static List<Method> getAnnotatedMethods(Class c,
			Class annotationClass) {
		List<Method> result = new ArrayList<Method>();
		if (c != null) {
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annotationClass) != null)
					result.add(m);
			}
		}
		return result;
	}

	/**
	 * Returns a method in the class definition which are annotated with
	 * the given annotation class.
	 * 
	 * @param c class definition to be parsed
	 * @param annotationClass
	 *            class of annotation that should be considered during the
	 *            parsing
	 * @return matching method or null in case of search failure
	 */
	public static Method getAnnotatedMethod(Class c, Class annotationClass) {
		if (c != null) {
			Method result = null;
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annotationClass) != null) {
					result = m;
					break;
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * Returns list of method parameters, which are annotated with the given annotation class.
	 * 
	 * @param method Method object that needs to be parsed
	 * @param annotationClass class of annotation that should be considered during the
	 *            parsing
	 * @return list of {@link Parameter} instances which fulfills search criteria.
	 * 
	 * @see Parameter
	 */
	public static List<Parameter> getParameters(Method method,
			Class annotationClass) {
		List<Parameter> result = new ArrayList<Parameter>();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();
		Annotation currentAnnotation;
		Parameter currentProperty;
		Annotation[] parameterAnnotations;
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterAnnotations = allAnnotations[i];
			currentAnnotation = getAnnotation(annotationClass,
					parameterAnnotations);
			if (currentAnnotation != null) {
				currentProperty = parseNamedAnnotation(currentAnnotation,
						parameterTypes[i], i);
				if (currentProperty != null)
					result.add(currentProperty);
			}
		}
		return result;
	}

	private static Parameter parseNamedAnnotation(Annotation annotation,
			Class type, int index) {
		IValuedAnnotation namedAnnotation = (IValuedAnnotation) AnnotationProxy
				.implement(IValuedAnnotation.class, annotation);
		return new Parameter(namedAnnotation.value(), type, index);
	}

}
