package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.annotations.action.Condition;
import net.madz.lifecycle.meta.type.ConditionMetadata;
import net.madz.util.MethodScanCallback;

public final class ScannerForVerifyConditionCoverage implements MethodScanCallback {

    private final ConditionMetadata conditionMetadata;
    private boolean covered = false;

    public ScannerForVerifyConditionCoverage(ConditionMetadata conditionMetadata) {
        this.conditionMetadata = conditionMetadata;
    }

    @Override
    public boolean onMethodFound(Method method) {
        final Condition condition = method.getAnnotation(Condition.class);
        if ( null != condition ) {
            if ( conditionMetadata.getKeySet().contains(condition.value()) ) {
                covered = true;
                return true;
            }
        }
        return false;
    }

    public boolean isCovered() {
        return covered;
    }
}