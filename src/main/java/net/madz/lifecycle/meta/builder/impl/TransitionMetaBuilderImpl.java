package net.madz.lifecycle.meta.builder.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.madz.common.Dumper;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.action.Conditional;
import net.madz.lifecycle.annotations.action.ConditionalTransition;
import net.madz.lifecycle.annotations.action.Corrupt;
import net.madz.lifecycle.annotations.action.Fail;
import net.madz.lifecycle.annotations.action.Recover;
import net.madz.lifecycle.annotations.action.Redo;
import net.madz.lifecycle.annotations.action.Timeout;
import net.madz.lifecycle.meta.builder.TransitionMetaBuilder;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.util.StringUtil;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class TransitionMetaBuilderImpl extends InheritableAnnotationMetaBuilderBase<TransitionMetadata, StateMachineMetadata> implements TransitionMetaBuilder {

    private TransitionTypeEnum type = TransitionTypeEnum.Common;
    private boolean conditional;
    private Class<?> conditionClass;
    private Class<? extends ConditionalTransition<?>> judgerClass;
    private boolean postValidate;
    private long timeout;

    protected TransitionMetaBuilderImpl(StateMachineMetadata parent, String name) {
        super(parent, "TransitionSet." + name);
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {}

    @Override
    public TransitionMetaBuilder build(Class<?> clazz, StateMachineMetadata parent) throws VerificationException {
        super.build(clazz, parent);
        configureSuper(clazz);
        configureCondition(clazz);
        configureType(clazz);
        configureTimeout(clazz);
        return this;
    }

    private void configureTimeout(Class<?> clazz) {
        final Timeout timeout = clazz.getAnnotation(Timeout.class);
        if ( null != timeout ) {
            this.timeout = timeout.value();
        }
    }

    private void configureType(Class<?> clazz) {
        if ( null != clazz.getAnnotation(Corrupt.class) ) {
            type = TransitionTypeEnum.Corrupt;
        } else if ( null != clazz.getAnnotation(Redo.class) ) {
            type = TransitionTypeEnum.Redo;
        } else if ( null != clazz.getAnnotation(Recover.class) ) {
            type = TransitionTypeEnum.Recover;
        } else if ( null != clazz.getAnnotation(Fail.class) ) {
            type = TransitionTypeEnum.Fail;
        } else {
            type = TransitionTypeEnum.Common;
        }
    }

    private void configureCondition(Class<?> clazz) throws VerificationException {
        Conditional conditionalAnno = clazz.getAnnotation(Conditional.class);
        if ( null != conditionalAnno ) {
            conditional = true;
            conditionClass = conditionalAnno.condition();
            judgerClass = conditionalAnno.judger();
            postValidate = conditionalAnno.postEval();
            verifyJudgerClass(clazz, judgerClass, conditionClass);
        } else {
            conditional = false;
        }
    }

    private void verifyJudgerClass(Class<?> clazz, Class<?> judgerClass, Class<?> conditionClass) throws VerificationException {
        for ( Type type : judgerClass.getGenericInterfaces() ) {
            if ( !( type instanceof ParameterizedType ) ) {
                continue;
            }
            final ParameterizedType pType = (ParameterizedType) type;
            if ( isConditionalTransition((Class<?>) pType.getRawType()) && !isConditionClassMatchingJudgerGenericType(conditionClass, pType) ) {
                throw newVerificationException(getDottedPath(), SyntaxErrors.TRANSITION_CONDITIONAL_CONDITION_NOT_MATCH_JUDGER, clazz, conditionClass,
                        judgerClass);
            }
        }
    }

    private boolean isConditionClassMatchingJudgerGenericType(Class<?> conditionClass, final ParameterizedType pType) {
        return conditionClass.isAssignableFrom((Class<?>) pType.getActualTypeArguments()[0]);
    }

    private boolean isConditionalTransition(final Class<?> rawType) {
        return ConditionalTransition.class.isAssignableFrom(rawType);
    }

    @Override
    public StateMachineMetadata getStateMachine() {
        return parent;
    }

    @Override
    public TransitionTypeEnum getType() {
        return type;
    }

    @Override
    public long getTimeout() {
        return this.timeout;
    }

    @Override
    public void dump(Dumper dumper) {}

    @Override
    public boolean isConditional() {
        return conditional;
    }

    @Override
    public Class<?> getConditionClass() {
        return conditionClass;
    }

    @Override
    public Class<? extends ConditionalTransition<?>> getJudgerClass() {
        return judgerClass;
    }

    @Override
    public boolean postValidate() {
        return postValidate;
    }

    @Override
    protected void verifySuper(Class<?> metaClass) throws VerificationException {
        if ( !parent.hasSuper() ) {
            throw newVerificationException(getDottedPath(), SyntaxErrors.TRANSITION_ILLEGAL_EXTENTION, metaClass, getSuperMetaClass(metaClass));
        } else {
            if ( !parent.getSuper().hasTransition(getSuperMetaClass(metaClass)) ) {
                throw newVerificationException(getDottedPath(), SyntaxErrors.TRANSITION_EXTENED_TRANSITION_CAN_NOT_FOUND_IN_SUPER_STATEMACHINE, metaClass,
                        getSuperMetaClass(metaClass), parent.getSuper().getPrimaryKey());
            }
        }
    }

    @Override
    protected TransitionMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return parent.getSuper().getTransition(metaClass);
    }

    @Override
    protected boolean extendsSuperKeySet() {
        return true;
    }

    @Override
    public void verifyTransitionMethod(Method method, VerificationFailureSet failureSet) {
        if ( method.getParameterTypes().length <= 0 ) {
            return;
        }
        if ( TransitionTypeEnum.Corrupt == getType() || TransitionTypeEnum.Recover == getType() || TransitionTypeEnum.Redo == getType() ) {
            failureSet.add(newVerificationFailure(getDottedPath(), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER, method,
                    StringUtil.toUppercaseFirstCharacter(method.getName()), getType()));
        }
    }
}
