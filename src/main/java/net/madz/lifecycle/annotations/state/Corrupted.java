package net.madz.lifecycle.annotations.state;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Corrupted {

    int recoverPriority() default 0;
}
