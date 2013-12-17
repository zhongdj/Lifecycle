package net.madz.lifecycle.annotations.callback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.madz.utils.Null;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostStateChange {

    Class<?> from() default AnyState.class;

    Class<?> to() default AnyState.class;

    String observableName() default CallbackConsts.NULL_STR;

    Class<?> observableClass() default Null.class;

    String mappedBy() default CallbackConsts.NULL_STR;
}
