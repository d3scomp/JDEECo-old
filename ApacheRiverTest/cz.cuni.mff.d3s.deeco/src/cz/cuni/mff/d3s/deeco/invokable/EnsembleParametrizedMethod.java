package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberOut;

/**
 * Classs used to represent ensemble specific method. Either membership or
 * mapper.
 * 
 * @author Michal Kit
 * 
 */
public class EnsembleParametrizedMethod extends ParameterizedMethod {

	public List<Parameter> memberIn;
	public List<Parameter> memberInOut;
	public List<Parameter> memberOut;
	public List<Parameter> coordinatorIn;
	public List<Parameter> coordinatorInOut;
	public List<Parameter> coordinatorOut;

	public EnsembleParametrizedMethod(Method method) {
		super(method);
	}

	/**
	 * Function invokes the method and returns the result of that invocation.
	 * 
	 * @param memberParameters
	 *            method parameters marked as {@link DEECoMemberIn},
	 *            {@link DEECoMemberOut} or {@link DEECoMemberInOut}
	 * @param coordinatorParameters
	 *            method parameters marked as {@link DEECoCoordinatorIn},
	 *            {@link DEECoCoordinatorOut} or {@link DEECoCoordinatorInOut}
	 * @return method invocation result
	 */
	public Object invoke(Object[] memberParameters,
			Object[] coordinatorParameters) {
		try {
			Object[] parameters = null;
			List<Parameter> memberCombined = null, coordinatorCombined = null;
			if (memberParameters != null && coordinatorParameters != null) {
				parameters = new Object[memberParameters.length
						+ coordinatorParameters.length];
				memberCombined = getCombined(memberIn, memberOut, memberInOut);
				coordinatorCombined = getCombined(coordinatorIn,
						coordinatorOut, coordinatorInOut);
			} else if (memberParameters != null) {
				parameters = new Object[memberParameters.length];
				memberCombined = getCombined(memberIn, memberOut, memberInOut);
			} else if (coordinatorParameters != null) {
				parameters = new Object[coordinatorParameters.length];
				coordinatorCombined = getCombined(coordinatorIn,
						coordinatorOut, coordinatorInOut);
			}
			Parameter dp;
			if (memberCombined != null)
				for (int i = 0; i < memberCombined.size(); i++) {
					dp = memberCombined.get(i);
					parameters[dp.index] = memberParameters[i];
				}
			if (coordinatorCombined != null)
				for (int i = 0; i < coordinatorCombined.size(); i++) {
					dp = coordinatorCombined.get(i);
					parameters[dp.index] = coordinatorParameters[i];
				}
			return method.invoke(null, parameters);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private List<Parameter> getCombined(List<Parameter>... lists) {
		List<Parameter> result = new ArrayList<Parameter>();
		for (List<Parameter> l : lists) {
			result.addAll(l);
		}
		return result;
	}

	/**
	 * Static function extracting {@link EnsembleParametrizedMethod} instance
	 * from the given {@link Method} object.
	 * 
	 * @param method
	 *            to be parsed
	 * @return {@link EnsembleParametrizedMethod} instance or null in case of
	 *         parsing failure.
	 */
	public static EnsembleParametrizedMethod extractParametrizedMethod(
			Method method) {
		EnsembleParametrizedMethod result = null;
		if (method != null) {
			result = new EnsembleParametrizedMethod(method);
			result.memberIn = AnnotationHelper.getParameters(method,
					DEECoMemberIn.class);
			result.memberOut = AnnotationHelper.getParameters(method,
					DEECoMemberOut.class);
			result.memberInOut = AnnotationHelper.getParameters(method,
					DEECoMemberInOut.class);
			result.coordinatorIn = AnnotationHelper.getParameters(method,
					DEECoCoordinatorIn.class);
			result.coordinatorOut = AnnotationHelper.getParameters(method,
					DEECoCoordinatorOut.class);
			result.coordinatorInOut = AnnotationHelper.getParameters(method,
					DEECoCoordinatorInOut.class);
		}
		return result;
	}
}
