package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.object.TransitionObject;

public interface TransitionObjectBuilder extends AnnotationMetaBuilder<TransitionObject, StateMachineObject<?>>, TransitionObject {}
