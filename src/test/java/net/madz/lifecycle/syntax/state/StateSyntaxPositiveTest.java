package net.madz.lifecycle.syntax.state;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class StateSyntaxPositiveTest extends StateSyntaxMetadata {

    @Test
    public void state_function_with_valid_conditional_transition() throws VerificationException {
        @LifecycleRegistry(S4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void state_function_with_valid_next_state_set() throws VerificationException {
        @LifecycleRegistry(S7.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void composite_state_with_valid_transition_and_shortcut() throws VerificationException {
        @LifecycleRegistry(PCS1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void state_overriding_function_referring_same_transition_with_super_state() throws VerificationException {
        @LifecycleRegistry(State_Overriding_Function_Referring_Same_Transition_With_Super_State.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void composite_state_with_reference_transition_in_owning_statemachine() throws VerificationException {
        @LifecycleRegistry(StateMachineWithFunctionInCompositeStateReferencingOuterTransition.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void composite_state_with_reference_transition_in_super_statemachine() throws VerificationException {
        @LifecycleRegistry(FunctionInCompositeStateReferencingTransitionInSuper.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }
}
