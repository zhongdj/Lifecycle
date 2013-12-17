package net.madz.lifecycle;

public interface SyntaxErrors {

    public static final String SYNTAX_ERROR_BUNDLE = "syntax_error";
    public static final String REGISTERED_META_ERROR = "002-1000";
    // StateMachine
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_SUPER_MUST_BE_STATEMACHINE = "002-2100";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_HAS_ONLY_ONE_SUPER_INTERFACE = "002-2101";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_CLASS_WITHOUT_ANNOTATION = "002-2102";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_WITHOUT_STATESET = "002-2103";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_MULTIPLE_STATESET = "002-2104";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_WITHOUT_TRANSITIONSET = "002-2105";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_MULTIPLE_TRANSITIONSET = "002-2106";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES = "002-2107";
    /**
     * @param {0} state machine class
     */
    public static final String STATEMACHINE_MULTIPLE_CONDITIONSET = "002-2108";
    // StateSet
    /**
     * @param {0} state set class
     */
    public static final String STATESET_WITHOUT_INITIAL_STATE = "002-2400";
    /**
     * @param {0} state set class
     */
    public static final String STATESET_MULTIPLE_INITAL_STATES = "002-2401";
    /**
     * @param {0} state set class
     */
    public static final String STATESET_WITHOUT_FINAL_STATE = "002-2402";
    /**
     * @param {0} state set class
     */
    public static final String STATESET_WITHOUT_STATE = "002-2403";
    // TransitionSet
    /**
     * @param {0} transition set class
     */
    public static final String TRANSITIONSET_WITHOUT_TRANSITION = "002-2501";
    /**
     * @param {0} method object
     * @param {1} transitionKey, will be transitionKey class when it's
     *        available, otherwise it will be transition method name with first
     *        char upper case.
     * @param {2} transitionType
     */
    public static final String TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER = "002-2502";
    /**
     * @param {0} Transition class
     * @param {1} condition class
     * @param {2} juder class
     */
    public static final String TRANSITION_CONDITIONAL_CONDITION_NOT_MATCH_JUDGER = "002-2503";
    
