package net.madz.lifecycle.syntax.lm;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.syntax.lm.LMSyntaxMetadata.S2.Transitions.NS1_Z;
import net.madz.lifecycle.syntax.lm.LMSyntaxMetadata.S3.Transitions.S3_Y;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxNegativeTest extends LMSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_002_3212_if_LM_partial_concreting_transitions_A() throws VerificationException {
        @LifecycleRegistry(NLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_NOT_CONCRETED_IN_LM, NS1_Z.class.getSimpleName(),
                    S2.class.getName(), NLM_1.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3212_if_LM_partial_concreting_transitions_B() throws VerificationException {
        @LifecycleRegistry(NLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_NOT_CONCRETED_IN_LM, NS1_Z.class.getSimpleName(),
                    S2.class.getName(), NLM_2.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3213_if_transition_method_cannot_bind_to_any_transition_via_method_name() throws VerificationException {
        @LifecycleRegistry(NLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_METHOD_NAME_INVALID, S2.class.getName(), "nS1_Xyz",
                    NLM_3.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3211_if_transition_method_reference_transition_beyond_stateMachine() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_METHOD_WITH_INVALID_TRANSITION_REFERENCE, NLM_4.class
                    .getMethod("nS1_X").getAnnotation(Transition.class), "nS1_X", NLM_4.class.getName(), S2.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3214_if_some_mandatory_transitions_have_multi_methods() throws VerificationException {
        @LifecycleRegistry(NLM_5.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REDO_CORRUPT_RECOVER_TRANSITION_HAS_ONLY_ONE_METHOD,
                    S3_Y.class.getSimpleName(), "@Redo", S3.class.getName(), NLM_5.class.getName());
            throw e;
        }
    }
}
