package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.object.StateObject;

public interface StateObjectBuilder<S> extends AnnotationMetaBuilder<StateObject<S>, StateMachineObject<S>>, StateObject<S> {}
