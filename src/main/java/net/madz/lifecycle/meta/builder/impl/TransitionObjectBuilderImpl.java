package net.madz.lifecycle.meta.builder.impl;

import java.lang.reflect.Method;

import net.madz.lifecycle.meta.builder.StateMachineObjectBuilder;
import net.madz.lifecycle.meta.builder.TransitionObjectBuilder;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.object.TransitionObject;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class TransitionObjectBuilderImpl extends ObjectBuilderBase<TransitionObject, StateMachineObject<?>, TransitionMetadata> implements
        TransitionObjectBuilder {

    private Method transitionMethod;

    public TransitionObjectBuilderImpl(StateMachineObjectBuilder<?> parent, Method transitionMethod, TransitionMetadata template) {
        super(parent, "TransitionSet." + template.getDottedPath().getName() + "." + transitionMethod.getName());
        this.transitionMethod = transitionMethod;
        this.setMetaType(template);
    }

    @Override
    public TransitionObjectBuilder build(Class<?> klass, StateMachineObject<?> parent) throws VerificationException {
        super.build(klass, parent);
        return this;
    }

    @Override
    public Method getTransitionMethod() {
        return transitionMethod;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }
}
