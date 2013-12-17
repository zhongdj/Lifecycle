package net.madz.lifecycle.meta.builder.impl;

import java.util.LinkedList;
import java.util.List;

import net.madz.common.Dumper;
import net.madz.lifecycle.meta.builder.RelationConstraintBuilder;
import net.madz.lifecycle.meta.builder.StateMetaBuilder;
import net.madz.lifecycle.meta.object.ErrorMessageObject;
import net.madz.lifecycle.meta.type.RelationConstraintMetadata;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class RelationConstraintBuilderImpl extends InheritableAnnotationMetaBuilderBase<RelationConstraintMetadata, StateMetadata> implements
        RelationConstraintBuilder {

    private StateMachineMetadata relatedStateMachine;
    private final LinkedList<StateMetadata> onStates = new LinkedList<>();
    private final LinkedList<ErrorMessageObject> errorMessageObjects = new LinkedList<>();
    private boolean nullable;
    private RelationMetadata relationMetadata;

    @Override
    public StateMachineMetadata getRelatedStateMachine() {
        return relatedStateMachine;
    }

    public RelationConstraintBuilderImpl(StateMetaBuilder parent, String name, List<StateMetadata> onStates, List<ErrorMessageObject> errorMessageObjects,
            StateMachineMetadata stateMachineMetadata, boolean nullable) {
        super(parent, name);
        this.onStates.addAll(onStates);
        this.errorMessageObjects.addAll(errorMessageObjects);
        this.relatedStateMachine = stateMachineMetadata;
        this.nullable = nullable;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

    @Override
    public RelationConstraintBuilder build(Class<?> klass, StateMetadata parent) throws VerificationException {
        super.build(klass, parent);
        this.relationMetadata = parent.getStateMachine().getRelationMetadata(klass);
        return this;
    }

    @Override
    public StateMetadata[] getOnStates() {
        return this.onStates.toArray(new StateMetadata[0]);
    }

    @Override
    public ErrorMessageObject[] getErrorMessageObjects() {
        return this.errorMessageObjects.toArray(new ErrorMessageObject[0]);
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void dump(Dumper dumper) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void verifySuper(Class<?> metaClass) throws VerificationException {}

    @Override
    protected RelationConstraintMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return null;
    }

    @Override
    protected boolean extendsSuperKeySet() {
        return true;
    }

    @Override
    public RelationMetadata getRelationMetadata() {
        return relationMetadata;
    }
}
