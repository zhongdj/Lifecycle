package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.object.StateObject;
import net.madz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.madz.verification.VerificationException;

public interface StateMachineObjectBuilder<S> extends AnnotationMetaBuilder<StateMachineObject<S>, StateMachineObject<S>>, StateMachineObject<S> {

    void setRegistry(LifecycleMetaRegistry registry);

    @Override
    StateMachineObjectBuilder<S> build(Class<?> klass, StateMachineObject<S> parent) throws VerificationException;
}
