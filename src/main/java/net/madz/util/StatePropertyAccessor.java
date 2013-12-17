package net.madz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class StatePropertyAccessor<T> implements StateAccessible<T> {

    private final Method getter;
    private final Setter<T> setter;

    public StatePropertyAccessor(Method getter, Setter<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(Object reactiveObject) {
        try {
            return (T) getter.invoke(reactiveObject);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void write(Object reactiveObject, T state) {
        setter.invoke(reactiveObject, state);
    }
}