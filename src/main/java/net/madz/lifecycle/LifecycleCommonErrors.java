package net.madz.lifecycle;

public class LifecycleCommonErrors {

    /**
     * @param {0} transition simple name or transitionKey Class,
     *        When @Transition(value=T.class) then transitionKeyClass will be
     *        the value other than simple Name
     * @param {1} state name
     * @param {2} target object
     */
    public static final String ILLEGAL_TRANSITION_ON_STATE = "002-9000";
    /**
     * @param {0} target object
     * @param {1} target object's state
     * @param {2} relation object
     * @param {3} relation state
     * @param {4} relation definition
     */
    public static final String STATE_INVALID = "002-9001";
    /**
     * @param {0} transition
     * @param {1} target object next state
     * @param {2} target object
     * @param {3} relation object
     * @param {4} relation object's state
     * @param {5} inbound constraint definition
     */
    public static final String VIOLATE_INBOUND_WHILE_RELATION_CONSTRAINT = "002-9002";
    public static final String BUNDLE = "lifecycle_common";
    /**
     * @param {0} Method object
     */
    public static final String CALLBACK_EXCEPTION_OCCOURRED = "002-9003";
    /**
     * @param {0} Relation class
     * @param {1} @nullable value
     * @param {2} State class
     */
    public static final String VALID_WHILE_RELATION_TARGET_IS_NULL = "002-9004";
    /**
     * @param {0} Relation class
     * @param {1} @nullable value
     * @param {2} State class
     */
    public static final String INBOUND_WHILE_RELATION_TARGET_IS_NULL = "002-9005";

    private LifecycleCommonErrors() {}
}
