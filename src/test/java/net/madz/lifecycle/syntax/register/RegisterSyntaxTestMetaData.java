package net.madz.lifecycle.syntax.register;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.CorrectStateMachineInheritanceSuperSyntax.Transitions.TransitionTwo;
import net.madz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.CorrectStateMachineSyntax.Transitions.TransitionOne;
import net.madz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.IncorrectStateMachineInheritanceSuperSyntax.Transitions.TransitionThree;

public class RegisterSyntaxTestMetaData extends BaseMetaDataTest {

    @StateMachine
    public static interface CorrectStateMachineSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionOne.class, value = StateB.class)
            static interface StateA {}
            @End
            static interface StateB {}
        }
        @TransitionSet
        static interface Transitions {

            static interface TransitionOne {}
        }
    }
    @LifecycleMeta(CorrectStateMachineSyntax.class)
    protected static class CorrectLifecycleMetaSyntax {

        private String state;

        @Transition(TransitionOne.class)
        public void foo() {}

        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String stateName) {
            this.state = stateName;
        }
    }
    protected static class WithoutMetadataAnnotationErrorSyntax {}
    @StateMachine
    public static interface CorrectStateMachineInheritanceSuperSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionTwo.class, value = StateD.class)
            static interface StateC {}
            @End
            static interface StateD {}
        }
        @TransitionSet
        static interface Transitions {

            static interface TransitionTwo {}
        }
    }
    @StateMachine
    public static interface CorrectStateMachineInheritanceChildSyntax extends CorrectStateMachineInheritanceSuperSyntax {}
    public static interface IncorrectStateMachineInheritanceSuperSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionThree.class, value = StateF.class)
            static interface StateE {}
            @End
            static interface StateF {}
        }
        @TransitionSet
        static interface Transitions {

            static interface TransitionThree {}
        }
    }
    @StateMachine
    public static interface IncorrectStateMachineInheritanceChildSyntax extends IncorrectStateMachineInheritanceSuperSyntax {}
    @StateMachine
    public static interface IncorrectStateMachineInheritanceChildWithMultiSuperInterfacesSyntax extends CorrectStateMachineInheritanceSuperSyntax,
            IncorrectStateMachineInheritanceSuperSyntax {}

    public RegisterSyntaxTestMetaData() {
        super();
    }

    protected static interface WrongStateMachineSyntaxWithoutAnnotation {}
    @LifecycleMeta(WrongStateMachineSyntaxWithoutAnnotation.class)
    protected static class WrongLifecycleMetaSyntaxWithStateMachineWithoutAnnotation {}
    public static class NonStateMachineClass {}
    @StateMachine
    public static class IllegalExtendsNonStateMachineClass extends NonStateMachineClass {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionThree.class, value = StateF.class)
            static interface StateE {}
            @End
            static interface StateF {}
        }
        @TransitionSet
        static interface Transitions {

            static interface TransitionThree {}
        }
    }
}