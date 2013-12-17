package net.madz.lifecycle.impl;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.madz.bcel.intercept.LifecycleInterceptContext;
import net.madz.lifecycle.LifecycleContext;

public class LifecycleContextImpl<T, S> implements LifecycleContext<T, S> {

    private final T target;
    private final S fromState;
    private final String fromStateName;
    private final S toState;
    private final String toStateName;
    private final Method transitionMethod;
    private final Object[] arguments;

    @SuppressWarnings("unchecked")
    public LifecycleContextImpl(LifecycleInterceptContext context, S fromState, S toState) {
        this.target = (T) context.getTarget();
        this.fromStateName = context.getFromState();
        this.fromState = fromState;
        if ( null == context.getToState() ) {
            this.toState = null;
        } else {
            this.toState = toState;
        }
        this.toStateName = context.getToState();
        this.transitionMethod = context.getMethod();
        this.arguments = context.getArguments();
    }

    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public S getFromState() {
        return fromState;
    }

    @Override
    public S getToState() {
        return toState;
    }

    @Override
    public Method getTransitionMethod() {
        return transitionMethod;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "LifecycleContextImpl [target=" + target + ", fromState=" + fromState + ", toState=" + toState + ", transitionMethod=" + transitionMethod
                + ", arguments=" + Arrays.toString(arguments) + "]";
    }

    @Override
    public String getFromStateName() {
        return this.fromStateName;
    }

    @Override
    public String getToStateName() {
        return this.toStateName;
    }
}
