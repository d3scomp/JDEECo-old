package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DEECoProcessInOut is used to mark a method parameter as both process input
 * and process output parameter, which is used when invoking a component
 * process. Such parameter will be retrieved form the knowledge repository for
 * method computation and stored back when it finishes.
 * 
 * The attribute "value" is dot separated absolute path, describing the nesting
 * in the component knowledge for which matching should be performed.
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DEECoProcessInOut {
	String value() default "";
}
