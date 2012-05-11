package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;

/**
 * RootKnowledge is the base class representing top-level component knowledge.
 * 
 * 
 * @author Michal Kit
 *
 */
public class RootKnowledge {
	public String id;

	public static Method getInitMethod(Class c) {
		List<Method> result = AnnotationHelper.getAnnotatedMethods(c,
				DEECoInitialize.class);
		if (result.size() == 1) {
			return result.get(0);
		}
		return null;
	}
}
