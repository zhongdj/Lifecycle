package net.madz.lifecycle.impl;

import net.madz.bcel.intercept.LifecycleInterceptContext;
import net.madz.lifecycle.LifecycleEvent;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;

public class LifecycleEventImpl implements LifecycleEvent {

    private final Object reactiveObject;
    private final String fromState;
    private final String toState;
    private final String transition;
    private final TransitionTypeEnum transitionType;
    private final long startTime;
    private final long endTime;

    public LifecycleEventImpl(LifecycleInterceptContext context) {
        this.reactiveObject = context.getTarget();
        this.fromState = context.getFromState();
        this.toState = context.getToState();
        this.transition = context.getTransition();
        this.transitionType = context.getTransitionType();
        this.startTime = context.getStartTime();
        this.endTime = context.getEndTime();
    }

    @Override
    public Object getReactiveObject() {
        return reactiveObject;
    }

    @Override
    public String fromState() {
        return fromState;
    }

    @Override
    public String toState() {
        return toState;
    }

    @Override
    public String transition() {
        return transition;
    }

    @Override
    public TransitionTypeEnum transitionType() {
        return transitionType;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public long endTime() {
        return endTime;
    }
}
