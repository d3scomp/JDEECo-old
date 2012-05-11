package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DEECoMemberIn marks a method parameter as a member input parameter, which is
 * used when evaluating an ensemble. Such parameter will be retrieved but never
 * stored back to the knowledge repository.
 * 
 * The attribute <code>value</code> is dot separated absolute path, describing the nesting
 * in the component knowledge for which matching should be performed.
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DEECoMemberIn {
	String value() default "";
}
