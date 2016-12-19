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
package net.imadz.lifecycle.meta.builder.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import net.imadz.bcel.intercept.Unlockable;
import net.imadz.bcel.intercept.UnlockableStack;
import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.LifecycleException;
import net.imadz.lifecycle.LifecycleLockStrategy;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.ReactiveObject;
import net.imadz.lifecycle.meta.builder.StateMachineObjectBuilder;
import net.imadz.lifecycle.meta.builder.StateObjectBuilder;
import net.imadz.lifecycle.meta.object.StateMachineObject;
import net.imadz.lifecycle.meta.object.StateObject;
import net.imadz.lifecycle.meta.type.RelationConstraintMetadata;
import net.imadz.lifecycle.meta.type.StateMetadata;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailureSet;

public class StateObjectBuilderImpl<S> extends ObjectBuilderBase<StateObject<S>, StateMachineObject<S>, StateMetadata> implements StateObjectBuilder<S> {

    private final HashMap<String, List<CallbackObject>> preFromStateChangeCallbacksMap = new HashMap<String, List<CallbackObject>>();
    private final HashMap<String, List<CallbackObject>> preToStateChangeCallbacksMap = new HashMap<String, List<CallbackObject>>();
    private final HashMap<String, List<CallbackObject>> postFromStateChangeCallbacksMap = new HashMap<String, List<CallbackObject>>();
    private final HashMap<String, List<CallbackObject>> postToStateChangeCallbacksMap = new HashMap<String, List<CallbackObject>>();

