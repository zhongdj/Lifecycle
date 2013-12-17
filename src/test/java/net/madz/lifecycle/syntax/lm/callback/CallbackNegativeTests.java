package net.madz.lifecycle.syntax.lm.callback;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.LifecycleContext;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.syntax.lm.callback.CallbackTestBase.S1.States.S1_State_C;
import net.madz.lifecycle.syntax.lm.callback.CallbackTestBase.S1.Transitions.S1_Transition_X;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;

import org.junit.Test;

public class CallbackNegativeTests extends CallbackTestBase {

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3700_if_prestatechangeToState_with_post_evaluate_non_relational() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_To_State_With_Post_Evaluate_Non_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_TO_POST_EVALUATE_STATE_IS_INVALID, S1_State_C.class,
                    NLM_With_PreStateChange_To_State_With_Post_Evaluate_Non_Relational.class.getMethod("interceptStateChange", LifecycleContext.class),
                    S1.class.getName() + ".TransitionSet." + S1_Transition_X.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3700_if_prestatechangeToState_with_post_evaluate_relational() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_To_State_With_Post_Evaluate_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_TO_POST_EVALUATE_STATE_IS_INVALID,
                    S3.States.S3_State_C.class,
                    NLM_With_PreStateChange_To_State_With_Post_Evaluate_Relational.class.getMethod("interceptStateChange", LifecycleContext.class),
                    S3.class.getName() + ".TransitionSet." + S3.Transitions.Move.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3701_if_prestatechange_from_state_not_found_in_stateMachine_non_relational() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_prestatechange_from_state_not_found_in_stateMachine_non_relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_FROM_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_prestatechange_from_state_not_found_in_stateMachine_non_relational.class.getMethod("interceptStateChange", LifecycleContext.class), S1.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3701_if_prestatechange_from_state_not_found_in_observable_stateMachine_relational() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_With_PreStateChange_From_State_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_FROM_STATE_IS_INVALID, S1.States.S1_State_A.class,
                    NLM_With_PreStateChange_From_State_Invalid_Relational.class.getMethod("interceptStateChange", LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3702_if_prestatechange_to_state_not_found_in_stateMachine_non_relational() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_prestatechange_to_state_invalid_non_relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_TO_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_prestatechange_to_state_invalid_non_relational.class.getMethod("interceptStateChange", LifecycleContext.class), S1.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3702_if_prestatechange_to_state_not_found_in_observable_stateMachine_relational() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_With_PreStateChange_To_State_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_TO_STATE_IS_INVALID, S1.States.S1_State_D.class,
                    NLM_With_PreStateChange_To_State_Invalid_Relational.class.getMethod("interceptStateChange", LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3703_if_poststatechange_from_state_not_found_in_stateMachine_non_relational() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_poststatechange_from_state_invalid_non_relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_FROM_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_poststatechange_from_state_invalid_non_relational.class.getMethod("interceptStateChange", LifecycleContext.class), S1.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3703_if_poststatechange_from_state_not_found_in_observable_stateMachine_relational() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_With_PostStateChange_From_State_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_FROM_STATE_IS_INVALID, S1.States.S1_State_A.class,
                    NLM_With_PostStateChange_From_State_Invalid_Relational.class.getMethod("interceptStateChange", LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3704_if_poststatechange_to_state_not_found_in_stateMachine_non_relational() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_poststatechange_to_state_invalid_non_relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_TO_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_poststatechange_to_state_invalid_non_relational.class.getMethod("interceptStateChange", LifecycleContext.class), S1.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3704_if_poststatechange_to_state_not_found_in_observable_stateMachine_relational() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_With_PostStateChange_To_State_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_TO_STATE_IS_INVALID, S1.States.S1_State_D.class,
                    NLM_With_PostStateChange_To_State_Invalid_Relational.class.getMethod("interceptStateChange", LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3705_if_prestatechange_observable_name_invalid() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_prestatechange_observable_object_not_found.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_RELATION_INVALID, "s1",
                    NLM_prestatechange_observable_object_not_found.class.getMethod("interceptStateChange", LifecycleContext.class),
                    NLM_prestatechange_observable_object_not_found.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3706_if_prestatechange_mappedby_cannot_be_found_in_observable_object() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_prestatechange_mappedby_invalid.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_MAPPEDBY_INVALID, "s1",
                    NLM_prestatechange_mappedby_invalid.class.getMethod("interceptStateChange", LifecycleContext.class), S1BaseLM.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3706_if_prestatechange_mappedby_is_null() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_prestatechange_mappedby_is_null.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_MAPPEDBY_INVALID, "",
                    NLM_prestatechange_mappedby_is_null.class.getMethod("interceptStateChange", LifecycleContext.class), S1BaseLM.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3707_if_poststatechange_observable_name_invalid() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_poststatechange_observable_name_invalid.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_RELATION_INVALID, "s1",
                    NLM_poststatechange_observable_name_invalid.class.getMethod("interceptStateChange", LifecycleContext.class),
                    NLM_poststatechange_observable_name_invalid.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3708_if_poststatechange_mappedby_cannot_found_in_observable_object() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_poststatechange_mappedby_invalid.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_MAPPEDBY_INVALID, "s1",
                    NLM_poststatechange_mappedby_invalid.class.getMethod("interceptStateChange", LifecycleContext.class), S1BaseLM.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3708_if_poststatechange_mappedby_is_null() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_poststatechange_mappedby_is_null.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_MAPPEDBY_INVALID, "",
                    NLM_poststatechange_mappedby_is_null.class.getMethod("interceptStateChange", LifecycleContext.class), S1BaseLM.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3701_and_002_3704_if_callbacks_defines_invalid_preStateChangeFromState_and_postStateChangeToState()
            throws NoSuchMethodException, SecurityException, VerificationException {
        @LifecycleRegistry(NLM_With_CallBacksWithInvalidStates.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertEquals(2, e.getVerificationFailureSet().size());
            final Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
            assertFailure(iterator.next(), SyntaxErrors.PRE_STATE_CHANGE_FROM_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_With_CallBacksWithInvalidStates.class.getMethod("interceptStates", LifecycleContext.class), S1.class);
            assertFailure(iterator.next(), SyntaxErrors.POST_STATE_CHANGE_TO_STATE_IS_INVALID, S2.States.S2_State_A.class,
                    NLM_With_CallBacksWithInvalidStates.class.getMethod("interceptStates", LifecycleContext.class), S1.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3709_if_postStateChange_observableName_mistmatch_observableClass_Relational() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PostStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_OBSERVABLE_NAME_MISMATCH_OBSERVABLE_CLASS, "s3",
                    NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                    NLM_With_PostStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), NLM_With_PostStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3710_if_postStateChange_observableClass_is_non_lifecycle_class() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PostStateChange_ObservableClass_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_OBSERVABLE_CLASS_INVALID, NonLifecycleClass.class,
                    NLM_With_PostStateChange_ObservableClass_Invalid_Relational.class.getDeclaredMethod("interceptStateChange", LifecycleContext.class),
                    NLM_With_PostStateChange_ObservableClass_Invalid_Relational.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3703_if_postStateChange_from_State_invalid_when_specified_observableClass() throws NoSuchMethodException,
            SecurityException, VerificationException {
        @LifecycleRegistry(NLM_With_PostStateChange_From_State_Invalid_Specified_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_FROM_STATE_IS_INVALID, S1.States.S1_State_A.class,
                    NLM_With_PostStateChange_From_State_Invalid_Specified_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3704_if_postStateChange_to_state_invalid_when_specified_observableClass_Relational() throws NoSuchMethodException,
            SecurityException, VerificationException {
        @LifecycleRegistry(NLM_With_PostStateChange_To_State_Invalid_Specified_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.POST_STATE_CHANGE_TO_STATE_IS_INVALID, S1.States.S1_State_D.class,
                    NLM_With_PostStateChange_To_State_Invalid_Specified_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), S3.class);
            throw e;
        }
    }

    // ///////////////////////////////
    @Test(expected = VerificationException.class)
    public void should_throw_002_3712_if_preStateChange_observableName_mistmatch_observableClass() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_OBSERVABLE_NAME_MISMATCH_OBSERVABLE_CLASS, "s3",
                    NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                    NLM_With_PreStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), NLM_With_PreStateChange_ObservableName_Mistmatch_ObservableClass_Relational.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3711_if_preStateChange_observableClass_is_not_lifecycle_class() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_ObservableClass_Invalid_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_OBSERVABLE_CLASS_INVALID, NonLifecycleClass.class,
                    NLM_With_PreStateChange_ObservableClass_Invalid_Relational.class.getDeclaredMethod("interceptStateChange", LifecycleContext.class),
                    NLM_With_PreStateChange_ObservableClass_Invalid_Relational.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3701_if_preStateChange_from_state_invalid_specified_observableClass() throws NoSuchMethodException,
            SecurityException, VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_From_State_Invalid_Specified_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_FROM_STATE_IS_INVALID, S1.States.S1_State_A.class,
                    NLM_With_PreStateChange_From_State_Invalid_Specified_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), S3.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3702_if_preStateChange_to_state_invalid_when_specified_observableClass() throws NoSuchMethodException,
            SecurityException, VerificationException {
        @LifecycleRegistry(NLM_With_PreStateChange_To_State_Invalid_Specified_ObservableClass_Relational.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.PRE_STATE_CHANGE_TO_STATE_IS_INVALID, S1.States.S1_State_D.class,
                    NLM_With_PreStateChange_To_State_Invalid_Specified_ObservableClass_Relational.class.getDeclaredMethod("interceptStateChange",
                            LifecycleContext.class), S3.class);
            throw e;
        }
    }
}
