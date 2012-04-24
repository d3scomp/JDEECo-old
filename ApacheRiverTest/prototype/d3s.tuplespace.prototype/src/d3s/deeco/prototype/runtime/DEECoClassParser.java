package d3s.deeco.prototype.runtime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import d3s.deeco.prototype.annotations.DEECoInitialize;
import d3s.deeco.prototype.annotations.DEECoProcess;
import d3s.deeco.prototype.interfaces.Interface;

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

	public static Method getInitMethod(Class c) {
		List<Method> result = getAnnotatedMethod(c, DEECoInitialize.class);
		if (result.size() == 1) {
			return result.get(0);
		}
		return null;
	}

	public static List<DEECoProcessInstance> getProcess(Class c, Interface k,
			DEECoRuntime runtime) {
		if (c != null) {
			List<Method> methods = getAnnotatedMethod(c, DEECoProcess.class);
			if (methods != null && methods.size() > 0) {
				List<DEECoProcessInstance> result = new ArrayList<DEECoProcessInstance>();
				DEECoProcessInstance pi;
				for (Method m : methods) {
					pi = new DEECoProcessInstance(runtime);
					DEECoProcess annotation = (DEECoProcess) m
							.getAnnotation(DEECoProcess.class);
					String[] properties = annotation.input();
					String[] absProperties = new String[properties.length];
					for (int i = 0; i < properties.length; i++) {
						absProperties[i] = k.id.toString() + "."
								+ properties[i];
					}
					pi.inputParameters = absProperties;
					properties = annotation.output();
					absProperties = new String[properties.length];
					for (int i = 0; i < properties.length; i++) {
						absProperties[i] = k.id.toString() + "."
								+ properties[i];
					}
					pi.outputParameters = absProperties;
					pi.method = m;
					result.add(pi);
				}
				return result;
			}
		}
		return null;
	}
}
