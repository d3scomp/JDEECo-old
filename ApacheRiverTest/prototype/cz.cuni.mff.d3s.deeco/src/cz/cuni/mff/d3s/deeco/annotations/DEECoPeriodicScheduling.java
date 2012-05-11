package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark an ensemble class or a component process to be executed
 * periodically.
 * 
 * The attribute <code>value</code> indicates time interval (in ms) of the
 * execution.
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DEECoPeriodicScheduling {
	int value() default 1000;
}
