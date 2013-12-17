package net.madz.lifecycle.annotations.callback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Callbacks {

    PreStateChange[] preStateChange() default {};

    PostStateChange[] postStateChange() default {};
}
