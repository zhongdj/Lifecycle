package net.madz.lifecycle.annotations.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    long value() default 300000L;
}
