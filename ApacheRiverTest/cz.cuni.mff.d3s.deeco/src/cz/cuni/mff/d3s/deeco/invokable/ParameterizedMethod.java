package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;

/**
 * Base class representing a parameterized method. 
 * 
 * @author Michal Kit
 *
 */
public class ParameterizedMethod {
	public Method method;
	
	public ParameterizedMethod(Method method) {
		this.method = method;
	}
}
