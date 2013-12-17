/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.madz.lifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.bcel.intercept.DefaultStateMachineRegistry;
import net.madz.common.Dumper;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.meta.builder.StateMachineMetaBuilder;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.utils.BundleUtils;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;
import net.madz.verification.VerificationFailureSet;

public abstract class AbsStateMachineRegistry implements LifecycleMetaRegistry {

    private static Logger logger = Logger.getLogger("Lifecycle Framework");
    private static volatile LifecycleMetaRegistry instance = null;

    public static LifecycleMetaRegistry getInstance() {
        if ( null != instance ) {
            return instance;
        } else {
            final String registryClass = System.getProperty("net.madz.lifecycle.StateMachineRegistry");
            if ( null != registryClass ) {
                try {
                    // TODO There are two patterns to use registry:
                    // 1. using Annotation to register LifecycleMetadata
                    // classes.
                    // 2. using
                    // AbsStateMachineRegistry.registerLifecycleMeta(Class<?>
                    // class);
                    // For now ONLY 1st pattern is supported with extending
                    // AbsStateMachineRegistry.
                    Class.forName(registryClass).newInstance();
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Cannot Instantiate State Machine Registry: " + registryClass, t);
                    throw new IllegalStateException(t);
                }
            } else {
                DefaultStateMachineRegistry.getInstance();
            }
        }
        return instance;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface LifecycleRegistry {

        /**
         * @return classes can be the name of life cycle interface itself, which
         *         has a @StateMachine annotated on it's type.
         *         or the name of a class/interface that has a @LifecycleMeta
         *         annotated on the type.
         */
        Class<?>[] value();
    }
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface StateMachineBuilder {

        /**
         * @return a concrete StateMachineMetaBuilder implementation class,
         *         which can build state machines from value of @LifecycleRegisty
         */
        Class<? extends StateMachineMetaBuilder> value() default StateMachineMetaBuilderImpl.class;
    }

    /**
     * The key might be class object as:
     * The life cycle interface itself that has a @StateMachine,
     * or a class/interface that has a @LifecycleMeta
     * The key might be String as:
     * The full qualified name corresponds to the dotted path,
     * or simple name, or class full name
     */
    protected final HashMap<Object, StateMachineMetadata> typeMap = new HashMap<>();
    protected final HashMap<Object, StateMachineObject<?>> instanceMap = new HashMap<>();
    private final LifecycleRegistry lifecycleRegistry;
    private final StateMachineBuilder builderMeta;
    private LifecycleEventHandler lifecycleEventHandler;

    protected AbsStateMachineRegistry() throws VerificationException {
        instance = this;
        lifecycleRegistry = getClass().getAnnotation(LifecycleRegistry.class);
        builderMeta = getClass().getAnnotation(StateMachineBuilder.class);
        registerStateMachines();
    }

    /**
     * To process all the registered class to build the corresponding state
     * machines.
     */
    private synchronized void registerStateMachines() throws VerificationException {
        if ( null == lifecycleRegistry || null == builderMeta ) {
            throw new NullPointerException(
                    "A subclass of AbstractStateMachineRegistry must have both @LifecycleRegistry and @StateMachineBuilder annotated on Type: " + getClass());
        }
        final Class<?>[] toRegister = lifecycleRegistry.value();
        final VerificationFailureSet failureSet = new VerificationFailureSet();
        for ( Class<?> clazz : toRegister )
            registerLifecycleMeta(failureSet, clazz);
        if ( failureSet.size() > 0 ) {
            failureSet.dump(new Dumper(System.out));
            throw new VerificationException(failureSet);
        }
    }

    public void registerLifecycleMeta(final Class<?> clazz) throws VerificationException {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("registering .. " + clazz);
        }
        final VerificationFailureSet failureSet = new VerificationFailureSet();
        registerLifecycleMeta(failureSet, clazz);
        if ( failureSet.size() > 0 ) {
            failureSet.dump(new Dumper(System.out));
            throw new VerificationException(failureSet);
        }
    }

