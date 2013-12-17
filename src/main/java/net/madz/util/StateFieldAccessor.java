package net.madz.util;

import java.lang.reflect.Field;

public final class StateFieldAccessor<T> implements StateAccessible<T> {

    private final Field stateField;

    public StateFieldAccessor(Field stateField) {
        this.stateField = stateField;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(Object reactiveObject) {
        final boolean accessible = stateField.isAccessible();
        try {
            if ( !accessible ) stateField.setAccessible(true);
            return (T) stateField.get(reactiveObject);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            if ( !accessible ) stateField.setAccessible(false);
        }
    }

    @Override
    public void write(Object reactiveObject, T state) {
        final boolean accessible = stateField.isAccessible();
        try {
            if ( !accessible ) stateField.setAccessible(true);
            stateField.set(reactiveObject, state);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            if ( !accessible ) stateField.setAccessible(false);
        }
    }
}