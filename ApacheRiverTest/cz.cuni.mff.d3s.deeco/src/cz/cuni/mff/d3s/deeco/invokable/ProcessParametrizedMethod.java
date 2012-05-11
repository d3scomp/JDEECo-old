package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessOut;

public class ProcessParametrizedMethod extends ParameterizedMethod {
	public String root;
	public List<Parameter> in;
	public List<Parameter> inOut;
	public List<Parameter> out;

	public ProcessParametrizedMethod(String root, Method method) {
		super(method);
		this.root = root;
	}

	public Object invoke(Object[] parameters) {
		try {
			return method.invoke(null, parameters);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static synchronized ProcessParametrizedMethod extractParametrizedMethod(
			Method method, String root) {
		ProcessParametrizedMethod result = null;
		if (method != null) {
			result = new ProcessParametrizedMethod(root, method);
			result.in = AnnotationHelper.getParameters(method,
					DEECoProcessIn.class);
			result.out = AnnotationHelper.getParameters(method,
					DEECoProcessOut.class);
			result.inOut = AnnotationHelper.getParameters(method,
					DEECoProcessInOut.class);
		}
		return result;
	}
}
