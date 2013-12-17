package net.madz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EagerSetterImpl<T> implements Setter<T> {

    private final Method setter;

    public EagerSetterImpl(Method setter) {
        this.setter = setter;
    }

    @Override
    public void invoke(Object reactiveObject, T state) {
        try {
            setter.setAccessible(true);
            setter.invoke(reactiveObject, state);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        } finally {
            setter.setAccessible(false);
        }
    }
}