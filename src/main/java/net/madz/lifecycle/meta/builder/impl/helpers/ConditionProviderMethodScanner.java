package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;
import java.util.HashSet;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.action.Condition;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.util.MethodScanCallback;
import net.madz.verification.VerificationFailureSet;

public final class ConditionProviderMethodScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private final VerificationFailureSet failureSet;
    private HashSet<Class<?>> conditions = new HashSet<>();
    private StateMachineMetadata template;
    private Class<?> klass;

    public ConditionProviderMethodScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, Class<?> klass, StateMachineMetadata template, VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.template = template;
        this.klass = klass;
        this.failureSet = failureSet;
    }

    @Override
    public boolean onMethodFound(Method method) {
        final Condition condition = method.getAnnotation(Condition.class);
        if ( null != condition ) {
            if ( template.hasCondition(condition.value()) ) {
                if ( conditions.contains(condition.value()) ) {
                    failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(), SyntaxErrors.LM_CONDITION_MULTIPLE_METHODS_REFERENCE_SAME_CONDITION, klass,
                            condition.value()));
                } else {
                    if ( !condition.value().isAssignableFrom(method.getReturnType()) ) {
                        failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(), SyntaxErrors.LM_CONDITION_OBJECT_DOES_NOT_IMPLEMENT_CONDITION_INTERFACE,
                                method, condition.value()));
                    }
                    conditions.add(condition.value());
                }
            } else {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(), SyntaxErrors.LM_CONDITION_REFERENCE_INVALID, method, condition.value()));
            }
        }
        return false;
    }
}