package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.madz.lifecycle.meta.builder.impl.RelationObjectBuilderImpl;
import net.madz.lifecycle.meta.object.RelationObject;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.verification.VerificationException;

public class RelationObjectConfigure {

    private final ArrayList<RelationMetadata> extendedRelationMetadata;
    private final Class<?> klass;
    private final StateMachineObject<?> parent;

    public RelationObjectConfigure(final Class<?> klass, final ArrayList<RelationMetadata> extendedRelationMetadata, final StateMachineObject<?> parent) {
        super();
        this.extendedRelationMetadata = extendedRelationMetadata;
        this.klass = klass;
        this.parent = parent;
    }

    public RelationObject configure(final StateMachineMetadata relatedStateMachine, final Object relationKey, final AccessibleObject obj)
            throws VerificationException {
        final RelationMetadata relationMetadata = relatedStateMachine.getRelationMetadata(relationKey);
        if ( extendedRelationMetadata.contains(relationMetadata) ) return null;
        if ( relationMetadata.hasSuper() ) {
            markExtendedRelationMetadata(extendedRelationMetadata, relationMetadata);
        }
        return createRelationObject(obj, relationMetadata);
    }

    private RelationObject createRelationObject(final AccessibleObject obj, final RelationMetadata relationMetadata) throws VerificationException {
        if ( obj instanceof Field ) {
            return new RelationObjectBuilderImpl(parent, (Field) obj, relationMetadata).build(klass, parent).getMetaData();
        } else if ( obj instanceof Method ) {
            return new RelationObjectBuilderImpl(parent, (Method) obj, relationMetadata).build(klass, parent).getMetaData();
        } else {
            throw new UnsupportedOperationException("For now, we only support Field and Method when configuring relation object in class " + getClass());
        }
    }

    private void markExtendedRelationMetadata(final ArrayList<RelationMetadata> extendedRelationMetadata, final RelationMetadata relationMetadata) {
        extendedRelationMetadata.add(relationMetadata);
        if ( relationMetadata.hasSuper() ) {
            markExtendedRelationMetadata(extendedRelationMetadata, relationMetadata.getSuper());
        }
    }
}
