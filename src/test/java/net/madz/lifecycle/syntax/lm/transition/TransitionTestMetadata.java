package net.madz.lifecycle.syntax.lm.transition;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.action.Corrupt;
import net.madz.lifecycle.annotations.action.Recover;
import net.madz.lifecycle.annotations.action.Redo;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.syntax.BaseMetaDataTest;

public class TransitionTestMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface SpecialTranstionStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.Start.class, value = { Running.class })
            static interface Queued {}
            @Functions({ @Function(transition = Transitions.Inactivate.class, value = { InactiveRunning.class }),
                    @Function(transition = Transitions.Stop.class, value = { Stopped.class }) })
            static interface Running {}
            @Functions({ @Function(transition = Transitions.Activate.class, value = { Running.class }),
                    @Function(transition = Transitions.Restart.class, value = { Running.class }),
                    @Function(transition = Transitions.Stop.class, value = { Stopped.class }) })
            static interface InactiveRunning {}
            @End
            static interface Stopped {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Start {}
            @Corrupt
            static interface Inactivate {}
            @Recover
            static interface Activate {}
            @Redo
            static interface Restart {}
            static interface Stop {}
        }
    }
    @LifecycleMeta(SpecialTranstionStateMachine.class)
    static interface PositiveProcess {

        @Transition
        void start();

        @Transition
        void inactivate();

        @Transition
        void activate();

        @Transition
        void stop();

        @Transition
        void restart();

        @StateIndicator
        String getState();
    }
    @LifecycleMeta(SpecialTranstionStateMachine.class)
    static interface NegativeProcess {

        @Transition
        void start(int i);

        @Transition
        void inactivate(int i);

        @Transition
        void activate(int i);

        @Transition
        void stop(int i);

        @Transition
        void restart(int i);

        @StateIndicator
        String getState();
    }
}
