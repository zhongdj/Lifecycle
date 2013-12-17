package net.madz.lifecycle.meta.builder.impl;

import net.madz.common.DottedPath;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.madz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.madz.meta.MetaData;
import net.madz.meta.impl.MetaDataBuilderBase;
import net.madz.utils.BundleUtils;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;

public abstract class AnnotationMetaBuilderBase<SELF extends MetaData, PARENT extends MetaData> extends MetaDataBuilderBase<SELF, PARENT> implements
        AnnotationMetaBuilder<SELF, PARENT> {

    protected LifecycleMetaRegistry registry;
    private Object primaryKey;

    public AnnotationMetaBuilderBase(PARENT parent, String name) {
        super(parent, name);
    }

    public LifecycleMetaRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(LifecycleMetaRegistry registry) {
        this.registry = registry;
    }

    public VerificationException newVerificationException(String dottedPathName, String errorCode, Object... args) {
        return new VerificationException(newVerificationFailure(dottedPathName, errorCode, args));
    }

    public VerificationException newVerificationException(DottedPath dottedPath, String errorCode, Object... args) {
        return new VerificationException(newVerificationFailure(dottedPath.getAbsoluteName(), errorCode, args));
    }

    public VerificationFailure newVerificationFailure(DottedPath dottedPath, String errorCode, Object... args) {
        return newVerificationFailure(dottedPath.getAbsoluteName(), errorCode, args);
    }

    public VerificationFailure newVerificationFailure(String dottedPathName, String errorCode, Object... args) {
        return new VerificationFailure(this, dottedPathName, errorCode, BundleUtils.getBundledMessage(getClass(), SyntaxErrors.SYNTAX_ERROR_BUNDLE, errorCode,
                args));
    }

    protected void addKeys(Class<?> clazz) {
        addKey(getDottedPath());
        addKey(getDottedPath().getAbsoluteName());
        addKey(clazz);
        addKey(clazz.getName());
        addKey(clazz.getSimpleName());
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    protected void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public AnnotationMetaBuilder<SELF, PARENT> build(Class<?> klass, PARENT parent) throws VerificationException {
        setPrimaryKey(klass);
        addKeys(klass);
        return this;
    }
}