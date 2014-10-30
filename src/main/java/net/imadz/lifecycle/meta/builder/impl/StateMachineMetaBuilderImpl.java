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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.imadz.common.Dumper;
import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.action.ConditionSet;
import net.imadz.lifecycle.annotations.relation.Parent;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.meta.builder.ConditionMetaBuilder;
import net.imadz.lifecycle.meta.builder.RelationMetaBuilder;
import net.imadz.lifecycle.meta.builder.StateMachineMetaBuilder;
import net.imadz.lifecycle.meta.builder.StateMachineObjectBuilder;
import net.imadz.lifecycle.meta.builder.StateMetaBuilder;
import net.imadz.lifecycle.meta.builder.EventMetaBuilder;
import net.imadz.lifecycle.meta.object.StateMachineObject;
import net.imadz.lifecycle.meta.type.ConditionMetadata;
import net.imadz.lifecycle.meta.type.RelationMetadata;
import net.imadz.lifecycle.meta.type.StateMachineMetadata;
import net.imadz.lifecycle.meta.type.StateMetadata;
import net.imadz.lifecycle.meta.type.EventMetadata;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailureSet;

public class StateMachineMetaBuilderImpl extends InheritableAnnotationMetaBuilderBase<StateMachineMetadata, StateMachineMetadata> implements
        StateMachineMetaBuilder {

    private StateMachineMetadata parentStateMachineMetadata;
    /* //////////////////////////////////////////////////// */
    /* ////////////// Fields For Events ////////////// */
    /* //////////////////////////////////////////////////// */
    private final ArrayList<EventMetaBuilder> eventList = new ArrayList<EventMetaBuilder>();
    private final HashMap<Object, EventMetaBuilder> eventMap = new HashMap<Object, EventMetaBuilder>();
    private EventMetaBuilder corruptEvent;
    private EventMetaBuilder recoverEvent;
    private EventMetaBuilder redoEvent;
    @SuppressWarnings("unused")
    private EventMetaBuilder failEvent;
    /* //////////////////////////////////////////////////// */
    /* ////////////// Fields For Condition /////////////// */
    /* //////////////////////////////////////////////////// */
    private final ArrayList<ConditionMetaBuilder> conditionList = new ArrayList<ConditionMetaBuilder>();
    private final HashMap<Object, ConditionMetaBuilder> conditionMap = new HashMap<Object, ConditionMetaBuilder>();
    /* //////////////////////////////////////////////////// */
    /* /////////////////// Fields For State /////////////// */
    /* //////////////////////////////////////////////////// */
    private final ArrayList<StateMetaBuilder> stateList = new ArrayList<StateMetaBuilder>();
    private final HashMap<Object, StateMetaBuilder> stateMap = new HashMap<Object, StateMetaBuilder>();
    private ArrayList<StateMetaBuilder> finalStateList = new ArrayList<StateMetaBuilder>();
    private StateMetaBuilder initialState;
    /* //////////////////////////////////////////////////// */
    /* //////// Fields For Composite State Machine /////// */
    /* //////////////////////////////////////////////////// */
    // If this state machine is a composite state machine, this.composite = true
    private boolean composite;
    // If this state machine is a composite state machine, owningState is the
    // enclosing state
    private StateMetadata owningState;
    // If this state machine is a composite state machine, owningStateMachine is
    // the enclosing state's (parent) StateMachine
    // private StateMachineMetadata owningStateMachine;
    // Also for composite State Machine
    private final ArrayList<StateMetaBuilder> shortcutStateList = new ArrayList<StateMetaBuilder>();
    private final ArrayList<StateMachineMetaBuilder> compositeStateMachineList = new ArrayList<StateMachineMetaBuilder>();
    private final HashMap<Object, RelationMetadata> relationMetadataMap = new HashMap<Object, RelationMetadata>();
    private final ArrayList<RelationMetadata> relationList = new ArrayList<RelationMetadata>();

    public StateMachineMetaBuilderImpl(AbsStateMachineRegistry registry, String name) {
        super(null, name);
        this.registry = registry;
    }

    public StateMachineMetaBuilderImpl(StateMachineMetaBuilderImpl parent, String name) {
        super(parent, name);
        parent.compositeStateMachineList.add(this);
        this.registry = parent.getRegistry();
    }

    @Override
    public boolean hasSuper() {
        return null != this.getSuper();
    }

    @Override
    public boolean hasParent() {
        return null != parentStateMachineMetadata;
    }

    @Override
    public StateMetadata[] getDeclaredStateSet() {
        return stateList.toArray(new StateMetadata[stateList.size()]);
    }

    @Override
    public StateMetadata getDeclaredState(Object stateKey) {
        return stateMap.get(stateKey);
    }

    @Override
    public StateMetadata getInitialState() {
        return initialState;
    }

    @Override
    public StateMetadata[] getFinalStates() {
        return finalStateList.toArray(new StateMetadata[finalStateList.size()]);
    }

    @Override
    public EventMetadata[] getDeclaredEventSet() {
        return eventList.toArray(new EventMetadata[eventList.size()]);
    }

    @Override
    public EventMetadata getDeclaredEvent(Object eventKey) {
        return this.eventMap.get(eventKey);
    }

    @Override
    public EventMetadata getStateSynchronizationEvent() {
        return null;
    }

    @Override
    public StateMachineObject<?> newInstance(Class<?> clazz) throws VerificationException {
        final StateMachineObjectBuilder<?> builder = new StateMachineObjectBuilderImpl(this, clazz.getName());
        builder.setRegistry(registry);
        return (StateMachineObject<?>) builder.build(clazz, null).getMetaData();
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

    @Override
    public void dump(Dumper dumper) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean hasRedoEvent() {
        return null != redoEvent;
    }

    @Override
    public EventMetadata getRedoEvent() {
        return redoEvent;
    }

    @Override
    public boolean hasRecoverEvent() {
        return null != recoverEvent;
    }

    @Override
    public EventMetadata getRecoverEvent() {
        return recoverEvent;
    }

    @Override
    public boolean hasCorruptEvent() {
        return null != corruptEvent;
    }

    @Override
    public EventMetadata getCorruptEvent() {
        return corruptEvent;
    }

    /* //////////////////////////////////////////////////// */
    /* //////// Methods For Composite State Machine /////// */
    /* //////////////////////////////////////////////////// */
    @Override
    public boolean isComposite() {
        return composite;
    }

    @Override
    public void setComposite(boolean b) {
        this.composite = b;
    }

    @Override
    public StateMachineMetadata getOwningStateMachine() {
        return getParent();
    }

    @Override
    public StateMetadata getOwningState() {
        return owningState;
    }

    @Override
    public void setOwningState(StateMetadata stateMetaBuilder) {
        this.owningState = stateMetaBuilder;
    }

    /* //////////////////////////////////////////////////// */
    /* ////////////// Methods For Builder ///////////////// */
    /* //////////////////////////////////////////////////// */
    @Override
    public StateMachineMetaBuilder build(Class<?> clazz, StateMachineMetadata parent) throws VerificationException {
        super.build(clazz, parent);
        preConfigureStateMachineType(clazz);
        // Step 1. Syntax Validation
        {
            verifySyntax(clazz);
        }
        // Step 2. Configure StateMachine
        {
            configureSuper(clazz);
            configureConditionSet(clazz);
            configureEventSet(clazz);
            configureStateSetBasic(clazz);
            configureRelationSet(clazz);
            configureStateSetRelationConstraints(clazz);
            configureCompositeStateMachine(clazz);
            configureFunctions(clazz);
        }
        return this;
    }

    private void preConfigureStateMachineType(Class<?> clazz) {
        if ( isCompositeStateMachine(clazz) ) {
            setOwningState(getOwningStateMachine().getState(clazz));
            setComposite(true);
        }
    }

    private void configureCompositeStateMachine(Class<?> clazz) throws VerificationException {
        for ( final Class<?> stateClass : findComponentClasses(clazz, StateSet.class) ) {
            StateMetaBuilder stateMetaBuilder = this.stateMap.get(stateClass);
            stateMetaBuilder.configureCompositeStateMachine(stateClass);
            verifyCompositeParentRelationSyntax(clazz, stateClass, stateMetaBuilder.getCompositeStateMachine());
        }
    }

    private void verifyCompositeParentRelationSyntax(Class<?> owningStateMachineClass, final Class<?> stateClass,
            final StateMachineMetadata compositeStateMachine) throws VerificationException {
        final Class<?> owningParentRelation = getDeclaredParentRelation(owningStateMachineClass);
        if ( null != compositeStateMachine ) {
            final Class<?> compositeParentRelation = getDeclaredParentRelation(stateClass);
            if ( null != owningParentRelation && null != compositeParentRelation ) {
                throw newVerificationException(compositeStateMachine.getDottedPath(),
                        SyntaxErrors.RELATION_COMPOSITE_STATE_MACHINE_CANNOT_OVERRIDE_OWNING_PARENT_RELATION, compositeStateMachine.getDottedPath(),
                        compositeParentRelation, owningStateMachineClass, owningParentRelation);
            }
        }
    }

    private boolean isNotCompositeState(Class<?> superMetadataClass) {
        if ( isCompositeStateMachine(superMetadataClass) ) {
            return false;
        }
        if ( null != superMetadataClass.getAnnotation(End.class) ) {
            return true;
        } else if ( null != superMetadataClass.getAnnotation(Function.class) ) {
            return true;
        } else if ( null != superMetadataClass.getAnnotation(Functions.class) ) {
            return true;
        }
        return false;
    }

    private void configureFunctions(Class<?> clazz) throws VerificationException {
        for ( final Class<?> stateClass : findComponentClasses(clazz, StateSet.class) ) {
            final StateMetaBuilder stateMetaBuilder = this.stateMap.get(stateClass);
            stateMetaBuilder.configureFunctions(stateClass);
        }
    }

    private void configureStateSetRelationConstraints(Class<?> clazz) throws VerificationException {
        for ( final Class<?> stateClass : findComponentClasses(clazz, StateSet.class) ) {
            final StateMetaBuilder stateMetaBuilder = this.stateMap.get(stateClass);
            stateMetaBuilder.configureRelationConstrants(stateClass);
        }
    }

    private void configureRelationSet(Class<?> clazz) throws VerificationException {
        verifyParentRelationSyntax(clazz);
        for ( Class<?> relationClass : findComponentClasses(clazz, RelationSet.class) ) {
            addRelationMetadata(relationClass);
        }
    }

    private Class<?> getDeclaredParentRelation(Class<?> clazz) {
        for ( Class<?> relationClass : findComponentClasses(clazz, RelationSet.class) ) {
            if ( hasParent(relationClass) ) {
                return relationClass;
            }
        }
        return null;
    }

    private void verifyParentRelationSyntax(Class<?> clazz) throws VerificationException {
        boolean hasParentRelation = false;
        for ( Class<?> relationClass : findComponentClasses(clazz, RelationSet.class) ) {
            if ( hasParentRelation && hasParent(relationClass) ) {
                throw newVerificationException(getDottedPath(), SyntaxErrors.RELATION_MULTIPLE_PARENT_RELATION, clazz);
            } else if ( hasParent(relationClass) ) {
                hasParentRelation = true;
                if ( hasSuper() ) {
                    if ( getSuper().hasParent() && !hasLifecycleOverrideAnnotation(relationClass) ) {
                        throw newVerificationException(getDottedPath(), SyntaxErrors.RELATION_NEED_OVERRIDES_TO_OVERRIDE_SUPER_STATEMACHINE_PARENT_RELATION,
                                clazz, getSuperMetaClass(clazz));
                    }
                }
            }
        }
    }

    private boolean hasParent(Class<?> relationClass) {
        return null != relationClass.getAnnotation(Parent.class);
    }

    private void addRelationMetadata(Class<?> relationClass) throws VerificationException {
        final RelationMetaBuilder relationBuilder = new RelationMetaBuilderImpl(this, relationClass.getSimpleName());
        try {
            relationBuilder.build(relationClass, this);
        } catch (VerificationException e) {
            if ( SyntaxErrors.STATEMACHINE_CLASS_WITHOUT_ANNOTATION.equals(e.getVerificationFailureSet().iterator().next().getErrorCode()) ) {
                throw newVerificationException(getDottedPath(), SyntaxErrors.RELATION_RELATED_TO_REFER_TO_NON_STATEMACHINE,
                        relationClass.getAnnotation(RelateTo.class));
            } else {
                throw e;
            }
        }
        final RelationMetadata metaData = relationBuilder.getMetaData();
        this.relationList.add(metaData);
        final Iterator<Object> iterator = relationBuilder.getKeySet().iterator();
        while ( iterator.hasNext() ) {
            final Object next = iterator.next();
            this.relationMetadataMap.put(next, metaData);
            if ( metaData.isParent() ) {
                this.parentStateMachineMetadata = metaData.getRelateToStateMachine();
            }
        }
    }

    private void configureStateSetBasic(Class<?> clazz) throws VerificationException {
        final Class<?>[] stateClasses = findComponentClasses(clazz, StateSet.class);
        StateMetaBuilder stateBuilder = null;
        for ( Class<?> klass : stateClasses ) {
            verifyStateClassBasic(klass);
            stateBuilder = new StateMetaBuilderImpl(this, klass.getSimpleName());
            final StateMetaBuilder stateMetaBuilder = stateBuilder.build(klass, this);
            addStateMetadata(klass, stateMetaBuilder);
        }
    }

    private void verifyStateClassBasic(Class<?> klass) throws VerificationException {
        if ( hasLifecycleOverrideAnnotation(klass) ) {
            if ( klass.isInterface() ) {
                if ( 0 >= klass.getInterfaces().length ) {
                    throw newVerificationException(getDottedPath() + ".StateSet." + klass.getSimpleName(), SyntaxErrors.STATE_OVERRIDES_WITHOUT_SUPER_CLASS,
                            klass);
                }
            } else if ( null == klass.getSuperclass() ) {
                throw newVerificationException(getDottedPath() + ".StateSet." + klass.getSimpleName(), SyntaxErrors.STATE_OVERRIDES_WITHOUT_SUPER_CLASS, klass);
            }
        }
    }

    private void addStateMetadata(Class<?> stateClass, StateMetaBuilder stateMetadata) {
        this.stateList.add(stateMetadata);
        final Iterator<Object> iterator = stateMetadata.getKeySet().iterator();
        while ( iterator.hasNext() ) {
            this.stateMap.put(iterator.next(), stateMetadata);
        }
        if ( stateMetadata.isInitial() ) {
            this.initialState = stateMetadata;
        } else if ( stateMetadata.isFinal() ) {
            this.finalStateList.add(stateMetadata);
        }
        if ( null != stateClass.getAnnotation(ShortCut.class) ) {
            this.shortcutStateList.add(stateMetadata);
        }
    }

    private void configureEventSet(Class<?> clazz) throws VerificationException {
        final List<Class<?>> eventSetClasses = findComponentClass(clazz.getDeclaredClasses(), EventSet.class);
        if ( 0 >= eventSetClasses.size() ) {
            return;
        }
        final Class<?>[] eventClasses = eventSetClasses.get(0).getDeclaredClasses();
        EventMetaBuilder eventMetaBuilder = null;
        for ( Class<?> klass : eventClasses ) {
            eventMetaBuilder = new EventMetaBuilderImpl(this, klass.getSimpleName());
            final EventMetaBuilder eventMetadata = eventMetaBuilder.build(klass, this);
            addEventMetadata(clazz, eventMetadata);
        }
    }

    private void addEventMetadata(Class<?> eventClass, EventMetaBuilder eventMetadata) {
        this.eventList.add(eventMetadata);
        final Iterator<Object> iterator = eventMetadata.getKeySet().iterator();
        while ( iterator.hasNext() ) {
            this.eventMap.put(iterator.next(), eventMetadata);
        }
        switch (eventMetadata.getType()) {
            case Corrupt:
                this.corruptEvent = eventMetadata;
                break;
            case Recover:
                this.recoverEvent = eventMetadata;
                break;
            case Redo:
                this.redoEvent = eventMetadata;
                break;
            case Fail:
                this.failEvent = eventMetadata;
                break;
            default:
                break;
        }
    }

    private void configureConditionSet(Class<?> clazz) throws VerificationException {
        ConditionMetaBuilder conditionMetaBuilder = null;
        for ( Class<?> klass : findComponentClasses(clazz, ConditionSet.class) ) {
            conditionMetaBuilder = new ConditionMetaBuilderImpl(this, klass.getSimpleName()).build(klass, this);
            addConditionMetadata(clazz, conditionMetaBuilder);
        }
    }

    private void addConditionMetadata(Class<?> clazz, ConditionMetaBuilder conditionMetaBuilder) {
        this.conditionList.add(conditionMetaBuilder);
        final Iterator<Object> iterator = conditionMetaBuilder.getKeySet().iterator();
        while ( iterator.hasNext() ) {
            conditionMap.put(iterator.next(), conditionMetaBuilder);
        }
    }

    private boolean isCompositeStateMachine(Class<?> stateMachineClass) {
        return null != stateMachineClass.getAnnotation(CompositeState.class);
    }

    /* //////////////////////////////////////////////////// */
    /* ////////////// Methods For Syntax Verification ///// */
    /* //////////////////////////////////////////////////// */
    private void verifySyntax(Class<?> clazz) throws VerificationException {
        verifyStateMachineDefinition(clazz);
        verifyRequiredComponents(clazz);
        verifyOptionalComponents(clazz);
    }

    private void verifyOptionalComponents(Class<?> klass) throws VerificationException {
        verifyRelationSet(klass);
        verifyConditionSet(klass);
    }

    private void verifyRelationSet(Class<?> klass) throws VerificationException {
        final List<Class<?>> relationSetClass = findComponentClass(klass.getDeclaredClasses(), RelationSet.class);
        if ( 1 < relationSetClass.size() ) {
            throw newVerificationException(getDottedPath(), SyntaxErrors.RELATIONSET_MULTIPLE, klass);
        }
    }

    private void verifyConditionSet(Class<?> klass) throws VerificationException {
        final List<Class<?>> conditionSetClasses = findComponentClass(klass.getDeclaredClasses(), ConditionSet.class);
        if ( 1 < conditionSetClasses.size() ) {
            throw newVerificationException(getDottedPath(), SyntaxErrors.CONDITIONSET_MULTIPLE, klass);
        }
    }

    private void verifyRequiredComponents(Class<?> clazz) throws VerificationException {
        final String stateSetPath = clazz.getName() + ".StateSet";
        final String eventSetPath = clazz.getName() + ".EventSet";
        if ( hasSuper(clazz) ) {
            verifyStateOverrides(clazz);
            return;
        } else {
            final Class<?>[] declaredClasses = clazz.getDeclaredClasses();
            if ( 0 == declaredClasses.length ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES, new Object[] { clazz.getName() });
            }
            final List<Class<?>> stateClasses = findComponentClass(declaredClasses, StateSet.class);
            final List<Class<?>> eventClasses = findComponentClass(declaredClasses, EventSet.class);
            final VerificationFailureSet vs = new VerificationFailureSet();
            verifyStateSet(clazz, stateSetPath, stateClasses, vs);
            verifyEventSet(clazz, eventSetPath, eventClasses, vs);
            if ( vs.size() > 0 ) {
                throw new VerificationException(vs);
            }
        }
    }

    private void verifyStateOverrides(Class<?> clazz) throws VerificationException {
        final List<Class<?>> stateSetClasses = findComponentClass(clazz.getDeclaredClasses(), StateSet.class);
        if ( 0 >= stateSetClasses.size() ) {
            return;
        }
        Class<?> stateSetClass = stateSetClasses.get(0);
        final Class<?>[] stateClasses = stateSetClass.getDeclaredClasses();
        if ( 0 == stateClasses.length ) {
            return;
        }
        verifyStateOverridesInitial(stateClasses);
    }

    private void verifyStateOverridesInitial(Class<?>[] stateClasses) throws VerificationException {
        if ( 0 != findComponentClass(stateClasses, Initial.class).size() ) {
            return;
        }
        for ( Class<?> stateClass : stateClasses ) {
            if ( hasSuper(stateClass) ) {
                final Class<?> superStateClass = getSuperMetaClass(stateClass);
                if ( hasInitial(superStateClass) && hasLifecycleOverrideAnnotation(stateClass) ) {
                    throw newVerificationException(getDottedPath() + ".StateSet",
                            SyntaxErrors.STATESET_WITHOUT_INITAL_STATE_AFTER_OVERRIDING_SUPER_INITIAL_STATE, stateClass, superStateClass);
                }
            }
        }
    }

    private boolean hasInitial(Class<?> superClass) {
        return null != superClass.getAnnotation(Initial.class);
    }

    private VerificationFailureSet verifyStateSet(Class<?> clazz, final String stateSetPath, final List<Class<?>> stateClasses, final VerificationFailureSet vs) {
        if ( stateClasses.size() <= 0 ) {
            vs.add(newVerificationException(stateSetPath, SyntaxErrors.STATEMACHINE_WITHOUT_STATESET, clazz));
        } else if ( stateClasses.size() > 1 ) {
            vs.add(newVerificationException(stateSetPath, SyntaxErrors.STATEMACHINE_MULTIPLE_STATESET, clazz));
        } else {
            verifyStateSetComponent(stateSetPath, stateClasses.get(0), vs);
        }
        return vs;
    }

    private void verifyEventSet(Class<?> clazz, final String eventSetPath, final List<Class<?>> eventClasses, final VerificationFailureSet vs) {
        if ( eventClasses.size() <= 0 ) {
            vs.add(newVerificationException(eventSetPath, SyntaxErrors.STATEMACHINE_WITHOUT_TRANSITIONSET, clazz));
        } else if ( eventClasses.size() > 1 ) {
            vs.add(newVerificationException(eventSetPath, SyntaxErrors.STATEMACHINE_MULTIPLE_TRANSITIONSET, clazz));
        } else {
            verifyEventSetComponent(eventSetPath, eventClasses.get(0), vs);
        }
    }

    private void verifyEventSetComponent(final String dottedPath, final Class<?> eventClass, final VerificationFailureSet vs) {
        final Class<?>[] eventSetClasses = eventClass.getDeclaredClasses();
        if ( 0 == eventSetClasses.length ) {
            vs.add(newVerificationException(dottedPath, SyntaxErrors.TRANSITIONSET_WITHOUT_TRANSITION, eventClass));
        }
    }

    private void verifyStateSetComponent(final String stateSetPath, final Class<?> stateSetClass, final VerificationFailureSet vs) {
        final Class<?>[] stateSetClasses = stateSetClass.getDeclaredClasses();
        if ( 0 == stateSetClasses.length ) {
            vs.add(newVerificationException(stateSetPath, SyntaxErrors.STATESET_WITHOUT_STATE, stateSetClass));
        } else {
            List<Class<?>> initialClasses = findComponentClass(stateSetClasses, Initial.class);
            if ( initialClasses.size() == 0 ) {
                vs.add(newVerificationException(stateSetPath + ".Initial", SyntaxErrors.STATESET_WITHOUT_INITIAL_STATE, stateSetClass));
            } else if ( initialClasses.size() > 1 ) {
                vs.add(newVerificationException(stateSetPath + ".Initial", SyntaxErrors.STATESET_MULTIPLE_INITAL_STATES, stateSetClass));
            }
            List<Class<?>> endClasses = findComponentClass(stateSetClasses, End.class);
            if ( endClasses.size() == 0 ) {
                vs.add(newVerificationException(stateSetPath + ".Final", SyntaxErrors.STATESET_WITHOUT_FINAL_STATE, stateSetClass));
            }
        }
    }

    private void verifyStateMachineDefinition(Class<?> clazz) throws VerificationException {
        if ( clazz.isInterface() && clazz.getInterfaces().length <= 0 ) {
            if ( null == clazz.getAnnotation(StateMachine.class) && null == clazz.getAnnotation(CompositeState.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.STATEMACHINE_CLASS_WITHOUT_ANNOTATION, clazz);
            }
        }
        if ( isComposite() && getOwningStateMachine().equals(getSuper()) ) {
            throw newVerificationException(getDottedPath(), SyntaxErrors.COMPOSITE_STATEMACHINE_CANNOT_EXTENDS_OWNING_STATEMACHINE, clazz);
        }
    }

    @Override
    protected void verifySuper(Class<?> clazz) throws VerificationException {
        if ( !clazz.isInterface() && null != clazz.getSuperclass() ) {
            final Class<?> superclass = clazz.getSuperclass();
            if ( !isComposite() && !Object.class.equals(superclass) && null == superclass.getAnnotation(StateMachine.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE, superclass);
            }
        } else if ( clazz.isInterface() && clazz.getInterfaces().length > 0 ) {
            if ( clazz.getInterfaces().length > 1 ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.STATEMACHINE_HAS_ONLY_ONE_SUPER_INTERFACE, clazz);
            }
            final Class<?> clz = clazz.getInterfaces()[0];
            if ( isComposite() ) {
                //
            } else if ( null == clz.getAnnotation(StateMachine.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE, clz);
            }
        }
    }

    private Class<?>[] findComponentClasses(Class<?> clazz, Class<? extends Annotation> componentClass) {
        final List<Class<?>> stateSetClasses = findComponentClass(clazz.getDeclaredClasses(), componentClass);
        if ( 0 >= stateSetClasses.size() ) {
            return new Class<?>[0];
        }
        final Class<?>[] stateClasses = stateSetClasses.get(0).getDeclaredClasses();
        return stateClasses;
    }

    private List<Class<?>> findComponentClass(final Class<?>[] declaredClasses, Class<? extends Annotation> annotationClass) {
        ArrayList<Class<?>> stateClasses = new ArrayList<Class<?>>();
        for ( Class<?> klass : declaredClasses ) {
            for ( Annotation annotation : klass.getDeclaredAnnotations() ) {
                if ( annotation.annotationType().equals(annotationClass) ) {
                    stateClasses.add(klass);
                    break;
                }
            }
        }
        return stateClasses;
    }

    @Override
    public boolean hasRelation(Object relationKey) {
        if ( this.relationMetadataMap.containsKey(relationKey) ) {
            return true;
        }
        for ( StateMachineMetaBuilder builder : getCompositeStateMachines() ) {
            if ( builder.hasRelation(relationKey) ) {
                return true;
            }
        }
        if ( !isComposite() || ( !isComposite() && getOwningStateMachine().equals(getSuper()) ) ) {
            if ( null != getSuper() ) {
                if ( getSuper().hasRelation(relationKey) ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EventMetadata[] getAllEvents() {
        final ArrayList<EventMetadata> result = new ArrayList<EventMetadata>();
        loadEvents(this, result);
        return result.toArray(new EventMetadata[0]);
    }

    private void loadEvents(final StateMachineMetadata stateMachineMetaBuilder, final ArrayList<EventMetadata> result) {
        populateEvents(stateMachineMetaBuilder, result);
        for ( final StateMachineMetadata compositeStateMachine : stateMachineMetaBuilder.getCompositeStateMachines() ) {
            populateEvents(compositeStateMachine, result);
        }
        if ( null != stateMachineMetaBuilder.getSuper() ) {
            loadEvents(stateMachineMetaBuilder.getSuper(), result);
        }
    }

    private void populateEvents(StateMachineMetadata stateMachineMetaBuilder, ArrayList<EventMetadata> result) {
        for ( EventMetadata event : stateMachineMetaBuilder.getDeclaredEventSet() ) {
            result.add(event);
        }
    }

    @Override
    public EventMetadata getEvent(Object eventKey) {
        return findEvent(this, eventKey);
    }

    private EventMetadata findEvent(StateMachineMetadata stateMachineMetadata, Object eventKey) {
        if ( null == stateMachineMetadata ) return null;
        EventMetadata eventMetadata = stateMachineMetadata.getDeclaredEvent(eventKey);
        if ( null != eventMetadata ) {
            return eventMetadata;
        }
        for ( StateMachineMetadata builder : stateMachineMetadata.getCompositeStateMachines() ) {
            eventMetadata = builder.getDeclaredEvent(eventKey);
            if ( null != eventMetadata ) return eventMetadata;
        }
        return findEvent(stateMachineMetadata.getSuper(), eventKey);
    }

    @Override
    public boolean hasEvent(Object eventKey) {
        return null != getEvent(eventKey);
    }

    @Override
    public StateMachineMetaBuilder[] getCompositeStateMachines() {
        return compositeStateMachineList.toArray(new StateMachineMetaBuilder[0]);
    }

    @Override
    public StateMetadata[] getAllStates() {
        final ArrayList<StateMetadata> results = new ArrayList<StateMetadata>();
        final ArrayList<StateMetadata> overridedStates = new ArrayList<StateMetadata>();
        populateStateMetadataFromStateMachineHierarchy(this, results, overridedStates);
        return results.toArray(new StateMetadata[0]);
    }

    private void populateStateMetadataFromStateMachineHierarchy(final StateMachineMetadata stateMachine, final ArrayList<StateMetadata> results,
            final ArrayList<StateMetadata> extendedStates) {
        if ( null == stateMachine ) {
            return;
        }
        {// populate declared states
            for ( StateMetadata stateMetadata : stateMachine.getDeclaredStateSet() ) {
                if ( extendedStates.contains(stateMetadata) ) {
                    continue;
                }
                results.add(stateMetadata);
                markExtendedStates(extendedStates, stateMetadata.getSuper());
            }
        }
        for ( final StateMachineMetadata stateMachineMeta : stateMachine.getCompositeStateMachines() ) {
            if ( !extendedStates.contains(stateMachineMeta.getOwningState()) ) {
                populateStateMetadataFromStateMachineHierarchy(stateMachineMeta, results, extendedStates);
            }
        }
        if ( !stateMachine.isComposite() || stateMachine.isComposite() && !stateMachine.getOwningState().isOverriding() ) {
            populateStateMetadataFromStateMachineHierarchy(stateMachine.getSuper(), results, extendedStates);
        }
    }

    private void markExtendedStates(ArrayList<StateMetadata> extendedStates, StateMetadata superStateMetadata) {
        if ( null == superStateMetadata ) {
            return;
        }
        extendedStates.add(superStateMetadata);
        if ( null != superStateMetadata.getSuper() ) {
            markExtendedStates(extendedStates, superStateMetadata.getSuper());
        }
    }

    @Override
    public StateMetadata getState(Object stateKey) {
        return findState(this, stateKey);
    }

    private StateMetadata findState(final StateMachineMetadata stateMachine, final Object stateKey) {
        if ( null == stateMachine ) {
            return null;
        }
        final StateMetadata declaredState = stateMachine.getDeclaredState(stateKey);
        if ( null != declaredState ) {
            return declaredState;
        }
        for ( final StateMachineMetadata stateMachineMetadata : stateMachine.getCompositeStateMachines() ) {
            final StateMetadata state = stateMachineMetadata.getDeclaredState(stateKey);
            if ( null != state ) {
                return state;
            }
        }
        return findState(stateMachine.getSuper(), stateKey);
    }

    @Override
    public ConditionMetadata[] getDeclaredConditions() {
        return this.conditionList.toArray(new ConditionMetadata[0]);
    }

    @Override
    public ConditionMetadata[] getAllCondtions() {
        final LinkedList<ConditionMetadata> conditions = new LinkedList<ConditionMetadata>();
        getCondition(this, conditions);
        return conditions.toArray(new ConditionMetadata[0]);
    }

    private void getCondition(final StateMachineMetadata stateMachine, final LinkedList<ConditionMetadata> conditions) {
        if ( null == stateMachine ) {
            return;
        }
        for ( ConditionMetadata conditionMetadata : stateMachine.getDeclaredConditions() ) {
            conditions.add(conditionMetadata);
        }
        for ( StateMachineMetadata item : stateMachine.getCompositeStateMachines() ) {
            for ( ConditionMetadata conditionMetadata : item.getDeclaredConditions() ) {
                conditions.add(conditionMetadata);
            }
        }
        getCondition(stateMachine.getSuper(), conditions);
    }

    @Override
    public ConditionMetadata getCondtion(Object conditionKey) {
        if ( null != this.conditionMap.get(conditionKey) ) {
            return this.conditionMap.get(conditionKey);
        }
        for ( StateMachineMetaBuilder item : this.compositeStateMachineList ) {
            if ( null != item.getCondtion(conditionKey) ) {
                return item.getCondtion(conditionKey);
            }
        }
        if ( hasSuper() ) {
            return getSuper().getCondtion(conditionKey);
        }
        return null;
    }

    @Override
    public boolean hasCondition(Object conditionKey) {
        if ( this.conditionMap.containsKey(conditionKey) ) {
            return true;
        }
        for ( StateMachineMetaBuilder stateMachineMetaBuilder : this.getCompositeStateMachines() ) {
            if ( stateMachineMetaBuilder.hasCondition(conditionKey) ) {
                return true;
            }
        }
        if ( hasSuper() ) {
            return getSuper().hasCondition(conditionKey);
        }
        return false;
    }

    @Override
    protected StateMachineMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return registry.loadStateMachineMetadata(metaClass);
    }

    @Override
    protected boolean hasSuper(Class<?> metaClass) {
        if ( !super.hasSuper(metaClass) ) {
            return false;
        }
        if ( isComposite() ) {
            if ( isNotCompositeState(getSuperMetaClass(metaClass)) ) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public RelationMetadata getDeclaredRelationMetadata(Object relationKey) {
        return this.relationMetadataMap.get(relationKey);
    }

    @Override
    public RelationMetadata getRelationMetadata(Object relationKey) {
        return findRelation(this, relationKey);
    }

    private RelationMetadata findRelation(StateMachineMetadata stateMachineMetadata, Object relationKey) {
        RelationMetadata relationMetadata = stateMachineMetadata.getDeclaredRelationMetadata(relationKey);
        if ( null != relationMetadata ) {
            return relationMetadata;
        }
        if ( stateMachineMetadata.isComposite() ) {
            relationMetadata = stateMachineMetadata.getOwningStateMachine().getRelationMetadata(relationKey);
            if ( null != relationMetadata ) {
                return relationMetadata;
            }
        }
        for ( StateMachineMetadata builder : stateMachineMetadata.getCompositeStateMachines() ) {
            relationMetadata = builder.getDeclaredRelationMetadata(relationKey);
            if ( null != relationMetadata ) return relationMetadata;
        }
        if ( stateMachineMetadata.hasSuper() ) {
            return findRelation(stateMachineMetadata.getSuper(), relationKey);
        } else {
            throw new IllegalStateException("Cannot find relation with Key: " + relationKey);
        }
    }
}
