package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.util.MethodScanCallback;
import net.madz.verification.VerificationFailureSet;

public final class StateIndicatorGetterMethodScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private Method stateGetterMethod = null;
    private boolean overridingFound = false;
    private final Class<?> klass;
    private final VerificationFailureSet failureSet;

    public StateIndicatorGetterMethodScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, Class<?> klass,
            final VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.klass = klass;
        this.failureSet = failureSet;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if ( null == stateGetterMethod && null != method.getAnnotation(StateIndicator.class) ) {
            stateGetterMethod = method;
            overridingFound = null != method.getAnnotation(LifecycleOverride.class);
            return false;
        } else if ( null != stateGetterMethod && null != method.getAnnotation(StateIndicator.class) ) {
            if ( !overridingFound ) {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(this.stateMachineObjectBuilderImpl.getDottedPath(),
                        SyntaxErrors.STATE_INDICATOR_MULTIPLE_STATE_INDICATOR_ERROR, klass));
                return true;
            }
        }
        return false;
    }

    public Method getStateGetterMethod() {
        return stateGetterMethod;
    }
}