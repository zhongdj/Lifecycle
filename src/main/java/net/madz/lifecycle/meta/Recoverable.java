package net.madz.lifecycle.meta;

import net.madz.lifecycle.meta.type.TransitionMetadata;

public interface Recoverable {

    public abstract boolean hasRedoTransition();

    public abstract TransitionMetadata getRedoTransition();

    public abstract boolean hasRecoverTransition();

    public abstract TransitionMetadata getRecoverTransition();

    public abstract boolean hasCorruptTransition();

    public abstract TransitionMetadata getCorruptTransition();
}