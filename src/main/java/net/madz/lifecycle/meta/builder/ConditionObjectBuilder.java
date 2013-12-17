package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.object.ConditionObject;
import net.madz.lifecycle.meta.object.StateMachineObject;

public interface ConditionObjectBuilder extends ConditionObject, AnnotationMetaBuilder<ConditionObject, StateMachineObject<?>> {}
