package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DEECoInitialize is used to mark a method in the component knowledge
 * definition class, to be used for initial instance retrieval.
 * 
 * @author Michal Kit
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DEECoInitialize {

}
