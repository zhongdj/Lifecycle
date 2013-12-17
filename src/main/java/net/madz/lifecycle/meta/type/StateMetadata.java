package net.madz.lifecycle.meta.type;

import net.madz.lifecycle.meta.MetaType;
import net.madz.lifecycle.meta.Recoverable;
import net.madz.lifecycle.meta.object.FunctionMetadata;

public interface StateMetadata extends Recoverable, MetaType<StateMetadata> {

    public static enum StateTypeEnum {
        Running,
        Waiting,
        Stopped,
        Corrupted,
        Common
    }

    StateTypeEnum getType();

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Basic Properties /////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    StateMachineMetadata getStateMachine();

    String getSimpleName();

    boolean isInitial();

    boolean isFinal();

    /* ////////////////////////////////////////////////////////////////// */
    /* ////////////////////////Transition Related /////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    TransitionMetadata[] getPossibleLeavingTransitions();

    TransitionMetadata[] getPossibleReachingTransitions();

    TransitionMetadata getTransition(Object transitionKey);

    boolean isTransitionValid(Object transitionKey);

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Dependency Part //////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    boolean hasInboundWhiles();

    /**
     * @return related state dependencies, expected to be used post-state-change
     *         validation
     */
    RelationConstraintMetadata[] getDeclaredInboundWhiles();

    boolean hasValidWhiles();

    /**
     * @return related state dependencies, expected to be used pre-state-change
     *         validation, which will validate the validity of the state. Once
     *         the state is not valid, transitions will fail until the state
     *         has been fixed by synchronizationTransition.
     * 
     *         And if parent object life cycle exists, then this state should be
     *         valid ONLY in a subset of parent life cycle states, so does the
     *         parent object, the validation will go up along the parent's
     *         parent recursively.
     * 
     */
    RelationConstraintMetadata[] getValidWhiles();

    /* ////////////////////////////////////////////////////////////////// */
    /* //////////////////////////Composite State///////////////////////// */
    /* ////////////////////////////////////////////////////////////////// */
    boolean isCompositeState();

    StateMetadata getOwningState();

    StateMachineMetadata getCompositeStateMachine();

    /* For Shortcut State inside a composite state */
    StateMetadata getLinkTo();

    FunctionMetadata[] getDeclaredFunctionMetadata();

    FunctionMetadata getDeclaredFunctionMetadata(Object functionKey);

    boolean hasMultipleStateCandidatesOn(Object transtionKey);

    FunctionMetadata getFunctionMetadata(Object functionKey);

    RelationConstraintMetadata[] getDeclaredValidWhiles();

    RelationConstraintMetadata[] getInboundWhiles();

    void setType(StateTypeEnum type);
}
