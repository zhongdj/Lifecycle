package net.madz.lifecycle.meta.builder.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.madz.lifecycle.meta.builder.RelationObjectBuilder;
import net.madz.lifecycle.meta.object.RelationObject;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.util.FieldEvaluator;
import net.madz.util.PropertyEvaluator;
import net.madz.util.Readable;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class RelationObjectBuilderImpl extends ObjectBuilderBase<RelationObject, StateMachineObject<?>, RelationMetadata> implements RelationObjectBuilder {

    private Readable<Object> evaluator = null;

    private RelationObjectBuilderImpl(final StateMachineObject<?> parent, final String name) {
        super(parent, name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RelationObjectBuilderImpl(final StateMachineObject parent, final Field field, RelationMetadata template) {
        this(parent, "RelationSet." + template.getDottedPath().getName() + "." + field.getName());
        evaluator = new FieldEvaluator(field);
        setMetaType(template);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RelationObjectBuilderImpl(final StateMachineObject parent, Method method, RelationMetadata template) {
        this(parent, "RelationSet." + template.getDottedPath().getName() + "." + method.getName());
        evaluator = new PropertyEvaluator(method);
        setMetaType(template);
    }

    @Override
    public RelationObjectBuilder build(Class<?> klass, StateMachineObject<?> parent) throws VerificationException {
        super.build(klass, parent);
        return this;
    }

    @Override
    public Readable<Object> getEvaluator() {
        return this.evaluator;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }
}