    /**
     * @param {0} Transition class
     * @param {1} Transition class' super class or interface
     */
    public static final String TRANSITION_ILLEGAL_EXTENTION = "002-2504";
    /**
     * @param {0} Transition class
     * @param {1} Transition class' super class or interface
     * @param {2} Super state machine class
     */
    public static final String TRANSITION_EXTENED_TRANSITION_CAN_NOT_FOUND_IN_SUPER_STATEMACHINE = "002-2505";
    // State's Function
    /**
     * @param {0} @Function definition
     * @param {1} State class
     * @param {2} Transition class
     */
    public static final String FUNCTION_INVALID_TRANSITION_REFERENCE = "002-2610";
    public static final String FUNCTION_CONDITIONAL_TRANSITION_WITHOUT_CONDITION = "002-2611";
    public static final String FUNCTION_TRANSITION_MUST_BE_NOT_ON_END_STATE = "002-2613";
    public static final String FUNCTION_WITH_EMPTY_STATE_CANDIDATES = "002-2614";
    public static final String STATE_NON_FINAL_WITHOUT_FUNCTIONS = "002-2615";
    /**
     * @param {0} stateClass
     * @param {1} transtionClass
     */
    public static final String STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_TRANSITION = "002-2616";
    /**
     * @param {0} stateClass
     */
    public static final String STATE_OVERRIDES_WITHOUT_SUPER_CLASS = "002-2617";
    /**
     * @param {0} stateClass
     * @param {1} superStateClass
     */
    public static final String STATESET_WITHOUT_INITAL_STATE_AFTER_OVERRIDING_SUPER_INITIAL_STATE = "002-2618";
    /**
     * @param {0} stateClass
     * @param {1} superClass
     */
    public static final String STATE_SUPER_CLASS_IS_NOT_STATE_META_CLASS = "002-2619";
    public static final String FUNCTION_NEXT_STATESET_OF_FUNCTION_INVALID = "002-2700";
    // State's Shortcut
    public static final String SHORT_CUT_INVALID = "002-2800";
    public static final String COMPOSITE_STATEMACHINE_SHORTCUT_WITHOUT_END = "002-2801";
    public static final String COMPOSITE_STATEMACHINE_FINAL_STATE_WITHOUT_SHORTCUT = "002-2802";
    public static final String COMPOSITE_STATEMACHINE_SHORTCUT_STATE_INVALID = "002-2803";
    /**
     * @param {0} composite state machine class
     */
    public static final String COMPOSITE_STATEMACHINE_CANNOT_EXTENDS_OWNING_STATEMACHINE = "002-2804";
    // State's Relation
    public static final String RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET = "002-2911";
    public static final String RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION = "002-2912";
    public static final String RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID = "002-2913";
    public static final String RELATION_OTHERWISE_ATTRIBUTE_OF_VALIDWHILE_INVALID = "002-2914";
    public static final String RELATION_VALIDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET = "002-2921";
    public static final String RELATION_ON_ATTRIBUTE_OF_VALIDWHILE_NOT_MACHING_RELATION = "002-2922";
    public static final String RELATION_RELATED_TO_REFER_TO_NON_STATEMACHINE = "002-2931";
    // LifecycleMeta
    public static final String LM_TRANSITION_METHOD_WITH_INVALID_TRANSITION_REFERENCE = "002-3211";
    public static final String LM_TRANSITION_NOT_CONCRETED_IN_LM = "002-3212";
    public static final String LM_METHOD_NAME_INVALID = "002-3213";
    public static final String LM_REDO_CORRUPT_RECOVER_TRANSITION_HAS_ONLY_ONE_METHOD = "002-3214";
    public static final String LM_MUST_CONCRETE_ALL_RELATIONS = "002-3220";
    /**
     * @param {0} @LifecycleMeta Class
     * @param {1} invalid relation instance
     * @param {2} StateMachine
     */
    public static final String LM_REFERENCE_INVALID_RELATION_INSTANCE = "002-3221";
    /**
     * @param {0} @LifecycleMeta Class
     * @param {1} relation instance
     */
    public static final String LM_RELATION_INSTANCE_MUST_BE_UNIQUE = "002-3222";
    /**
     * @param {0} method
     * @param {1} @LifecycleMeta Class
     * @param {2} relation
     * @param {3} state
     */
    public static final String LM_RELATION_NOT_BE_CONCRETED = "002-3223";
    /**
     * @param {0} method
     * 
     */
    public static final String LM_RELATION_ON_METHOD_PARAMETER_MUST_SPECIFY_VALUE = "002-3224";
    /**
     * @param {0} @LifecycleMeta class
     * @param {1} @StateMachine dottedPath
     * @param {2} Condition Class
     */
    public static final String LM_CONDITION_NOT_COVERED = "002-3230";
    /**
     * @param {0} Method full qualified name
     * @param {1} Invalid condition class
     */
    public static final String LM_CONDITION_REFERENCE_INVALID = "002-3231";
    /**
     * @param {0} @LifecycleMeta class
     * @param {1} Condition class
     */
    public static final String LM_CONDITION_MULTIPLE_METHODS_REFERENCE_SAME_CONDITION = "002-3232";
    /**
     * @param {0} Method class
     * @param {1} Condition class
     */
    public static final String LM_CONDITION_OBJECT_DOES_NOT_IMPLEMENT_CONDITION_INTERFACE = "002-3233";
    /**
     * @param {0} class annotated with @LifecycleMeta
     */
    public static final String STATE_INDICATOR_CANNOT_FIND_DEFAULT_AND_SPECIFIED_STATE_INDICATOR = "002-3300";
    /**
     * @param {0} setter method object
     */
    public static final String STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_SETTER = "002-3301";
    /**
     * @param {0} setter field object
     */
    public static final String STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_FIELD = "002-3302";
    /**
     * @param {0} class annotated with @LifecycleMeta
     */
    public static final String STATE_INDICATOR_SETTER_NOT_FOUND = "002-3303";
    /**
     * @param {0} class annotated with @LifecycleMeta
     * @param {1} state type
     */
    public static final String STATE_INDICATOR_CONVERTER_NOT_FOUND = "002-3304";
    /**
     * @param {0} class annotated with @LifecycleMeta
     * @param {1} actual state type
     * @param {2} invalid converter
     * @param {3} raw type that converter converts
     */
    public static final String STATE_INDICATOR_CONVERTER_INVALID = "002-3305";
    /**
     * @param {0} class annotated with @LifecycleMeta
     */
    public static final String STATE_INDICATOR_MULTIPLE_STATE_INDICATOR_ERROR = "002-3306";
    // @ConditionSet
    /**
     * @param {0} stateMachine class
     */
    public static final String CONDITIONSET_MULTIPLE = "002-3400";
    // @RelationSet
    public static final String RELATIONSET_MULTIPLE = "002-3500";
    /**
     * @param {0} stateMachine class
     */
    public static final String RELATION_MULTIPLE_PARENT_RELATION = "002-3501";
    /**
     * @param {0} child stateMachine class
     * @param {1} super stateMachine class
     */
    public static final String RELATION_NEED_OVERRIDES_TO_OVERRIDE_SUPER_STATEMACHINE_PARENT_RELATION = "002-3502";
    /**
     * @param {0} composite stateMachine dottedPath
     * @param {1} parent relation class of composite stateMachine class
     * @param {2} owning stateMachine class
     * @param {3} parent relation class of owning stateMachine class
     */
    public static final String RELATION_COMPOSITE_STATE_MACHINE_CANNOT_OVERRIDE_OWNING_PARENT_RELATION = "002-3503";
    /**
     * @param {0} Relation Class
     */
    public static final String RELATION_NO_RELATED_TO_DEFINED = "002-3504";
    /**
     * @param {0} Lifecycle Lock class
     */
    public static final String LIFECYCLE_LOCK_SHOULD_HAVE_NO_ARGS_CONSTRUCTOR = "002-3600";
    /**
     * @param {0} Lifecycle Event Handler Class
     */
    public static final String LIFECYCLE_EVENT_HANDLER_MUST_HAVE_NO_ARG_CONSTRUCTOR = "002-3601";
    /**
     * @param {0} To state class
     * @param {1} Call back Method
     * @param {2} Transition Dotted Path
     */
    public static final String PRE_STATE_CHANGE_TO_POST_EVALUATE_STATE_IS_INVALID = "002-3700";
    /**
     * @param {0} From state class
     * @param {1} Method class
     * @param {2} State Machine class
     */
    public static final String PRE_STATE_CHANGE_FROM_STATE_IS_INVALID = "002-3701";
    /**
     * @param {0} To state class
     * @param {1} Method class
     * @param {2} State Machine class
     */
    public static final String PRE_STATE_CHANGE_TO_STATE_IS_INVALID = "002-3702";
    /**
     * @param {0} From state class
     * @param {1} Method class
     * @param {2} State Machine class
     */
    public static final String POST_STATE_CHANGE_FROM_STATE_IS_INVALID = "002-3703";
    /**
     * @param {0} To state class
     * @param {1} Method class
     * @param {2} State Machine class
     */
    public static final String POST_STATE_CHANGE_TO_STATE_IS_INVALID = "002-3704";
    /**
     * @param {0} relation
     * @param {1} Method class
     * @param {2} State Machine object class
     */
    public static final String PRE_STATE_CHANGE_RELATION_INVALID = "002-3705";
    /**
     * @param {0} mappedby
     * @param {1} Method class
     * @param {2} Observerable Class
     */
    public static final String PRE_STATE_CHANGE_MAPPEDBY_INVALID = "002-3706";
    /**
     * @param {0} relation
     * @param {1} Method class
     * @param {2} State Machine object class
     */
    public static final String POST_STATE_CHANGE_RELATION_INVALID = "002-3707";
    /**
     * @param {0} mappedby
     * @param {1} Method class
     * @param {2} Observerable Class
     */
    public static final String POST_STATE_CHANGE_MAPPEDBY_INVALID = "002-3708";
    /**
     * @param {0} observable name
     * @param {1} observable class
     * @param {2} Method class
     */
    public static final String POST_STATE_CHANGE_OBSERVABLE_NAME_MISMATCH_OBSERVABLE_CLASS = "002-3709";
    /**
     * @param {0} observable class
     * @param {1} Method class
     */
    public static final String POST_STATE_CHANGE_OBSERVABLE_CLASS_INVALID = "002-3710";
    /**
     * @param {0} observable class
     * @param {1} Method class
     */
    public static final String PRE_STATE_CHANGE_OBSERVABLE_CLASS_INVALID = "002-3711";
    /**
     * @param {0} observable name
     * @param {1} observable class
     * @param {2} Method class
     */
    public static final String PRE_STATE_CHANGE_OBSERVABLE_NAME_MISMATCH_OBSERVABLE_CLASS = "002-3712";
    
}
