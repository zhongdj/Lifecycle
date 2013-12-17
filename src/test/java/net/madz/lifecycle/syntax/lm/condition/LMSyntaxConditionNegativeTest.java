package net.madz.lifecycle.syntax.lm.condition;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.syntax.lm.condition.LMSyntaxConditionMetadata.S1.Conditions.S1_Condition_A;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxConditionNegativeTest extends LMSyntaxConditionMetadata {

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3231_if_condition_reference_invalid() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_REFERENCE_INVALID,
                    NLM_1.class.getMethod("getConditionA"), S1.Transitions.S1_Transition_X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3232_if_multiple_methods_reference_same_condition() throws VerificationException {
        @LifecycleRegistry(NLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_MULTIPLE_METHODS_REFERENCE_SAME_CONDITION, NLM_2.class,
                    S1.Conditions.S1_Condition_A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3230_if_condition_not_covered() throws VerificationException {
        @LifecycleRegistry(NLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_NOT_COVERED, NLM_3.class, S1.class.getName(),
                    S1.class.getName() + ".ConditionSet." + S1_Condition_A.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3233_if_condition_object_does_not_implement_condition_class() throws NoSuchMethodException, SecurityException, VerificationException {
        @LifecycleRegistry(ConditionObjectDoesNotImplementConditionClass.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_OBJECT_DOES_NOT_IMPLEMENT_CONDITION_INTERFACE,
                    ConditionObjectDoesNotImplementConditionClass.class.getMethod("getConditionA"), S1_Condition_A.class);
            throw e;
        }
    }
}
