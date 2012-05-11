package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationProxy implements InvocationHandler {

	private Annotation annotation;
	private Class annotationClass;

	public static Object implement(Class interfaceToImplement,
			Annotation annotation) {
		return Proxy.newProxyInstance(interfaceToImplement.getClassLoader(),
				new Class[] { interfaceToImplement }, new AnnotationProxy(
						annotation));
	}

	public AnnotationProxy(Annotation annotation) {
		this.annotation = annotation;
		this.annotationClass = annotation.getClass();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Method realMethod = annotationClass.getMethod(method.getName(),
				method.getParameterTypes());
		return realMethod.invoke(annotation, args);
	}

}
