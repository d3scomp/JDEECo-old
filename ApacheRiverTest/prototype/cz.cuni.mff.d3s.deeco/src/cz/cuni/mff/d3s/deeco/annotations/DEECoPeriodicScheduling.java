package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.staticTypes.EnumScheduling;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DEECoPeriodicScheduling {
	
	int interval() default 1000;
}
