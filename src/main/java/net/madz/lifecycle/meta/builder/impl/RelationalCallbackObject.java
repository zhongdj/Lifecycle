package net.madz.lifecycle.meta.builder.impl;

import java.lang.reflect.Method;

import net.madz.util.Readable;

public class RelationalCallbackObject extends CallbackObject {

    private final Readable<?> readAccessor;

    public RelationalCallbackObject(String fromStateName, String toStateName, Method callbackMethod, Readable<?> accessor) {
        super(fromStateName, toStateName, callbackMethod);
        this.readAccessor = accessor;
    }

    @Override
    protected Object evaluateTarget(Object target) {
        return this.readAccessor.read(target);
    }
}
