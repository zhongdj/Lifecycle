package net.madz.lifecycle.syntax.lm.condition;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxConditionPositiveTests extends LMSyntaxConditionMetadata {

    @Test
    public final void should_concrete_condition_in_standalone_state_machine() throws VerificationException {
        @LifecycleRegistry(PLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            public Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public final void should_concrete_condition_in_composite_state_machine() throws VerificationException {
        @LifecycleRegistry(PLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            public Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public final void should_concrete_condition_in_super_state_machine() throws VerificationException {
        @LifecycleRegistry(PLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            public Registry() throws VerificationException {}
        }
        new Registry();
    }
}
