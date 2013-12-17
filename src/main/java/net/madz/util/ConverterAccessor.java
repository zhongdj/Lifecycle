package net.madz.util;

import net.madz.lifecycle.StateConverter;

public final class ConverterAccessor<T> implements StateAccessible<String> {

    private final StateConverter<T> stateConverter;
    private final StateAccessible<T> rawAccessor;

    public ConverterAccessor(StateConverter<T> stateConverter, StateAccessible<T> rawAccessor) {
        this.stateConverter = stateConverter;
        this.rawAccessor = rawAccessor;
    }

    @Override
    public String read(Object reactiveObject) {
        return stateConverter.toState(rawAccessor.read(reactiveObject));
    }

    @Override
    public void write(Object reactiveObject, String state) {
        rawAccessor.write(reactiveObject, stateConverter.fromState(state));
    }
}