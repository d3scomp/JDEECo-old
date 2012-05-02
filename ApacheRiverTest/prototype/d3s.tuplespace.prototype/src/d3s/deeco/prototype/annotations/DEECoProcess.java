package d3s.deeco.prototype.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import d3s.deeco.prototype.staticTypes.EnumTransaction;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DEECoProcess {
	EnumTransaction transactionType() default EnumTransaction.WEAK;
}
