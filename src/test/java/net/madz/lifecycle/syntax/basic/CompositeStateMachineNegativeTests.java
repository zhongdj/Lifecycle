package net.madz.lifecycle.syntax.basic;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.verification.VerificationException;

import org.junit.Ignore;
import org.junit.Test;

public class CompositeStateMachineNegativeTests extends BaseMetaDataTest {

    @StateMachine
    static interface CompositeExtendsOwningStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = CompositeExtendsOwningStateMachine.Transitions.PCS1_X.class, value = PCS1_B.class)
            static interface PCS1_A {}
            @CompositeState
            @Function(transition = CompositeExtendsOwningStateMachine.Transitions.PCS1_Y.class, value = PCS1_C.class)
            static interface PCS1_B extends CompositeExtendsOwningStateMachine {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = CompositeExtendsOwningStateMachine.States.PCS1_B.CTransitions.PCS1_CX.class, value = PCS1_CB.class)
                    static interface PCS1_CA {}
                    @Function(transition = CompositeExtendsOwningStateMachine.States.PCS1_B.CTransitions.PCS1_CX.class, value = PCS1_CC.class)
                    static interface PCS1_CB {}
                    @End
                    @ShortCut(PCS1_C.class)
                    static interface PCS1_CC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface PCS1_CX {}
                }
            }
            @End
            static interface PCS1_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PCS1_X {}
            static interface PCS1_Y {}
        }
    }

    @Test(expected = VerificationException.class)
    @Ignore
    public void should_throw_exception_002_2804_if_composite_state_extends_owning_stateMachine() throws VerificationException {
        @LifecycleRegistry(CompositeExtendsOwningStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_CANNOT_EXTENDS_OWNING_STATEMACHINE,
                    CompositeExtendsOwningStateMachine.States.PCS1_B.class);
            throw e;
        }
    }
}