    private void registerLifecycleMeta(final VerificationFailureSet failureSet, Class<?> clazz) throws VerificationException {
        if ( LifecycleEventHandler.class.isAssignableFrom(clazz) ) {
            try {
                this.lifecycleEventHandler = (LifecycleEventHandler) clazz.newInstance();
            } catch (Exception e) {
                final String errorMessage = BundleUtils.getBundledMessage(getClass(), "syntax_error",
                        SyntaxErrors.LIFECYCLE_EVENT_HANDLER_MUST_HAVE_NO_ARG_CONSTRUCTOR, clazz);
                throw new VerificationException(new VerificationFailure(this, getClass().getName(),
                        SyntaxErrors.LIFECYCLE_EVENT_HANDLER_MUST_HAVE_NO_ARG_CONSTRUCTOR, errorMessage));
            }
        } else if ( null != clazz.getAnnotation(StateMachine.class) ) {
            if ( isMetaTypeRegistered(clazz) ) {
                return;
            }
            createStateMachineMetadata(clazz, null, failureSet);
        } else if ( null != clazz.getAnnotation(LifecycleMeta.class) ) {
            final Class<?> stateMachineClass = clazz.getAnnotation(LifecycleMeta.class).value();
            final StateMachineMetadata metaData;
            if ( !isMetaTypeRegistered(stateMachineClass) ) {
                metaData = createStateMachineMetadata(stateMachineClass, null, failureSet);
            } else {
                metaData = loadStateMachineMetadata(stateMachineClass);
            }
            if ( null == metaData ) {
                // Failed to create State machine Meta Builder will return
                // null.
                return;
            }
            if ( null == getStateMachineObject(clazz) ) {
                StateMachineObject<?> stateMachineInstance = metaData.newInstance(clazz);
                stateMachineInstance.verifyMetaData(failureSet);
                addInstance(clazz, stateMachineInstance);
            }
        } else {
            final String errorMessage = BundleUtils.getBundledMessage(getClass(), "syntax_error", SyntaxErrors.REGISTERED_META_ERROR, clazz);
            failureSet.add(new VerificationFailure(this, getClass().getName(), SyntaxErrors.REGISTERED_META_ERROR, errorMessage));
        }
    }

    private boolean isMetaTypeRegistered(Object key) {
        return null != getStateMachineMeta(key);
    }

    public synchronized void addInstance(Class<?> clazz, StateMachineObject<?> stateMachine) {
        if ( instanceMap.containsKey(stateMachine.getPrimaryKey()) ) {
            final StateMachineObject<?> existedStateMachine = instanceMap.get(stateMachine.getPrimaryKey());
            if ( !existedStateMachine.getDottedPath().equals(stateMachine.getDottedPath()) ) {
                throw new IllegalStateException("Same Key corresponds two different StateMachine: " + stateMachine.getPrimaryKey().toString() + ", one is : "
                        + existedStateMachine.getDottedPath() + " and another is:" + stateMachine.getDottedPath());
            }
        } else {
            instanceMap.put(stateMachine.getPrimaryKey(), stateMachine);
        }
    }

    public synchronized void addTemplate(final StateMachineMetadata metaData) {
        final Object key = metaData.getPrimaryKey();
        if ( isMetaTypeRegistered(key) ) {
            final StateMachineMetadata existedStateMachine = getStateMachineMeta(key);
            if ( !existedStateMachine.getDottedPath().equals(metaData.getDottedPath()) ) {
                throw new IllegalStateException("Same Key corresponds two different StateMachine: " + key.toString() + ", one is : "
                        + existedStateMachine.getDottedPath() + " and another is:" + metaData.getDottedPath());
            }
        } else {
            typeMap.put(key, metaData);
        }
    }

