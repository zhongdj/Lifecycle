package net.madz.lifecycle.syntax.basic;

import java.util.Iterator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;
import net.madz.verification.VerificationFailureSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StateSetAndTransitionSetSyntaxNegativeTest extends StateSetSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2107_if_stateMachine_defines_no_innerClasses() throws VerificationException {
        @LifecycleRegistry(Negative_No_InnerClasses.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertEquals(1, e.getVerificationFailureSet().size());
            final VerificationFailure failure = e.getVerificationFailureSet().iterator().next();
            assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES, failure.getErrorCode());
            final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES,
                    new Object[] { Negative_No_InnerClasses.class.getName() });
            assertEquals(expectedMessage, failure.getErrorMessage(null));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2103_and_002_2105_if_stateMachine_defines_no_stateSet_and_transitionSet() throws VerificationException {
        @LifecycleRegistry(Negative_No_StateSet_and_TransitionSet.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertEquals(2, e.getVerificationFailureSet().size());
            Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
            final VerificationFailure failureOne = iterator.next();
            final VerificationFailure failureTwo = iterator.next();
            {
                assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_STATESET, failureOne.getErrorCode());
                assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_TRANSITIONSET, failureTwo.getErrorCode());
            }
            {
                final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_STATESET, Negative_No_StateSet_and_TransitionSet.class);
                assertEquals(expectedMessage, failureOne.getErrorMessage(null));
            }
            {
                final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_TRANSITIONSET, Negative_No_StateSet_and_TransitionSet.class);
                assertEquals(expectedMessage, failureTwo.getErrorMessage(null));
            }
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2104_and_002_2106_if_stateMachine_defines_multi_stateSet_and_multi_transitionSet() throws VerificationException {
        @LifecycleRegistry({ Negative_Multi_StateSet_Multi_TransitionSet.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException ex) {
            assertEquals(2, ex.getVerificationFailureSet().size());
            Iterator<VerificationFailure> iterator = ex.getVerificationFailureSet().iterator();
            final VerificationFailure failureOne = iterator.next();
            final VerificationFailure failureTwo = iterator.next();
            assertFailure(failureOne, SyntaxErrors.STATEMACHINE_MULTIPLE_STATESET, Negative_Multi_StateSet_Multi_TransitionSet.class);
            assertFailure(failureTwo, SyntaxErrors.STATEMACHINE_MULTIPLE_TRANSITIONSET, Negative_Multi_StateSet_Multi_TransitionSet.class);
            throw ex;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2403_and_002_2501_if_stateSet_defines_no_states_and_transitionSet_has_no_transitions() throws VerificationException {
        @LifecycleRegistry({ Negative_No_State_No_Transition.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
            assertFailure(iterator.next(), SyntaxErrors.STATESET_WITHOUT_STATE, Negative_No_State_No_Transition.States.class);
            assertFailure(iterator.next(), SyntaxErrors.TRANSITIONSET_WITHOUT_TRANSITION, Negative_No_State_No_Transition.Transitions.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2400_and_002_2402_if_stateSet_defines_no_initialState_and_endState() throws VerificationException {
        @LifecycleRegistry(Negative_StateSet_Without_InitalState_And_EndState.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            VerificationFailureSet failureSet = e.getVerificationFailureSet();
            assertEquals(2, failureSet.size());
            Iterator<VerificationFailure> iterator = failureSet.iterator();
            VerificationFailure failureOne = iterator.next();
            VerificationFailure failureTwo = iterator.next();
            assertFailure(failureOne, SyntaxErrors.STATESET_WITHOUT_INITIAL_STATE, Negative_StateSet_Without_InitalState_And_EndState.States.class);
            assertFailure(failureTwo, SyntaxErrors.STATESET_WITHOUT_FINAL_STATE, Negative_StateSet_Without_InitalState_And_EndState.States.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_exception_002_2401_if_stateSet_has_multi_initialState() throws VerificationException {
        @LifecycleRegistry(Negative_StateSet_With_Multi_InitalState.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATESET_MULTIPLE_INITAL_STATES,
                    Negative_StateSet_With_Multi_InitalState.States.class);
            throw e;
        }
    }
}
