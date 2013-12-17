package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.verification.VerificationException;

public interface RelationMetaBuilder extends AnnotationMetaBuilder<RelationMetadata, StateMachineMetadata>, RelationMetadata {

    @Override
    RelationMetaBuilder build(Class<?> klass, StateMachineMetadata parent) throws VerificationException;
}
