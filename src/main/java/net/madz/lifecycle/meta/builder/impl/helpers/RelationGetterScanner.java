package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.lifecycle.meta.type.RelationConstraintMetadata;
import net.madz.util.MethodScanCallback;
import net.madz.utils.ClassUtils;

public final class RelationGetterScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private RelationConstraintMetadata relationMetadata;
    private boolean covered = false;

    public RelationGetterScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, RelationConstraintMetadata relation) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.relationMetadata = relation;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if ( !Relation.Utils.isRelationMethod(method) ) {
            return false;
        }
        if ( matchRelationWithName(method.getAnnotation(Relation.class), method.getName()) ) {
            covered = true;
        } else if ( matchRelationWithKeyClass(method.getAnnotation(Relation.class)) ) {
            covered = true;
        }
        return covered;
    }

    public boolean isCovered() {
        return covered;
    }

    private boolean matchRelationWithKeyClass(final Relation relation) {
        final Class<?> relationKeyClass = relation.value();
        return !ClassUtils.isDefaultStyle(relationKeyClass) && this.stateMachineObjectBuilderImpl.isKeyOfRelationMetadata(relationMetadata, relationKeyClass);
    }

    private boolean matchRelationWithName(final Relation relation, final String methodName) {
        final Class<?> relationKeyClass = relation.value();
        final String relationName = methodName.substring(3); // trim "get"
        return ClassUtils.isDefaultStyle(relationKeyClass) && this.stateMachineObjectBuilderImpl.isKeyOfRelationMetadata(relationMetadata, relationName);
    }
}