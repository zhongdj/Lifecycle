package net.madz.lifecycle.syntax.basic;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.lifecycle.syntax.basic.StateSetSyntaxMetadata.Positive.Transitions.T;

public class StateSetSyntaxMetadata extends BaseMetaDataTest {

    @StateMachine
    protected static interface Positive {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = T.class, value = B.class)
            static interface A {};
            @End
            static interface B {};
        }
        @TransitionSet
        static interface Transitions {

            static interface T {};
        }
    }
    @StateMachine
    protected static interface Negative_No_InnerClasses {}
    @StateMachine
    protected static interface Negative_No_StateSet_and_TransitionSet {

        static interface States {

            @Initial
            @Function(transition = T.class, value = B.class)
            static interface A {};
            @End
            static interface B {};
        }
        static interface Transitions {

            static interface T {};
        }
    }
    @StateMachine
    protected static interface Negative_Multi_StateSet_Multi_TransitionSet {

        @StateSet
        static interface StatesA {}
        @StateSet
        static interface StatesB {}
        @TransitionSet
        static interface TransitionsA {}
        @TransitionSet
        static interface TransitionsB {}
    }
    @StateMachine
    protected static interface Negative_No_State_No_Transition {

        @StateSet
        static interface States {}
        @TransitionSet
        static interface Transitions {}
    }
    @StateMachine
    protected static interface Negative_StateSet_Without_InitalState_And_EndState {

        @StateSet
        static interface States {

            static interface Start {}
            static interface End {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Queue {}
        }
    }
    @StateMachine
    protected static interface Negative_StateSet_With_Multi_InitalState {

        @StateSet
        static interface States {

            @Initial
            static interface Start {}
            @Initial
            static interface Queued {}
            @End
            static interface Ended {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Queue {}
        }
    }
}