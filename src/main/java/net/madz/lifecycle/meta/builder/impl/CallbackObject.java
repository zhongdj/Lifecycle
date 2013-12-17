package net.madz.lifecycle.meta.builder.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleContext;
import net.madz.lifecycle.LifecycleException;

public class CallbackObject {

    private final String fromStateName;
    private final String toStateName;
    private final Method callbackMethod;

    public CallbackObject(String fromStateName, String toStateName, Method callbackMethod) {
        super();
        this.fromStateName = fromStateName;
        this.toStateName = toStateName;
        this.callbackMethod = callbackMethod;
    }

    public boolean matches(final LifecycleContext<?, ?> callbackContext) {
        String fromState = callbackContext.getFromStateName();
        String toStateName = callbackContext.getToStateName();
        if ( this.fromStateName.equals(fromState) && this.toStateName.equals(toStateName) ) {
            return true;
        }
        return false;
    }

    public void doCallback(final LifecycleContext<?, ?> callbackContext) {
        try {
            Object evaluateTarget = evaluateTarget(callbackContext.getTarget());
            callbackMethod.invoke(evaluateTarget, callbackContext);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new LifecycleException(getClass(), LifecycleCommonErrors.BUNDLE, LifecycleCommonErrors.CALLBACK_EXCEPTION_OCCOURRED, callbackMethod, e);
        }
    }

    protected Object evaluateTarget(Object target) {
        return target;
    }
}
