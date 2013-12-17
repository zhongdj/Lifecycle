package net.madz.lifecycle.annotations.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Conditional {

    Class<? extends ConditionalTransition<?>> judger();

    boolean postEval() default false;

    Class<?> condition();
}
