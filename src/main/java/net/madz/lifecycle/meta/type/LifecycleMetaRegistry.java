package net.madz.lifecycle.meta.type;

import net.madz.lifecycle.LifecycleEventHandler;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.verification.VerificationException;

public interface LifecycleMetaRegistry {

    StateMachineMetadata loadStateMachineMetadata(Class<?> metaClass) throws VerificationException;

    StateMachineMetadata loadStateMachineMetadata(Class<?> stateClass, StateMachineMetadata parent) throws VerificationException;

    StateMachineObject<?> loadStateMachineObject(Class<?> returnType) throws VerificationException;

    LifecycleEventHandler getLifecycleEventHandler();
}