    protected StateObjectBuilderImpl(StateMachineObjectBuilder<S> parent, StateMetadata stateMetadata) {
        super(parent, "StateSet." + stateMetadata.getDottedPath().getName());
        this.setMetaType(stateMetadata);
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {}

    @Override
    public StateObjectBuilder<S> build(Class<?> klass, StateMachineObject<S> parent) throws VerificationException {
        super.build(klass, parent);
        return this;
    }

    @Override
    public void verifyValidWhile(Object target, RelationConstraintMetadata[] relationMetadataArray, final Object relatedTarget, UnlockableStack stack) {
        try {
            final StateMachineObject<?> relatedStateMachineObject = findRelatedStateMachineWithRelatedTarget(relationMetadataArray, relatedTarget);
            lockRelatedObject(relatedTarget, stack, relatedStateMachineObject);
            final String relatedStateName = relatedStateMachineObject.evaluateState(relatedTarget);
            boolean found = false;
            for ( RelationConstraintMetadata relationMetadata : relationMetadataArray ) {
                for ( StateMetadata stateMetadata : relationMetadata.getOnStates() ) {
                    if ( stateMetadata.getKeySet().contains(relatedStateName) ) {
                        found = true;
                        break;
                    }
                }
            }
            if ( !found ) {
                final LinkedHashSet<String> validRelationStates = new LinkedHashSet<String>();
                for ( RelationConstraintMetadata relationMetadata : relationMetadataArray ) {
                    for ( StateMetadata metadata : relationMetadata.getOnStates() ) {
                        validRelationStates.add(metadata.getSimpleName());
                    }
                }
                throw new LifecycleException(getClass(), LifecycleCommonErrors.BUNDLE, LifecycleCommonErrors.STATE_INVALID, target, this.getMetaType()
                        .getSimpleName(), relatedTarget, relatedStateName, Arrays.toString(validRelationStates.toArray(new String[0])));
            } else {
                relatedStateMachineObject.validateValidWhiles(relatedTarget, stack);
            }
        } catch (VerificationException e) {
            throw new IllegalStateException("Cannot happen, it should be defect of syntax verification.");
        }
    }

    private StateMachineObject<?> findRelatedStateMachineWithRelatedTarget(RelationConstraintMetadata[] relationMetadataArray, final Object relatedTarget)
            throws VerificationException {
        Class<?> relatedKey = null;
        if ( null != relatedTarget.getClass().getAnnotation(ReactiveObject.class) ) {
            final RelationConstraintMetadata relationConstraintMetadata = relationMetadataArray[0];
            relatedKey = findRelationKey(relatedTarget, relationConstraintMetadata);
        } else if (relatedTarget.getClass().getSuperclass() == java.lang.reflect.Proxy.class) {
            final RelationConstraintMetadata relationConstraintMetadata = relationMetadataArray[0];
            relatedKey = findRelationClass((Class<?>)relationConstraintMetadata.getRelatedStateMachine().getPrimaryKey(), relatedTarget.getClass().getInterfaces());
        } else {
            relatedKey = relatedTarget.getClass();
        }
        final StateMachineObject<?> relatedStateMachineObject = this.getRegistry().loadStateMachineObject(relatedKey);
        return relatedStateMachineObject;
    }

    private void lockRelatedObject(final Object relatedTarget, UnlockableStack stack, final StateMachineObject<?> relatedStateMachineObject) {
        if ( !relatedStateMachineObject.isLockEnabled() ) {
            return;
        }
        final LifecycleLockStrategy lifecycleLockStrategy = relatedStateMachineObject.getLifecycleLockStrategy();
        lifecycleLockStrategy.lockRead(relatedTarget);
        stack.pushUnlockable(new Unlockable() {

            @Override
            public void unlock() {
                lifecycleLockStrategy.unlockRead(relatedTarget);
            }
        });
    }

    @Override
    public void verifyInboundWhileAndLockRelatedObjects(Object eventKey, Object target, String nextState,
            RelationConstraintMetadata[] relationMetadataArray, Object relatedTarget, UnlockableStack stack) {
        try {
            final StateMachineObject<?> relatedStateMachineObject = findRelatedStateMachineWithRelatedTarget(relationMetadataArray, relatedTarget);
            lockRelatedObject(relatedTarget, stack, relatedStateMachineObject);
            final String relatedEvaluateState = relatedStateMachineObject.evaluateState(relatedTarget);
            boolean find = false;
            for ( RelationConstraintMetadata relationMetadata : relationMetadataArray ) {
                for ( StateMetadata stateMetadata : relationMetadata.getOnStates() ) {
                    if ( stateMetadata.getKeySet().contains(relatedEvaluateState) ) {
                        find = true;
                        break;
                    }
                }
            }
            if ( !find ) {
                final LinkedHashSet<String> validRelationStates = new LinkedHashSet<String>();
                for ( RelationConstraintMetadata relationMetadata : relationMetadataArray ) {
                    for ( StateMetadata metadata : relationMetadata.getOnStates() ) {
                        validRelationStates.add(metadata.getSimpleName());
                    }
                }
                throw new LifecycleException(getClass(), LifecycleCommonErrors.BUNDLE, LifecycleCommonErrors.VIOLATE_INBOUND_WHILE_RELATION_CONSTRAINT,
                        eventKey, nextState, target, relatedTarget, relatedEvaluateState, Arrays.toString(validRelationStates.toArray(new String[0])));
            } else {
                relatedStateMachineObject.validateValidWhiles(relatedTarget, stack);
            }
        } catch (VerificationException e) {
            throw new IllegalStateException("Cannot happen, it should be defect of syntax verification.");
        }
    }

    private Class<?> findRelationKey(Object relatedTarget, final RelationConstraintMetadata relationConstraintMetadata) {
        Class<?> relatedKey;
        final Class<?> stateMachineClass = (Class<?>) relationConstraintMetadata.getRelatedStateMachine().getPrimaryKey();
        final Class<?>[] interfaces = relatedTarget.getClass().getInterfaces();
        relatedKey = findRelationClass(stateMachineClass, interfaces);
        if ( null == relatedKey ) {
            throw new IllegalArgumentException("Cannot find " + stateMachineClass + " from " + Arrays.toString(interfaces));
        }
        return relatedKey;
    }

    private Class<?> findRelationClass(final Class<?> stateMachineClass, final Class<?>[] interfaces) {
        for ( final Class<?> interfaze : interfaces ) {
            for ( Annotation annotation : interfaze.getDeclaredAnnotations() ) {
                if ( LifecycleMeta.class == annotation.annotationType() ) {
                    LifecycleMeta meta = (LifecycleMeta) annotation;
                    if ( stateMachineClass == meta.value() ) {
                        return interfaze;
                    }
                }
            }
            if ( interfaze.getInterfaces().length > 0 ) {
                Class<?> result = findRelationClass(stateMachineClass, interfaze.getInterfaces());
                if ( null != result ) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public void invokeFromPreStateChangeCallbacks(LifecycleContext<?, S> callbackContext) {
        if ( preFromStateChangeCallbacksMap.containsKey(callbackContext.getFromStateName()) ) {
            interatorInvokeCallback(callbackContext, preFromStateChangeCallbacksMap.get(callbackContext.getFromStateName()));
        }
    }

    @Override
    public void invokeToPreStateChangeCallbacks(LifecycleContext<?, S> callbackContext) {
        if ( preToStateChangeCallbacksMap.containsKey(callbackContext.getToStateName()) ) {
            interatorInvokeCallback(callbackContext, preToStateChangeCallbacksMap.get(callbackContext.getToStateName()));
        }
    }

    @Override
    public void invokeFromPostStateChangeCallbacks(CallbackObject cbo, LifecycleContext<?, S> callbackContext) {
        if ( postFromStateChangeCallbacksMap.containsKey(callbackContext.getFromStateName()) ) {
        	cbo.doCallback(callbackContext);
        }
    }

    @Override
    public void invokeToPostStateChangeCallbacks(CallbackObject cbo, LifecycleContext<?, S> callbackContext) {
        if ( postToStateChangeCallbacksMap.containsKey(callbackContext.getToStateName()) ) {
            cbo.doCallback(callbackContext);
        }
    }

    private void interatorInvokeCallback(final LifecycleContext<?, S> callbackContext, List<CallbackObject> callbackObjects) {
        for ( CallbackObject callbackObject : callbackObjects ) {
            callbackObject.doCallback(callbackContext);
        }
    }

    @Override
    public void addPreToCallbackObject(Class<?> to, final CallbackObject callbackObject) {
        final String toStateClassName = to.getSimpleName();
        if ( this.preToStateChangeCallbacksMap.containsKey(toStateClassName) ) {
            this.preToStateChangeCallbacksMap.get(toStateClassName).add(callbackObject);
        } else {
            final List<CallbackObject> callbackObjects = new ArrayList<CallbackObject>();
            callbackObjects.add(callbackObject);
            this.preToStateChangeCallbacksMap.put(toStateClassName, callbackObjects);
        }
    }

    @Override
    public void addPreFromCallbackObject(Class<?> from, final CallbackObject callbackObject) {
        final String fromStateClassName = from.getSimpleName();
        if ( this.preFromStateChangeCallbacksMap.containsKey(fromStateClassName) ) {
            this.preFromStateChangeCallbacksMap.get(fromStateClassName).add(callbackObject);
        } else {
            final List<CallbackObject> callbackObjects = new ArrayList<CallbackObject>();
            callbackObjects.add(callbackObject);
            this.preFromStateChangeCallbacksMap.put(fromStateClassName, callbackObjects);
        }
    }

    @Override
    public void addPostToCallbackObject(Class<?> to, final CallbackObject item) {
        final String toStateClassName = to.getSimpleName();
        if ( this.postToStateChangeCallbacksMap.containsKey(toStateClassName) ) {
            this.postToStateChangeCallbacksMap.get(toStateClassName).add(item);
        } else {
            final List<CallbackObject> callbackObjects = new ArrayList<CallbackObject>();
            callbackObjects.add(item);
            this.postToStateChangeCallbacksMap.put(toStateClassName, callbackObjects);
        }
    }

    @Override
    public void addPostFromCallbackObject(Class<?> from, final CallbackObject item) {
        final String fromStateClassName = from.getSimpleName();
        if ( this.postFromStateChangeCallbacksMap.containsKey(fromStateClassName) ) {
            this.postFromStateChangeCallbacksMap.get(fromStateClassName).add(item);
        } else {
            final List<CallbackObject> callbackObjects = new ArrayList<CallbackObject>();
            callbackObjects.add(item);
            this.postFromStateChangeCallbacksMap.put(fromStateClassName, callbackObjects);
        }
    }

	@Override
	public List<CallbackObject> getPostFromCallbackObjects(final String stateName) {
		return this.postFromStateChangeCallbacksMap.get(stateName);
	}

	@Override
	public List<CallbackObject> getPostToCallbackObjects(final String stateName) {
		return this.postToStateChangeCallbacksMap.get(stateName);
	}
}
