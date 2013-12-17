package net.madz.lifecycle.syntax.state;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.States.NCS2_B.CStates.NCS2_CC;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.States.NCS3_B.CStates.NCS3_CC;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.States.NCS4_B.CStates.NCS4_CC;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S1.States.A;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.C;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.D;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S3.States.E;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S3.Transitions.Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S5.states.S5_A;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterTransition.States.SC1_C;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class StateSyntaxNegativeTest extends StateSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_002_2615_if_non_final_state_without_functions() throws VerificationException {
        @LifecycleRegistry(S1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_NON_FINAL_WITHOUT_FUNCTIONS, A.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_state_function__with_invalid_transition() throws VerificationException {
        @LifecycleRegistry(S2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    C.class.getAnnotation(Function.class), C.class, net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S1.Transitions.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2611_if_state_function_with_invalid_conditional_transition_without_conditional_annotation() throws VerificationException {
        @LifecycleRegistry(S3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_CONDITIONAL_TRANSITION_WITHOUT_CONDITION,
                    E.class.getAnnotation(Function.class), E.class.getName(), Y.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2700_if_state_function_with_invalid_next_state() throws VerificationException {
        @LifecycleRegistry(S5.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_NEXT_STATESET_OF_FUNCTION_INVALID,
                    S5_A.class.getAnnotation(Function.class), S5_A.class.getName(), S5.class.getName(), D.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2803_if_shortcut_referencing_state_beyond_scope() throws VerificationException {
        @LifecycleRegistry(NCS2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_STATE_INVALID,
                    NCS2_CC.class.getAnnotation(ShortCut.class), NCS2_CC.class, SC1_C.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2802_if_composite_final_without_shortcut() throws VerificationException {
        @LifecycleRegistry(NCS3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_FINAL_STATE_WITHOUT_SHORTCUT, NCS3_CC.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2801_if_shortcut_without_end_annotation() throws VerificationException {
        @LifecycleRegistry(NCS4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_WITHOUT_END, NCS4_CC.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2616_if_multiple_functions_referring_same_transition() throws VerificationException {
        @LifecycleRegistry(Multiple_Function_Referring_Same_Transition.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_TRANSITION,
                    Multiple_Function_Referring_Same_Transition.States.Created.class, Multiple_Function_Referring_Same_Transition.Transitions.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2616_if_inheritance_multiple_functions_referring_same_transition() throws VerificationException {
        @LifecycleRegistry(Multiple_Function_Referring_Same_Transition_Child.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_TRANSITION,
                    Multiple_Function_Referring_Same_Transition_Child.States.Created.class,
                    Multiple_Function_Referring_Same_Transition_Super.Transitions.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2617_if_state_overrides_without_super_class() throws VerificationException {
        @LifecycleRegistry(NegativeOverridesWithoutSuperClass.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_OVERRIDES_WITHOUT_SUPER_CLASS,
                    NegativeOverridesWithoutSuperClass.States.A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2618_if_missing_initial_state_when_intial_state_overrided() throws VerificationException {
        @LifecycleRegistry(NegativeOverridesMissingInitial.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATESET_WITHOUT_INITAL_STATE_AFTER_OVERRIDING_SUPER_INITIAL_STATE,
                    NegativeOverridesMissingInitial.States.A.class, CorrectBase.States.A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_composite_statemachine_transition_referenced_from_another_composite_state_machine() throws VerificationException {
        @LifecycleRegistry(CompositeStateMachineTransitionReferencedFromAnotherCompositeStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    CompositeStateMachineTransitionReferencedFromAnotherCompositeStateMachine.States.S2.S2_States.S2_A.class.getAnnotation(Function.class),
                    CompositeStateMachineTransitionReferencedFromAnotherCompositeStateMachine.States.S2.S2_States.S2_A.class,
                    CompositeStateMachineTransitionReferencedFromAnotherCompositeStateMachine.States.S3.S3_Transitions.S3_X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_composite_statemachine_transition_referenced_from_super_non_extended_composite_state_machine() throws VerificationException {
        @LifecycleRegistry(CompositeStateMachineTransitionReferenceFromSuperNonExtendedCompositeStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    CompositeStateMachineTransitionReferenceFromSuperNonExtendedCompositeStateMachine.States.Composite_S1.InnerStates.Inner_S2.class
                            .getAnnotation(Function.class),
                    CompositeStateMachineTransitionReferenceFromSuperNonExtendedCompositeStateMachine.States.Composite_S1.InnerStates.Inner_S2.class,
                    SuperStateMachine.States.Super_S2.CompositeTransitions.Super_S2_X.class);
            throw e;
        }
    }
}
