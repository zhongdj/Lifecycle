package net.madz.lifecycle.syntax.lm;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxPositiveTest extends LMSyntaxMetadata {

    @Test
    public void lm_with_correct_stateMachine() throws VerificationException {
        @LifecycleRegistry(PLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void lm_concrete_all_transitions_with_explicit_transition_name() throws VerificationException {
        @LifecycleRegistry(PLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void lm_concrete_all_transitions_with_implicit_transition_name() throws VerificationException {
        @LifecycleRegistry(PLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void transitions_with_corrupt_redo_recover_with_only_one_method() throws VerificationException {
        @LifecycleRegistry(PLM_4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }
}
