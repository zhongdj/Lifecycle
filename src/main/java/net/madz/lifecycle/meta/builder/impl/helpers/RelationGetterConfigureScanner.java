package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.madz.lifecycle.meta.object.RelationObject;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.util.MethodScanCallback;
import net.madz.util.StringUtil;
import net.madz.utils.Null;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public final class RelationGetterConfigureScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    final private StateMachineObject<?> stateMachineObject;
    private final VerificationFailureSet failureSet;
    private final Class<?> klass;
    final ArrayList<RelationMetadata> extendedRelationMetadata;

    public RelationGetterConfigureScanner(Class<?> klass, StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl,
            StateMachineObject<?> stateMachineObject, VerificationFailureSet failureSet, final ArrayList<RelationMetadata> extendedRelationMetadata) {
        super();
        this.klass = klass;
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.stateMachineObject = stateMachineObject;
        this.failureSet = failureSet;
        this.extendedRelationMetadata = extendedRelationMetadata;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if ( null == method.getAnnotation(Relation.class) ) {
            return false;
        }
        try {
            final StateMachineMetadata relatedStateMachine = this.stateMachineObjectBuilderImpl.getMetaType().getRegistry()
                    .loadStateMachineMetadata(method.getDeclaringClass().getAnnotation(LifecycleMeta.class).value());
            final Object relationKey = getRelationKeyFromProperty(method);
            final RelationObjectConfigure configure = new RelationObjectConfigure(method.getDeclaringClass(), extendedRelationMetadata,
                    this.stateMachineObjectBuilderImpl);
            final RelationObject relationObject = configure.configure(relatedStateMachine, relationKey, method);
            if ( null == relationObject ) {
                return false;
            } else {
                this.stateMachineObjectBuilderImpl.addRelation(method.getDeclaringClass(), relationObject, relationObject.getMetaType().getPrimaryKey());
            }
        } catch (VerificationException e) {
            failureSet.add(e);
        }
        return false;
    }

    private Object getRelationKeyFromProperty(Method method) {
        final Class<?> relationClass = method.getAnnotation(Relation.class).value();
        if ( Null.class == relationClass ) {
            if ( method.getName().startsWith("get") ) {
                return StringUtil.toUppercaseFirstCharacter(method.getName().substring(3));
            } else {
                return StringUtil.toUppercaseFirstCharacter(method.getName());
            }
        } else {
            return relationClass;
        }
    }
}