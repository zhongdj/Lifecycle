package net.madz.lifecycle.meta.type;

import net.madz.lifecycle.meta.MetaType;
import net.madz.lifecycle.meta.object.ErrorMessageObject;

public interface RelationConstraintMetadata extends MetaType<RelationConstraintMetadata> {

    StateMetadata getParent();

    StateMachineMetadata getRelatedStateMachine();

    StateMetadata[] getOnStates();

    ErrorMessageObject[] getErrorMessageObjects();

    boolean isNullable();

    RelationMetadata getRelationMetadata();
}
