package net.madz.lifecycle.meta.builder;

import net.madz.meta.MetaData;
import net.madz.meta.MetaDataBuilder;
import net.madz.verification.VerificationException;

public interface AnnotationMetaBuilder<SELF extends MetaData, PARENT extends MetaData> extends MetaDataBuilder<SELF, PARENT> {

    AnnotationMetaBuilder<SELF, PARENT> build(Class<?> klass, PARENT parent) throws VerificationException;
}