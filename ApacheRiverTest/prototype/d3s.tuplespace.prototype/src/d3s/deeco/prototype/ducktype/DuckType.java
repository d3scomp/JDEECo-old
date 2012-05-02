package d3s.deeco.prototype.ducktype;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DuckType implements InvocationHandler {

	public static Object implement(Class interfaceToImplement, Object object) {
		return Proxy.newProxyInstance(interfaceToImplement.getClassLoader(),
				new Class[] { interfaceToImplement }, new DuckType(object));
	}

	public static boolean instanceOf(Class intrface, Class targetClass) {
		final Method[] methods = intrface.getMethods();
		for (int methodidx = 0; methodidx < methods.length; methodidx++) {
			Method method = methods[methodidx];
			try {
				targetClass.getMethod(method.getName(),
						method.getParameterTypes());
			} catch (NoSuchMethodException e) {
				return false;
			}
		}
		return true;
	}

	protected DuckType(Object object) {
		this.object = object;
		this.objectClass = object.getClass();
	}

	protected Object object;
	protected Class objectClass;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Method realMethod = objectClass.getMethod(method.getName(),
				method.getParameterTypes());
		return realMethod.invoke(object, args);
	}

}
