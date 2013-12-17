package net.madz.util;


public interface StateAccessible<T> extends Readable<T> {

    void write(Object reactiveObject, T state);
}