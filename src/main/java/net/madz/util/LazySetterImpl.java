package net.madz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LazySetterImpl<T> implements Setter<T> {

    private final Method getter;
    private volatile Method setterMethod;

    public LazySetterImpl(Method getter) {
        this.getter = getter;
    }

    @Override
    public void invoke(Object reactiveObject, T state) {
        initSetter(reactiveObject);
        try {
            setterMethod.setAccessible(true);
            setterMethod.invoke(reactiveObject, state);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        } finally {
            setterMethod.setAccessible(false);
        }
    }

    private void initSetter(Object reactiveObject) {
        if ( null == setterMethod ) {
            synchronized (this) {
                if ( null == setterMethod ) {
                    setterMethod = findSetter(reactiveObject);
                }
            }
        }
    }

    private Method findSetter(Object reactiveObject) {
        final String setterName = "set" + getter.getName().substring(3);
        for ( Class<?> rawClass = reactiveObject.getClass(); rawClass != Object.class; rawClass = rawClass.getSuperclass() ) {
            try {
                return rawClass.getDeclaredMethod(setterName, getter.getReturnType());
            } catch (NoSuchMethodException | SecurityException e) {
                continue;
            }
        }
        throw new IllegalStateException("state setter method: " + setterName + " Cannot be found through class: " + reactiveObject.getClass());
    }
}