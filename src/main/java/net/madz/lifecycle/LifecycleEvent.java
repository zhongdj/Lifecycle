package net.madz.lifecycle;

import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;

public interface LifecycleEvent {

    Object getReactiveObject();

    String fromState();

    String toState();

    String transition();

    TransitionTypeEnum transitionType();

    long startTime();

    long endTime();
}
