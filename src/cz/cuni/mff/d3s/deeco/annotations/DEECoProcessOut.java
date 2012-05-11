package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a method parameter to be a process output parameter for a
 * component process evaluation. Such parameter will be instantiated by the
 * system and passed to the method for invocation. After the method execution it
 * will be stored in the knowledge repository.
 * 
 * The attribute <code>value</code> is dot separated absolute path, describing
 * the nesting in the component knowledge for which matching should be
 * performed.
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DEECoProcessOut {
	String value() default "";
}
