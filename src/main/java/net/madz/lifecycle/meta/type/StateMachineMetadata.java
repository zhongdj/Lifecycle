package net.madz.lifecycle.meta.type;

import net.madz.lifecycle.meta.MetaType;
import net.madz.lifecycle.meta.Recoverable;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.verification.VerificationException;

public interface StateMachineMetadata extends Recoverable, MetaType<StateMachineMetadata> {

    /* ///////////////////////////////////////////////////////////// */
    /* // State Machine Relation with other State Machine Methods // */
    /* ///////////////////////////////////////////////////////////// */
    boolean hasParent();

    StateMachineMetadata getParent();

    boolean hasRelation(Object relationKey);

    /**
     * @param relationKey
     * @return RelationMetadata in current StateMachine
     */
    RelationMetadata getDeclaredRelationMetadata(Object relationKey);

    /**
     * @param relationKey
     * @return RelationMetadata in StateMachine hierarchy.
     */
    RelationMetadata getRelationMetadata(Object relationKey);

    /* //////////////////////////////////////////////////// */
    /* /////////////// State Related Methods ////////////// */
    /* //////////////////////////////////////////////////// */
    StateMetadata[] getDeclaredStateSet();

    StateMetadata getDeclaredState(Object stateKey);

    /**
     * @return all states in current StateMachine, current StateMachine's
     *         composite StateMachine, super StateMachine, super StateMachine's
     *         composite StateMachine.
     */
    StateMetadata[] getAllStates();

    /**
     * @param stateKey
     * @return state in allStates by specified stateKey.
     */
    StateMetadata getState(Object stateKey);

    StateMetadata getInitialState();

    StateMetadata[] getFinalStates();

    /* //////////////////////////////////////////////////// */
    /* ///////////// Transtion Related Methods //////////// */
    /* //////////////////////////////////////////////////// */
    TransitionMetadata[] getDeclaredTransitionSet();

    // TransitionMetadata[] getSuperTransitionSet();
    TransitionMetadata getDeclaredTransition(Object transitionKey);

    /**
     * @return transitions in current StateMachine, current StateMachine's
     *         CompositeStateMachine, super StateMachines, super
     *         StateMachines'composite
     *         StateMachines.
     */
    TransitionMetadata[] getAllTransitions();

    /**
     * @param transitionKey
     * @return transition in allTransitionSet by specified transitionKey
     */
    TransitionMetadata getTransition(Object transitionKey);

    TransitionMetadata getStateSynchronizationTransition();

    /**
     * @param clazz
     *            defined with @LifecycleMeta, and with @Transition
     *            , @StateIndicator, @Relation.
     * 
     * @return a concrete instance of StateMachineMetadata, whose abstract
     *         part is concreted by the clazz param.
     * @throws VerificationException
     */
    StateMachineObject<?> newInstance(Class<?> clazz) throws VerificationException;

    /* //////////////////////////////////////////////////// */
    /* //////// Methods For Composite State Machine /////// */
    /* //////////////////////////////////////////////////// */
    boolean isComposite();

    /**
     * @return a state machine template, in whose state defining this state
     *         machine
     */
    StateMachineMetadata getOwningStateMachine();

    StateMetadata getOwningState();

    // StateMetadata[] getShortcutStateSet();
    StateMachineMetadata[] getCompositeStateMachines();

    /* //////////////////////////////////////////////////// */
    /* //////// Methods For Conditions /////// */
    /* //////////////////////////////////////////////////// */
    /**
     * @return condition metadata defined in current state machine and current
     *         composite state machines.
     */
    ConditionMetadata[] getDeclaredConditions();

    /**
     * @return all condition metadata defined in current state machine, current
     *         composite state machines, super state machine, super state
     *         machine's composite state machines.
     */
    ConditionMetadata[] getAllCondtions();

    /**
     * @param conditionKey
     * @return condition metadata in the state machine's inheritance hierarchy,
     *         including composite state machines.
     */
    ConditionMetadata getCondtion(Object conditionKey);

    /**
     * @param conditionKey
     * @return true if the condition can by found in the state machine's
     *         inheritance hierarchy and composite state machines.
     */
    boolean hasCondition(Object conditionKey);

    LifecycleMetaRegistry getRegistry();

    boolean hasTransition(Object transitionKey);
}
