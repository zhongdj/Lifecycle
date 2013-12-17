package net.madz.util;

public interface Setter<T> {

    void invoke(Object reactiveObject, T state);
}