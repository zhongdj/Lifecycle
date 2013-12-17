package net.madz.lifecycle.meta.builder.impl;

import net.madz.lifecycle.meta.MetaObject;
import net.madz.lifecycle.meta.MetaType;
import net.madz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.madz.meta.MetaData;
import net.madz.verification.VerificationException;

public abstract class ObjectBuilderBase<SELF extends MetaObject<SELF, TYPE>, PARENT extends MetaData, TYPE extends MetaType<TYPE>> extends
        InheritableAnnotationMetaBuilderBase<SELF, PARENT> implements MetaObject<SELF, TYPE> {

    private TYPE metaType;

    public ObjectBuilderBase(PARENT parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean hasSuper() {
        return false;
    }

    @Override
    protected SELF findSuper(Class<?> metaClass) throws VerificationException {
        return null;
    }

    @Override
    public TYPE getMetaType() {
        return metaType;
    }

    protected void setMetaType(TYPE metaType) {
        this.metaType = metaType;
    }

    @Override
    public AnnotationMetaBuilder<SELF, PARENT> build(Class<?> klass, PARENT parent) throws VerificationException {
        setPrimaryKey(getMetaType().getPrimaryKey());
        addKey(getPrimaryKey());
        addKeys(getMetaType().getKeySet());
        return this;
    }
}