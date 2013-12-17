package net.madz.lifecycle.syntax.basic;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.verification.VerificationException;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateSetSyntaxPositiveTest extends StateSetSyntaxMetadata {

    @Test
    public void test_positive_state_set_syntax() {
        @LifecycleRegistry(Positive.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            fail("No Verificiation Exception expected.");
        }
    }
}
