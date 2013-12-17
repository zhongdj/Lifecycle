package net.madz.util;

import java.lang.reflect.Field;

public final class FieldEvaluator<T> implements Readable<T> {

    private final Field objField;

    public FieldEvaluator(Field objField) {
        this.objField = objField;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(Object reactiveObject) {
        try {
            if ( !objField.isAccessible() ) objField.setAccessible(true);
            return (T) objField.get(reactiveObject);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            if ( !objField.isAccessible() ) objField.setAccessible(false);
        }
    }
}