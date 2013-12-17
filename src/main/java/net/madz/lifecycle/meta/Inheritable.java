package net.madz.lifecycle.meta;

public interface Inheritable<T> {

    T getSuper();

    boolean isOverriding();

    boolean hasSuper();
}
