package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.verification.VerificationException;

public interface StateMetaBuilder extends AnnotationMetaBuilder<StateMetadata, StateMachineMetadata>, StateMetadata {

    @Override
    StateMetaBuilder build(Class<?> clazz, StateMachineMetadata parent) throws VerificationException;

    @Override
    StateMachineMetadata getStateMachine();

    void configureFunctions(Class<?> stateClass) throws VerificationException;

    void configureCompositeStateMachine(Class<?> stateClass) throws VerificationException;

    void configureRelationConstrants(Class<?> clazz) throws VerificationException;

    void addPossibleReachingTransition(TransitionMetadata transition);
}
