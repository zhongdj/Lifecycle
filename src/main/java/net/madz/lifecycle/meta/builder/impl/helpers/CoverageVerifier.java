package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;
import java.util.HashSet;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.madz.util.MethodScanCallback;
import net.madz.util.StringUtil;
import net.madz.utils.Null;
import net.madz.verification.VerificationFailureSet;

public final class CoverageVerifier implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private final TransitionMetadata transitionMetadata;
    HashSet<Class<?>> declaringClass = new HashSet<>();
    private final VerificationFailureSet failureSet;

    public CoverageVerifier(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, final TransitionMetadata transitionMetadata, final VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.transitionMetadata = transitionMetadata;
        this.failureSet = failureSet;
    }

    public boolean notCovered() {
        return declaringClass.size() == 0;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if ( !match(transitionMetadata, method) ) {
            return false;
        }
        if ( !declaringClass.contains(method.getDeclaringClass()) ) {
            declaringClass.add(method.getDeclaringClass());
            return false;
        }
        final TransitionTypeEnum type = transitionMetadata.getType();
        if ( type.isUniqueTransition() ) {
            failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationFailure(transitionMetadata.getDottedPath(), SyntaxErrors.LM_REDO_CORRUPT_RECOVER_TRANSITION_HAS_ONLY_ONE_METHOD,
                    transitionMetadata.getDottedPath().getName(), "@" + type.name(), this.stateMachineObjectBuilderImpl.getMetaType().getDottedPath(), this.stateMachineObjectBuilderImpl.getDottedPath().getAbsoluteName()));
        }
        return false;
    }

    private boolean match(TransitionMetadata transitionMetadata, Method transitionMethod) {
        Transition transition = transitionMethod.getAnnotation(Transition.class);
        if ( null == transition ) return false;
        final String transitionName = transitionMetadata.getDottedPath().getName();
        if ( Null.class == transition.value() ) {
            return transitionName.equals(StringUtil.toUppercaseFirstCharacter(transitionMethod.getName()));
        } else {
            return transitionName.equals(transition.value().getSimpleName());
        }
    }
}