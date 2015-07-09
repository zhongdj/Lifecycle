package net.imadz.lifecycle.annotations.callback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.imadz.utils.Null;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnEvent {

	Class<?> value() default AnyEvent.class;
	
    String observableName() default CallbackConsts.NULL_STR;

    Class<?> observableClass() default Null.class;

    String mappedBy() default CallbackConsts.NULL_STR;
	
}
