package net.madz.lifecycle.syntax.lm.callback;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class CallbackPositiveTests extends CallbackTestBase {

    @Test
    public final void test_callbacks_with_default_value() throws VerificationException {
        @LifecycleRegistry(PLM_With_CallBacksWithDefaultValues.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }
}
