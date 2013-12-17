package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.callback.AnyState;
import net.madz.lifecycle.annotations.callback.CallbackConsts;
import net.madz.lifecycle.annotations.callback.Callbacks;
import net.madz.lifecycle.annotations.callback.PostStateChange;
import net.madz.lifecycle.annotations.callback.PreStateChange;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.util.MethodScanCallback;
import net.madz.utils.Null;
import net.madz.verification.VerificationFailureSet;

public final class CallbackMethodVerificationScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private VerificationFailureSet failureSet;

    public CallbackMethodVerificationScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, final VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.failureSet = failureSet;
    }

    @Override
    public boolean onMethodFound(Method method) {
        verifyPreStateChange(method, failureSet, method.getAnnotation(PreStateChange.class));
        verifyPostStateChange(method, failureSet, method.getAnnotation(PostStateChange.class));
        verifyCallbacks(method);
        return false;
    }

    private void verifyCallbacks(Method method) {
        final Callbacks callbacks = method.getAnnotation(Callbacks.class);
        if ( null != callbacks ) {
            for ( PreStateChange item : callbacks.preStateChange() ) {
                verifyPreStateChange(method, failureSet, item);
            }
            for ( PostStateChange item : callbacks.postStateChange() ) {
                verifyPostStateChange(method, failureSet, item);
            }
        }
    }

    private void verifyPostStateChange(final Method method, final VerificationFailureSet failureSet, final PostStateChange postStateChange) {
        if ( null == postStateChange ) return;
        if ( isRelationalCallback(postStateChange.observableName(), postStateChange.observableClass()) ) return;
        verifyStateWithoutRelation(method, failureSet, postStateChange.from(), SyntaxErrors.POST_STATE_CHANGE_FROM_STATE_IS_INVALID);
        verifyStateWithoutRelation(method, failureSet, postStateChange.to(), SyntaxErrors.POST_STATE_CHANGE_TO_STATE_IS_INVALID);
    }

    private void verifyPreStateChange(final Method method, final VerificationFailureSet failureSet, final PreStateChange preStateChange) {
        if ( null == preStateChange ) return;
        if ( isRelationalCallback(preStateChange.observableName(), preStateChange.observableClass()) ) return;
        verifyStateWithoutRelation(method, failureSet, preStateChange.from(), SyntaxErrors.PRE_STATE_CHANGE_FROM_STATE_IS_INVALID);
        verifyStateWithoutRelation(method, failureSet, preStateChange.to(), SyntaxErrors.PRE_STATE_CHANGE_TO_STATE_IS_INVALID);
        if ( AnyState.class == preStateChange.to() ) return;
        verifyPreToStatePostEvaluate(method, failureSet, preStateChange.to(), (StateMachineMetadata) this.stateMachineObjectBuilderImpl.getMetaType());
    }

    private boolean isRelationalCallback(String relation, Class<?> observableClass) {
        return !CallbackConsts.NULL_STR.equals(relation) || Null.class != observableClass;
    }

    private void verifyPreToStatePostEvaluate(final Method method, final VerificationFailureSet failureSet, final Class<?> toStateClass,
            final StateMachineMetadata stateMachineMetadata) {
        final StateMetadata toState = ( (StateMachineMetadata) this.stateMachineObjectBuilderImpl.getMetaType() ).getState(toStateClass);
        if ( null == toState ) return;
        for ( final TransitionMetadata transition : stateMachineMetadata.getState(toStateClass).getPossibleReachingTransitions() ) {
            if ( transition.isConditional() && transition.postValidate() ) {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationFailure(this.stateMachineObjectBuilderImpl.getDottedPath(),
                        SyntaxErrors.PRE_STATE_CHANGE_TO_POST_EVALUATE_STATE_IS_INVALID, toStateClass, method, transition.getDottedPath()));
            }
        }
    }

    private void verifyStateWithoutRelation(final Method method, final VerificationFailureSet failureSet, final Class<?> stateClass, final String errorCode) {
        if ( AnyState.class != stateClass ) {
            if ( stateMetadataNotFound(stateClass) ) {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(method.getDeclaringClass().getName() + "." + stateClass + "."
                        + errorCode, errorCode, stateClass, method, this.stateMachineObjectBuilderImpl.getMetaType().getPrimaryKey()));
            }
        }
    }

    private boolean stateMetadataNotFound(final Class<?> stateClass) {
        return null == this.stateMachineObjectBuilderImpl.getMetaType().getState(stateClass);
    }
}