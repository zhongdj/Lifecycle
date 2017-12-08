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

import net.imadz.common.DottedPath;
import net.imadz.common.Dumper;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.Transitions;
import net.imadz.lifecycle.annotations.relation.ErrorMessage;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.InboundWhiles;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.relation.ValidWhiles;
import net.imadz.lifecycle.annotations.state.Corrupted;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.Running;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.annotations.state.Stopped;
import net.imadz.lifecycle.annotations.state.Waiting;
import net.imadz.lifecycle.meta.builder.StateMachineMetaBuilder;
import net.imadz.lifecycle.meta.builder.StateMetaBuilder;
import net.imadz.lifecycle.meta.object.ErrorMessageObject;
import net.imadz.lifecycle.meta.object.FunctionMetadata;
import net.imadz.lifecycle.meta.type.EventMetadata;
import net.imadz.lifecycle.meta.type.RelationConstraintMetadata;
import net.imadz.lifecycle.meta.type.RelationMetadata;
import net.imadz.lifecycle.meta.type.StateMachineMetadata;
import net.imadz.lifecycle.meta.type.StateMetadata;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailureSet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class StateMetaBuilderImpl extends InheritableAnnotationMetaBuilderBase<StateMetadata, StateMachineMetadata> implements StateMetaBuilder {

  private boolean end;
  private boolean initial;
  private boolean compositeState;
  private StateMachineMetadata compositeStateMachine;
  private StateMetadata owningState;
  private LinkedList<RelationConstraintMetadata> validWhileRelations = new LinkedList<RelationConstraintMetadata>();
  private LinkedList<RelationConstraintMetadata> inboundWhileRelations = new LinkedList<RelationConstraintMetadata>();
  private HashMap<Object, FunctionMetadata> functionMetadataMap = new HashMap<Object, FunctionMetadata>();
  private ArrayList<EventMetadata> possibleLeavingEventList = new ArrayList<EventMetadata>();
  private ArrayList<EventMetadata> possibleReachingEventList = new ArrayList<EventMetadata>();
  private ArrayList<FunctionMetadata> functionMetadataList = new ArrayList<FunctionMetadata>();
  private HashMap<Object, EventMetadata> possibleEventMap = new HashMap<Object, EventMetadata>();
  private StateMetadata shortcutState;
  private StateTypeEnum type;
  private EventMetadata corruptEvent;
  private EventMetadata recoverEvent;
  private EventMetadata redoEvent;

  protected StateMetaBuilderImpl(StateMachineMetaBuilder parent, String name) {
    super(parent, "StateSet." + name);
  }

  @Override
  public void verifyMetaData(VerificationFailureSet verificationSet) {
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

  @Override
  public void dump(Dumper dumper) {
  }

  @Override
  public StateMachineMetadata getStateMachine() {
    return parent;
  }

  @Override
  public String getSimpleName() {
    return getDottedPath().getName();
  }

  @Override
  public boolean isInitial() {
    return initial;
  }

  @Override
  public boolean isFinal() {
    return end;
  }

  @Override
  public EventMetadata[] getPossibleLeavingEvents() {
    return this.possibleLeavingEventList.toArray(new EventMetadata[0]);
  }

  @Override
  public FunctionMetadata[] getDeclaredFunctionMetadata() {
    return this.functionMetadataList.toArray(new FunctionMetadata[0]);
  }

  @Override
  public EventMetadata getEvent(Object eventKey) {
    EventMetadata eventMetadata = null;
    if (this.parent.isComposite()) {
      eventMetadata = this.parent.getOwningState().getEvent(eventKey);
    }
    if (isOverriding() || !hasSuper()) {
      if (null == eventMetadata) {
        eventMetadata = getDeclaredPossibleEvent(eventKey);
      }
      if (null == eventMetadata) {
        return null;
      } else {
        return eventMetadata;
      }
    } else {// if ( hasSuper() && !isOverriding() ) {
      if (null == eventMetadata) {
        eventMetadata = getDeclaredPossibleEvent(eventKey);
      }
      if (null != eventMetadata) {
        return eventMetadata;
      } else {
        return this.getSuper().getEvent(eventKey);
      }
    }
  }

  private EventMetadata getDeclaredPossibleEvent(Object eventKey) {
    return possibleEventMap.get(eventKey);
  }

  @Override
  public boolean isEventValid(Object eventKey) {
    return null != getEvent(eventKey);
  }

  @Override
  public boolean hasInboundWhiles() {
    return null != this.inboundWhileRelations && this.inboundWhileRelations.size() > 0;
  }

  @Override
  public RelationConstraintMetadata[] getDeclaredInboundWhiles() {
    return this.inboundWhileRelations.toArray(new RelationConstraintMetadata[0]);
  }

  @Override
  public RelationConstraintMetadata[] getInboundWhiles() {
    final ArrayList<RelationConstraintMetadata> result = new ArrayList<RelationConstraintMetadata>();
    getInboundWhileRelationMetadataRecursively(this, result);
    return result.toArray(new RelationConstraintMetadata[0]);
  }

  @Override
  public boolean hasValidWhiles() {
    return null != this.validWhileRelations && this.validWhileRelations.size() > 0;
  }

  @Override
  public RelationConstraintMetadata[] getValidWhiles() {
    final ArrayList<RelationConstraintMetadata> result = new ArrayList<RelationConstraintMetadata>();
    getValidWhileRelationMetadataRecursively(this, result);
    return result.toArray(new RelationConstraintMetadata[0]);
  }

  private void getValidWhileRelationMetadataRecursively(StateMetadata stateMetadata, ArrayList<RelationConstraintMetadata> result) {
    final RelationConstraintMetadata[] declaredValidWhiles = stateMetadata.getDeclaredValidWhiles();
    for (final RelationConstraintMetadata relationMetadata : declaredValidWhiles) {
      result.add(relationMetadata);
    }
    if (parent.isComposite()) {
      for (final RelationConstraintMetadata relationMetadata : parent.getOwningState().getValidWhiles()) {
        result.add(relationMetadata);
      }
    }
    if (isOverriding()) {
      return;
    } else {
      if (null != stateMetadata.getSuper()) {
        getValidWhileRelationMetadataRecursively(stateMetadata.getSuper(), result);
      }
    }
  }

  private void getInboundWhileRelationMetadataRecursively(StateMetadata stateMetadata, ArrayList<RelationConstraintMetadata> result) {
    final RelationConstraintMetadata[] declaredInboundWhiles = stateMetadata.getDeclaredInboundWhiles();
    for (final RelationConstraintMetadata relationMetadata : declaredInboundWhiles) {
      result.add(relationMetadata);
    }
    if (parent.isComposite()) {
      for (final RelationConstraintMetadata relationMetadata : parent.getOwningState().getInboundWhiles()) {
        result.add(relationMetadata);
      }
    }
    if (isOverriding()) {
      return;
    } else {
      if (null != stateMetadata.getSuper()) {
        getValidWhileRelationMetadataRecursively(stateMetadata.getSuper(), result);
      }
    }
  }

  @Override
  public RelationConstraintMetadata[] getDeclaredValidWhiles() {
    return this.validWhileRelations.toArray(new RelationConstraintMetadata[0]);
  }

  @Override
  public boolean isCompositeState() {
    return compositeState;
  }

  @Override
  public StateMetadata getOwningState() {
    return owningState;
  }

  @Override
  public StateMachineMetadata getCompositeStateMachine() {
    return compositeStateMachine;
  }

  @Override
  public StateMetadata getLinkTo() {
    return shortcutState;
  }

  @Override
  public StateMetaBuilder build(Class<?> clazz, StateMachineMetadata parent) throws VerificationException {
    super.build(clazz, parent);
    verifyBasicSyntax(clazz);
    configureOwningState(parent);
    configureSuper(clazz);
    configureStateType(clazz);
    configureShortcutState(clazz, parent);
    return this;
  }

  private void configureOwningState(StateMachineMetadata parent) {
    if (parent.isComposite()) {
      this.owningState = parent.getOwningState();
    }
  }

  private void configureStateType(Class<?> clazz) {
    if (isOverriding()) {
      for (Annotation anno : clazz.getDeclaredAnnotations()) {
        if (Initial.class == anno.annotationType()) {
          this.initial = true;
        } else if (Final.class == anno.annotationType()) {
          this.end = true;
        }
      }
    } else {
      if (null != clazz.getAnnotation(Initial.class)) {
        this.initial = true;
      } else if (null != clazz.getAnnotation(Final.class)) {
        this.end = true;
      }
    }
    if (null != clazz.getAnnotation(Corrupted.class)) {
      setType(StateTypeEnum.Corrupted);
    }
    if (null != clazz.getAnnotation(Running.class)) {
      setType(StateTypeEnum.Running);
    }
    if (null != clazz.getAnnotation(Stopped.class)) {
      setType(StateTypeEnum.Stopped);
    }
    if (null != clazz.getAnnotation(Waiting.class)) {
      setType(StateTypeEnum.Waiting);
    }
  }

  private void configureShortcutState(Class<?> clazz, StateMachineMetadata parent) {
    if (!parent.isComposite()) {
      return;
    }
    if (!isFinal()) {
      return;
    }
    final ShortCut shortCut = clazz.getAnnotation(ShortCut.class);
    this.shortcutState = parent.getOwningStateMachine().getState(shortCut.value());
  }

  private void verifyBasicSyntax(Class<?> clazz) throws VerificationException {
    verifyShortcutSyntax(clazz);
  }

  private void verifyShortcutSyntax(Class<?> clazz) throws VerificationException {
    if (!parent.isComposite()) {
      return;
    }
    if (isFinalState(clazz) && !isShortCut(clazz)) {
      throw newVerificationException(getDottedPath(), SyntaxErrors.COMPOSITE_STATEMACHINE_FINAL_STATE_WITHOUT_SHORTCUT, clazz);
    } else if (isShortCut(clazz) && !isFinalState(clazz)) {
      throw newVerificationException(getDottedPath(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_WITHOUT_END, clazz);
    } else if (isShortCut(clazz)) {
      final ShortCut shortCut = clazz.getAnnotation(ShortCut.class);
      final Class<?> targetStateClass = shortCut.value();
      StateMetadata found = findStateMetadata(targetStateClass, parent.getOwningStateMachine());
      if (null == found) {
        throw newVerificationException(getDottedPath(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_STATE_INVALID, shortCut, clazz,
            targetStateClass);
      }
    }
  }

  private boolean isShortCut(Class<?> clazz) {
    return null != clazz.getAnnotation(ShortCut.class);
  }

  @Override
  public void configureFunctions(Class<?> stateClass) throws VerificationException {
    for (Transition function : verifyFunctions(stateClass)) {
      verifyFunction(stateClass, function);
      configureFunction(this, function);
    }
  }

  @Override
  public void configureCompositeStateMachine(Class<?> stateClass) throws VerificationException {
    final CompositeState csm = stateClass.getAnnotation(CompositeState.class);
    if (null == csm) {
      return;
    }
    this.setCompositeState(true);
    this.compositeStateMachine = parent.getRegistry().loadStateMachineMetadata(stateClass, parent);
  }

  private ArrayList<Transition> verifyFunctions(Class<?> stateClass) throws VerificationException {
    if (isFinalState(stateClass)) {
      return new ArrayList<Transition>();
    }
    final ArrayList<Transition> functionList = new ArrayList<Transition>();
    final HashSet<Class<?>> eventClassSet = new HashSet<Class<?>>();
    if (null != stateClass.getAnnotation(Transition.class)) {
      final Transition function = stateClass.getAnnotation(Transition.class);
      addFunction(stateClass, functionList, eventClassSet, function);
    } else if (null != stateClass.getAnnotation(Transitions.class)) {
      for (Transition function : stateClass.getAnnotation(Transitions.class).value()) {
        addFunction(stateClass, functionList, eventClassSet, function);
      }
    }
    if (0 == functionList.size() && null != this.getSuper()) {
      return new ArrayList<Transition>();
    }
    if (0 == functionList.size() && !this.isCompositeState()) {
      throw newVerificationException(getDottedPath().getAbsoluteName(), SyntaxErrors.STATE_NON_FINAL_WITHOUT_FUNCTIONS, stateClass.getName());
    }
    return functionList;
  }

  private void addFunction(Class<?> stateClass, final ArrayList<Transition> functionList, final HashSet<Class<?>> eventClassSet, Transition function)
      throws VerificationException {
    if (eventClassSet.contains(function.event()) || !isOverriding() && superStateHasFunction(function.event())) {
      throw newVerificationException(getDottedPath(), SyntaxErrors.STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_EVENT, stateClass,
          function.event());
    } else {
      functionList.add(function);
      eventClassSet.add(function.event());
    }
  }

  private boolean superStateHasFunction(Class<?> eventClass) {
    for (StateMetadata metadata = isOverriding() ? null : getSuper(); null != metadata; metadata = metadata.isOverriding() ? null : metadata
        .getSuper()) {
      if (null != metadata.getDeclaredFunctionMetadata(eventClass)) {
        return true;
      }
    }
    return false;
  }

  private void configureFunction(StateMetaBuilderImpl parent, Transition function) {
    final EventMetadata event = findEvent(parent.getParent(), function.event());
    Class<?>[] value = function.value();
    final LinkedList<StateMetadata> nextStates = new LinkedList<StateMetadata>();
    for (Class<?> item : value) {
      StateMetaBuilder nextState = (StateMetaBuilder) parent.getParent().getState(item);
      nextState.addPossibleReachingEvent(event);
      nextStates.add(nextState);
    }
    final FunctionMetadata functionMetadata = new FunctionMetadata(parent, event, nextStates);
    this.functionMetadataList.add(functionMetadata);
    this.possibleLeavingEventList.add(event);
    final Iterator<Object> iterator = event.getKeySet().iterator();
    while (iterator.hasNext()) {
      final Object next = iterator.next();
      this.functionMetadataMap.put(next, functionMetadata);
      this.possibleEventMap.put(next, event);
    }
    switch (event.getType()) {
      case Corrupt:
        this.corruptEvent = event;
        break;
      case Recover:
        this.recoverEvent = event;
        break;
      case Redo:
        this.redoEvent = event;
        break;
      case Common:
      case Fail:
      case Other:
        break;
    }
  }

  private void verifyFunction(Class<?> stateClass, Transition function) throws VerificationException {
    Class<?> eventClass = function.event();
    Class<?>[] stateCandidates = function.value();
    final VerificationFailureSet failureSet = new VerificationFailureSet();
    final EventMetadata event = findEvent(parent, eventClass);
    if (null == event) {
      failureSet.add(newVerificationFailure(getDottedPath().getAbsoluteName(), SyntaxErrors.FUNCTION_INVALID_EVENT_REFERENCE, function, stateClass,
          eventClass));
    }
    if (0 == stateCandidates.length) {
      failureSet.add(newVerificationFailure(getDottedPath().getAbsoluteName(), SyntaxErrors.FUNCTION_WITH_EMPTY_STATE_CANDIDATES, function,
          stateClass.getName(), eventClass.getName()));
    } else if (1 < stateCandidates.length) {
      if (!event.isConditional()) {
        failureSet.add(newVerificationFailure(event.getDottedPath().getAbsoluteName(),
            SyntaxErrors.FUNCTION_CONDITIONAL_EVENT_WITHOUT_CONDITION, function, stateClass.getName(), eventClass.getName()));
      }
    }
    for (int i = 0; i < stateCandidates.length; i++) {
      final Class<?> stateCandidateClass = stateCandidates[i];
      StateMetadata stateMetadata = findSuper(stateCandidateClass);
      if (null == stateMetadata) {
        failureSet.add(newVerificationFailure(getDottedPath().getAbsoluteName(), SyntaxErrors.FUNCTION_NEXT_STATESET_OF_FUNCTION_INVALID, function,
            stateClass.getName(), parent.getDottedPath().getAbsoluteName(), stateCandidateClass.getName()));
      }
    }
    if (0 < failureSet.size()) {
      throw new VerificationException(failureSet);
    }
  }

  private EventMetadata findEvent(StateMachineMetadata stateMachine, Class<?> eventKey) {
    EventMetadata declaredEvent = stateMachine.getDeclaredEvent(eventKey);
    if (null == declaredEvent) {
      if (stateMachine.isComposite()) {
        declaredEvent = stateMachine.getOwningStateMachine().getDeclaredEvent(eventKey);
        if (null == declaredEvent) {
          if (stateMachine.hasSuper()) {
            declaredEvent = findEvent(stateMachine.getSuper(), eventKey);
          } else if (stateMachine.getOwningStateMachine().hasSuper()) {
            declaredEvent = findEvent(stateMachine.getOwningStateMachine().getSuper(), eventKey);
          }
        }
      } else {
        if (stateMachine.hasSuper()) {
          declaredEvent = findEvent(stateMachine.getSuper(), eventKey);
        }
      }
    }
    return declaredEvent;
  }

  private StateMetadata findStateMetadata(final Class<?> stateCandidateClass, StateMachineMetadata stateMachine) {
    StateMetadata stateMetadata = null;
    for (StateMachineMetadata sm = stateMachine; sm != null && null == stateMetadata; sm = sm.getSuper()) {
      stateMetadata = sm.getDeclaredState(stateCandidateClass);
      if (null == stateMetadata) {
        for (StateMachineMetadata compositeStateMachine : stateMachine.getCompositeStateMachines()) {
          stateMetadata = findStateMetadata(stateCandidateClass, compositeStateMachine);
          if (null != stateMetadata) {
            return stateMetadata;
          } else {
            continue;
          }
        }
      } else {
        return stateMetadata;
      }
    }
    return stateMetadata;
  }

  private boolean isFinalState(Class<?> stateClass) {
    return null != stateClass.getAnnotation(Final.class);
  }

  @Override
  public void configureRelationConstrants(Class<?> clazz) throws VerificationException {
    final VerificationFailureSet failureSet = new VerificationFailureSet();
    for (InboundWhile inboundWhile : findInboundWhiles(clazz)) {
      verifyInboundWhile(inboundWhile, clazz, failureSet);
    }
    for (ValidWhile validWhile : findDeclaredValidWhiles(clazz)) {
      verifyValidWhile(validWhile, clazz, failureSet);
    }
    if (0 < failureSet.size()) {
      throw new VerificationException(failureSet);
    }
    for (InboundWhile inboundWhile : findInboundWhiles(clazz)) {
      RelationConstraintMetadata relationMetadata = configureRelationConstraint(findRelatedStateMachine(inboundWhile.relation()), "InboundWhiles."
              + inboundWhile.relation().getSimpleName(), inboundWhile.relation(),
          getOnStates(findRelatedStateMachine(inboundWhile.relation()), inboundWhile.on()),
          configureErrorMessageObjects(inboundWhile.otherwise(), inboundWhile.relation()), inboundWhile.nullable());
      this.inboundWhileRelations.add(relationMetadata);
    }
    for (ValidWhile validWhile : findDeclaredValidWhiles(clazz)) {
      RelationConstraintMetadata relationMetadata = configureRelationConstraint(findRelatedStateMachine(validWhile.relation()), "ValidWhiles."
              + validWhile.relation().getSimpleName(), validWhile.relation(),
          getOnStates(findRelatedStateMachine(validWhile.relation()), validWhile.on()),
          configureErrorMessageObjects(validWhile.otherwise(), validWhile.relation()), validWhile.nullable());
      this.validWhileRelations.add(relationMetadata);
    }
  }

  private LinkedList<StateMetadata> getOnStates(StateMachineMetadata stateMachineMetadata, Class<?>[] on) {
    final LinkedList<StateMetadata> onStates = new LinkedList<StateMetadata>();
    for (Class<?> clz : on) {
      onStates.add(stateMachineMetadata.getState(clz.getSimpleName()));
    }
    return onStates;
  }

  private RelationConstraintMetadata configureRelationConstraint(StateMachineMetadata relatedStateMachine, String name, Class<?> relationClass,
      LinkedList<StateMetadata> onStates, LinkedList<ErrorMessageObject> errorObjects, boolean nullable) throws VerificationException {
    return new RelationConstraintBuilderImpl(this, name, onStates, errorObjects, relatedStateMachine, nullable).build(relationClass, this);
  }

  private LinkedList<ErrorMessageObject> configureErrorMessageObjects(ErrorMessage[] otherwise, Class<?> clz) {
    LinkedList<ErrorMessageObject> errorObjects = new LinkedList<ErrorMessageObject>();
    for (ErrorMessage item : otherwise) {
      LinkedList<StateMetadata> errorStates = new LinkedList<StateMetadata>();
      Class<?>[] states = item.states();
      for (Class<?> stateClz : states) {
        errorStates.add(this.getParent().getState(stateClz));
      }
      errorObjects.add(new ErrorMessageObject(item.bundle(), clz, item.code(), errorStates.toArray(new StateMetadata[0])));
    }
    return errorObjects;
  }

  private void verifyValidWhile(ValidWhile validWhile, Class<?> clazz, VerificationFailureSet failureSet) {
    final Class<?>[] relatedStateClasses = validWhile.on();
    final Class<?> relationClass = validWhile.relation();
    final ErrorMessage[] errorMessages = validWhile.otherwise();
    verifyRelation(validWhile, relatedStateClasses, relationClass, errorMessages, clazz, failureSet);
  }

  private void verifyInboundWhile(InboundWhile inboundWhile, Class<?> clazz, VerificationFailureSet failureSet) {
    final Class<?>[] relatedStateClasses = inboundWhile.on();
    final Class<?> relationClass = inboundWhile.relation();
    final ErrorMessage[] errorMessages = inboundWhile.otherwise();
    verifyRelation(inboundWhile, relatedStateClasses, relationClass, errorMessages, clazz, failureSet);
  }

  private void verifyRelation(Annotation a, final Class<?>[] relatedStateClasses, final Class<?> relationClass, final ErrorMessage[] errorMessages,
      Class<?> stateClass, VerificationFailureSet failureSet) {
    if (!hasRelation(relationClass)) {
      final String errorCode;
      if (a instanceof InboundWhile) {
        errorCode = SyntaxErrors.RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET;
      } else {
        errorCode = SyntaxErrors.RELATION_VALIDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET;
      }
      failureSet.add(newVerificationFailure(getDottedPath(), errorCode, relationClass, stateClass, parent.getDottedPath()));
      return;
    }
    final StateMachineMetadata relatedStateMachine = findRelatedStateMachine(relationClass);
    verifyOnRelatedStates(a, relatedStateClasses, stateClass, failureSet, relatedStateMachine);
    verifyErrorMessages(a, errorMessages, stateClass, failureSet, relatedStateMachine);
  }

  private void verifyOnRelatedStates(Annotation a, final Class<?>[] relatedStateClasses, Class<?> stateClass, VerificationFailureSet failureSet,
      final StateMachineMetadata relatedStateMachine) {
    for (final Class<?> relateStateClass : relatedStateClasses) {
      if (null == findStateMetadata(relateStateClass, relatedStateMachine)) {
        if (a instanceof InboundWhile) {
          failureSet.add(newVerificationFailure(getInboundWhilePath(relateStateClass),
              SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION, a, stateClass, relatedStateMachine.getDottedPath()));
        } else {
          failureSet.add(newVerificationFailure(getValidWhilePath(relateStateClass),
              SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_VALIDWHILE_NOT_MACHING_RELATION, a, stateClass, relatedStateMachine.getDottedPath()));
        }
      }
    }
  }

  private DottedPath getValidWhilePath(final Class<?> relateStateClass) {
    return getDottedPath().append(ValidWhile.class.getSimpleName()).append(relateStateClass.getSimpleName());
  }

  private DottedPath getInboundWhilePath(final Class<?> relateStateClass) {
    return getDottedPath().append(InboundWhile.class.getSimpleName()).append(relateStateClass.getSimpleName());
  }

  private void verifyErrorMessages(Annotation a, final ErrorMessage[] errorMessages, Class<?> stateClass, VerificationFailureSet failureSet,
      final StateMachineMetadata relatedStateMachine) {
    for (ErrorMessage error : errorMessages) {
      for (final Class<?> relateStateClass : error.states()) {
        if (null == findStateMetadata(relateStateClass, relatedStateMachine)) {
          if (a instanceof InboundWhile) {
            failureSet.add(newVerificationFailure(getInboundWhilePath(relateStateClass),
                SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID, a, stateClass, relatedStateMachine.getDottedPath()));
          } else {
            failureSet.add(newVerificationFailure(getValidWhilePath(relateStateClass),
                SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_VALIDWHILE_INVALID, a, stateClass, relatedStateMachine.getDottedPath()));
          }
        }
      }
    }
  }

  private boolean hasRelation(final Class<?> relationClass) {
    boolean result = false;
    for (StateMachineMetadata smd = parent; !result && smd != null; smd = smd.getSuper()) {
      result = smd.hasRelation(relationClass);
      if (!result && smd.isComposite()) {
        result = smd.getOwningStateMachine().hasRelation(relationClass);
      }
      if (result) {
        return result;
      }
    }
    return result;
  }

  private StateMachineMetadata findRelatedStateMachine(Class<?> relationClass) {
    RelationMetadata relationMetadata = null;
    for (StateMachineMetadata stateMachineMetadata = parent; relationMetadata == null && stateMachineMetadata != null; stateMachineMetadata =
        stateMachineMetadata
        .getSuper()) {
      relationMetadata = stateMachineMetadata.getDeclaredRelationMetadata(relationClass);
      if (null != relationMetadata) {
        return relationMetadata.getRelateToStateMachine();
      }
      if (null == relationMetadata && stateMachineMetadata.isComposite()) {
        final RelationMetadata owingRelationMetadata = stateMachineMetadata.getOwningStateMachine().getDeclaredRelationMetadata(relationClass);
        if (null != owingRelationMetadata) {
          return owingRelationMetadata.getRelateToStateMachine();
        }
      }
    }
    return null;
  }

  private ArrayList<ValidWhile> findDeclaredValidWhiles(Class<?> clazz) {
    final ArrayList<ValidWhile> validWhileList = new ArrayList<ValidWhile>();
    Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
    ValidWhiles validWhiles = null;
    ValidWhile validWhile = null;
    for (Annotation annotation : declaredAnnotations) {
      if (ValidWhiles.class == annotation.annotationType()) {
        validWhiles = (ValidWhiles) annotation;
      }
      if (ValidWhile.class == annotation.annotationType()) {
        validWhile = (ValidWhile) annotation;
      }
    }
    if (null != validWhiles) {
      for (ValidWhile valid : validWhiles.value()) {
        validWhileList.add(valid);
      }
    } else if (null != validWhile) {
      validWhileList.add(validWhile);
    }
    return validWhileList;
  }

  private ArrayList<InboundWhile> findInboundWhiles(Class<?> clazz) {
    final ArrayList<InboundWhile> inboundWhileList = new ArrayList<InboundWhile>();
    final InboundWhiles inboundWhiles = clazz.getAnnotation(InboundWhiles.class);
    final InboundWhile inboundWhile = clazz.getAnnotation(InboundWhile.class);
    if (null != inboundWhiles) {
      for (InboundWhile inbound : inboundWhiles.value()) {
        inboundWhileList.add(inbound);
      }
    } else if (null != inboundWhile) {
      inboundWhileList.add(inboundWhile);
    }
    return inboundWhileList;
  }

  @Override
  public FunctionMetadata getDeclaredFunctionMetadata(Object functionKey) {
    return this.functionMetadataMap.get(functionKey);
  }

  @Override
  public boolean hasMultipleStateCandidatesOn(Object eventKey) {
    FunctionMetadata functionMetadata = null;
    if (parent.isComposite()) {
      functionMetadata = parent.getOwningState().getDeclaredFunctionMetadata(eventKey);
    }
    if (isOverriding() || !hasSuper()) {
      if (null == functionMetadata) {
        functionMetadata = getDeclaredFunctionMetadata(eventKey);
      }
      if (null == functionMetadata) {
        throw new IllegalArgumentException("Invalid Key or Key not registered: " + eventKey);
      }
      if (1 < functionMetadata.getNextStates().size()) {
        return true;
      } else {
        return false;
      }
    } else {// if ( hasSuper() && !isOverriding() ) {
      if (null == functionMetadata) {
        functionMetadata = this.getDeclaredFunctionMetadata(eventKey);
      }
      if (null != functionMetadata) {
        if (functionMetadata.getNextStates().size() > 1) {
          return true;
        } else {
          return false;
        }
      } else {
        return this.getSuper().hasMultipleStateCandidatesOn(eventKey);
      }
    }
  }

  public FunctionMetadata getFunctionMetadata(Object functionKey) {
    FunctionMetadata functionMetadata = null;
    if (parent.isComposite()) {
      functionMetadata = parent.getOwningState().getDeclaredFunctionMetadata(functionKey);
      if (null != functionMetadata) {
        return functionMetadata;
      }
    }
    if (isOverriding() || !hasSuper()) {
      return getDeclaredFunctionMetadata(functionKey);
    } else {// if ( hasSuper() && !isOverriding() ) {
      functionMetadata = this.getDeclaredFunctionMetadata(functionKey);
      if (null != functionMetadata) {
        return functionMetadata;
      } else {
        return this.getSuper().getFunctionMetadata(functionKey);
      }
    }
  }

  @Override
  protected void verifySuper(Class<?> clazz) throws VerificationException {
    if (null == findSuper(getSuperMetaClass(clazz))) {
      throw newVerificationException(getDottedPath(), SyntaxErrors.STATE_SUPER_CLASS_IS_NOT_STATE_META_CLASS, clazz, getSuperMetaClass(clazz));
    }
  }

  @Override
  protected StateMetadata findSuper(Class<?> metaClass) throws VerificationException {
    return findStateMetadata(metaClass, this.parent);
  }

  private void setCompositeState(boolean compositeState) {
    this.compositeState = compositeState;
  }

  @Override
  protected boolean extendsSuperKeySet() {
    return true;
  }

  @Override
  public void addPossibleReachingEvent(EventMetadata event) {
    this.possibleReachingEventList.add(event);
  }

  @Override
  public EventMetadata[] getPossibleReachingEvents() {
    return this.possibleReachingEventList.toArray(new EventMetadata[0]);
  }

  @Override
  public StateTypeEnum getType() {
    return type;
  }

  @Override
  public void setType(StateTypeEnum type) {
    this.type = type;
  }
}
