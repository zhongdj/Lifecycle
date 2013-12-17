package net.madz.lifecycle;

public interface StateConverter<T> {

    String toState(T t);

    T fromState(String state);
}
