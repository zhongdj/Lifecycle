package net.madz.lifecycle.meta.object;

import java.util.LinkedList;

import net.madz.lifecycle.meta.builder.impl.StateMetaBuilderImpl;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata;

public class FunctionMetadata {

    private final StateMetadata parent;
    private final TransitionMetadata transition;
    private final LinkedList<StateMetadata> nextStates;

    public FunctionMetadata(StateMetaBuilderImpl parent, TransitionMetadata transition, LinkedList<StateMetadata> nextStates) {
        this.parent = parent;
        this.transition = transition;
        this.nextStates = nextStates;
    }

    public StateMetadata getParent() {
        return parent;
    }

    public TransitionMetadata getTransition() {
        return transition;
    }

    public LinkedList<StateMetadata> getNextStates() {
        return nextStates;
    }
}
