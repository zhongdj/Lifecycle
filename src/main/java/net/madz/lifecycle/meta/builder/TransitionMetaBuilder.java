package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.verification.VerificationException;

public interface TransitionMetaBuilder extends AnnotationMetaBuilder<TransitionMetadata, StateMachineMetadata>, TransitionMetadata {

    @Override
    TransitionMetaBuilder build(Class<?> klass, StateMachineMetadata parent) throws VerificationException;
}