    private StateMachineMetaBuilder createBuilder(Class<?> metadataClass) throws VerificationException {
        try {
            final Constructor<? extends StateMachineMetaBuilder> c = builderMeta.value().getConstructor(AbsStateMachineRegistry.class, String.class);
            return c.newInstance(this, metadataClass.getName());
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }
    }

    public synchronized Map<Object, StateMachineMetadata> getStateMachineTypes() {
        return Collections.unmodifiableMap(this.typeMap);
    }

    public synchronized Map<Object, StateMachineObject<?>> getStateMachineInstances() {
        return Collections.unmodifiableMap(this.instanceMap);
    }

    public synchronized StateMachineMetadata getStateMachineMeta(Object key) {
        return this.typeMap.get(key);
    }

    public synchronized StateMachineObject<?> getStateMachineObject(Object key) {
        return this.instanceMap.get(key);
    }

    private StateMachineMetadata createStateMachineMetadata(Class<?> stateMachineClass, StateMachineMetadata owningStateMachine,
            VerificationFailureSet failureSet) throws VerificationException {
        StateMachineMetaBuilder metaBuilder = null;
        try {
            if ( null != stateMachineClass.getAnnotation(CompositeState.class) ) {
                metaBuilder = createCompositeBuilder(stateMachineClass, owningStateMachine);
            } else {
                metaBuilder = createBuilder(stateMachineClass);
            }
            final StateMachineMetadata metaData = metaBuilder.build(stateMachineClass, null).getMetaData();
            addTemplate(metaData);
            if ( null != failureSet ) {
                metaData.verifyMetaData(failureSet);
            } else {
                VerificationFailureSet tmpSet = new VerificationFailureSet();
                metaData.verifyMetaData(tmpSet);
                if ( 0 < tmpSet.size() ) {
                    throw new VerificationException(tmpSet);
                }
            }
            return metaData;
        } catch (VerificationException ex) {
            if ( null == failureSet ) {
                throw ex;
            } else {
                failureSet.add(ex);
            }
        }
        return null;
    }

    @Override
    public StateMachineMetadata loadStateMachineMetadata(Class<?> stateMachineClass) throws VerificationException {
        return loadStateMachineMetadata(stateMachineClass, null);
    }

    @Override
    public StateMachineObject<?> loadStateMachineObject(Class<?> stateMachineObjectClass) throws VerificationException {
        final StateMachineObject<?> stateMachineObject = getStateMachineObject(stateMachineObjectClass);
        if ( null != stateMachineObject ) {
            return stateMachineObject;
        } else {
            registerLifecycleMeta(stateMachineObjectClass);
            return getStateMachineObject(stateMachineObjectClass);
        }
    }

    @Override
    public StateMachineMetadata loadStateMachineMetadata(Class<?> stateMachineClass, StateMachineMetadata owningStateMachine) throws VerificationException {
        StateMachineMetadata stateMachineMeta = getStateMachineMeta(stateMachineClass);
        if ( null != stateMachineMeta ) return stateMachineMeta;
        return createStateMachineMetadata(stateMachineClass, owningStateMachine, null);
    }

    private StateMachineMetaBuilder createCompositeBuilder(Class<?> stateMachineClass, StateMachineMetadata owningStateMachine) throws VerificationException {
        Constructor<? extends StateMachineMetaBuilder> c;
        try {
            c = builderMeta.value().getConstructor(builderMeta.value(), String.class);
            final StateMachineMetaBuilder compositeStateMachine = c.newInstance(owningStateMachine,
                    "CompositeStateMachine." + stateMachineClass.getSimpleName());
            return compositeStateMachine;
        } catch (Throwable t) {
            if ( t.getCause() instanceof VerificationException ) {
                throw (VerificationException) t.getCause();
            }
            throw new IllegalStateException(t);
        }
    }

    public LifecycleEventHandler getLifecycleEventHandler() {
        return this.lifecycleEventHandler;
    }
}
