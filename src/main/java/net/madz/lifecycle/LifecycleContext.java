package net.madz.lifecycle;

import java.lang.reflect.Method;

public interface LifecycleContext<T, S> {

    T getTarget();

    S getFromState();
    
    String getFromStateName();

    S getToState();

    String getToStateName();
    
    Method getTransitionMethod();

    Object[] getArguments();
}
