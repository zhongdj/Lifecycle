package net.madz.lifecycle.meta.builder.impl;

import net.madz.common.Dumper;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.relation.Parent;
import net.madz.lifecycle.annotations.relation.RelateTo;
import net.madz.lifecycle.meta.builder.RelationMetaBuilder;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class RelationMetaBuilderImpl extends InheritableAnnotationMetaBuilderBase<RelationMetadata, StateMachineMetadata> implements RelationMetaBuilder {

    private StateMachineMetadata relatedStateMachine;
    private boolean parentRelation;

    public RelationMetaBuilderImpl(StateMachineMetadata parent, String name) {
        super(parent, name);
    }

    @Override
    public StateMachineMetadata getRelateToStateMachine() {
        return relatedStateMachine;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

    @Override
    public void dump(Dumper dumper) {
        // TODO Auto-generated method stub
    }

    @Override
    public RelationMetaBuilder build(Class<?> klass, StateMachineMetadata parent) throws VerificationException {
        super.build(klass, parent);
        configureSuper(klass);
        configureParent(klass);
        configureRelatedStateMachine(klass, parent);
        return this;
    }

    private void configureParent(Class<?> klass) {
        if ( !hasSuper(klass) ) {
            this.parentRelation = null != klass.getAnnotation(Parent.class);
        } else if ( hasLifecycleOverrideAnnotation(klass) ) {
            this.parentRelation = hasDeclaredAnnotation(klass, Parent.class);
        } else {
            if ( hasDeclaredAnnotation(klass, Parent.class) ) {
                this.parentRelation = true;
            } else {
                this.parentRelation = parent.getRelationMetadata(getSuperMetaClass(klass)).isParent();
            }
        }
    }

    @Override
    protected RelationMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return parent.getSuper().getDeclaredRelationMetadata(metaClass);
    }

    protected void verifyRelateTo(Class<?> clazz) throws VerificationException {
        if ( !hasSuper(clazz) ) {
            if ( null == clazz.getAnnotation(RelateTo.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.RELATION_NO_RELATED_TO_DEFINED, clazz);
            }
        } else if ( hasLifecycleOverrideAnnotation(clazz) ) {
            if ( !hasDeclaredAnnotation(clazz, RelateTo.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.RELATION_NO_RELATED_TO_DEFINED, clazz);
            }
        } else {
            // TODO It is not necessary ..
            if ( null != clazz.getAnnotation(RelateTo.class) ) {
                verifyRelateTo(getSuperMetaClass(clazz));
            }
        }
    }

    protected void configureRelatedStateMachine(Class<?> klass, StateMachineMetadata parent) throws VerificationException {
        verifyRelateTo(klass);
        if ( hasSuper() && isOverriding() ) {
            final RelateTo relateTo = getDeclaredAnnotation(klass, RelateTo.class);
            if ( null != relateTo ) {
                relatedStateMachine = parent.getRegistry().loadStateMachineMetadata(relateTo.value(), null);
            }
        } else {
            final Class<?> relateStateMachineClass = klass.getAnnotation(RelateTo.class).value();
            relatedStateMachine = parent.getRegistry().loadStateMachineMetadata(relateStateMachineClass, null);
        }
    }

    @Override
    public boolean isParent() {
        return parentRelation;
    }

    @Override
    protected boolean extendsSuperKeySet() {
        return true;
    }
}
