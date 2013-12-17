package net.madz.lifecycle.meta.builder;

import net.madz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.StateMetadata;

public interface StateMachineMetaBuilder extends AnnotationMetaBuilder<StateMachineMetadata, StateMachineMetadata>, StateMachineMetadata {

    void setRegistry(LifecycleMetaRegistry registry);

    void setComposite(boolean b);

    void setOwningState(StateMetadata stateMetaBuilderImpl);

    boolean hasTransition(Object obj);

    StateMachineMetaBuilder[] getCompositeStateMachines();

    LifecycleMetaRegistry getRegistry();
}
