package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * DEECoTriggeredScheduling is used to mark an ensemble class or a component
 * process to be event triggered. (Not used currently)
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DEECoTriggeredScheduling {

}
