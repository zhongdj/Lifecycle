package net.madz.lifecycle.meta.builder.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.meta.Inheritable;
import net.madz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.madz.meta.MetaData;
import net.madz.verification.VerificationException;

public abstract class InheritableAnnotationMetaBuilderBase<SELF extends MetaData, PARENT extends MetaData> extends AnnotationMetaBuilderBase<SELF, PARENT>
        implements AnnotationMetaBuilder<SELF, PARENT>, Inheritable<SELF> {

    protected static Logger logger = Logger.getLogger("Lifecycle Framework");
    private SELF superMeta;
    private boolean overriding;

    protected InheritableAnnotationMetaBuilderBase(PARENT parent, String name) {
        super(parent, name);
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine(getDottedPath().getAbsoluteName());
        }
    }

    @Override
    public boolean hasSuper() {
        return null != superMeta;
    }

    @Override
    public SELF getSuper() {
        return superMeta;
    }

    @Override
    public boolean isOverriding() {
        return overriding;
    }

    protected void setOverriding(boolean overriding) {
        this.overriding = overriding;
    }

    protected void setSuper(SELF superMeta) {
        this.superMeta = superMeta;
    }

    protected void configureSuper(Class<?> metaClass) throws VerificationException {
        if ( !hasSuper(metaClass) ) {
            this.setOverriding(false);
            return;
        }
        verifySuper(metaClass);
        setSuper(findSuper(getSuperMetaClass(metaClass)));
        if ( hasLifecycleOverrideAnnotation(metaClass) ) {
            this.setOverriding(true);
        } else {
            this.setOverriding(false);
        }
        if ( extendsSuperKeySet() ) {
            final Iterator<Object> iterator = getSuper().getKeySet().iterator();
            while ( iterator.hasNext() ) {
                addKey(iterator.next());
            }
        }
    }

    protected boolean extendsSuperKeySet() {
        return false;
    }

    protected void verifySuper(Class<?> metaClass) throws VerificationException {}

    public boolean hasLifecycleOverrideAnnotation(AnnotatedElement metaClass) {
        return null != metaClass.getAnnotation(LifecycleOverride.class);
    }

    protected abstract SELF findSuper(Class<?> metaClass) throws VerificationException;

    protected Class<?> getSuperMetaClass(Class<?> clazz) {
        // if ( !hasSuper(clazz) ) {
        // throw new IllegalStateException("Class " + clazz +
        // " has no super class");
        // }
        if ( null != clazz.getSuperclass() && !Object.class.equals(clazz.getSuperclass()) ) {
            return clazz.getSuperclass();
        } else {
            // if clazz is interface or clazz implements an interface.
            return clazz.getInterfaces()[0];
        }
    }

    protected boolean hasSuper(Class<?> metaClass) {
        return ( null != metaClass.getSuperclass() && !Object.class.equals(metaClass.getSuperclass()) ) || ( 1 <= metaClass.getInterfaces().length );
    }

    protected boolean hasDeclaredAnnotation(Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        for ( Annotation anno : clazz.getDeclaredAnnotations() ) {
            if ( annotationClass == anno.annotationType() ) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected <A extends Annotation> A getDeclaredAnnotation(Class<?> metaClass, Class<A> annotationClass) {
        for ( Annotation annotation : metaClass.getDeclaredAnnotations() ) {
            if ( annotationClass == annotation.annotationType() ) {
                return (A) annotation;
            }
        }
        return null;
    }
}