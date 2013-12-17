package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.util.MethodScanCallback;
import net.madz.utils.Null;
import net.madz.verification.VerificationFailureSet;

public final class RelationIndicatorPropertyMethodScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private final VerificationFailureSet failureSet;

    public RelationIndicatorPropertyMethodScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.failureSet = failureSet;
    }

    @Override
    public boolean onMethodFound(Method method) {
        Relation relation = method.getAnnotation(Relation.class);
        if ( null == relation ) {
            return false;
        } else {
            if ( Null.class == relation.value() ) {} else if ( !this.stateMachineObjectBuilderImpl.getMetaType().hasRelation(relation.value()) ) {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationFailure(method.getDeclaringClass().getName(), SyntaxErrors.LM_REFERENCE_INVALID_RELATION_INSTANCE, method
                        .getDeclaringClass().getName(), relation.value().getName(), this.stateMachineObjectBuilderImpl.getMetaType().getDottedPath().getAbsoluteName()));
            }
        }
        return false;
    }
}