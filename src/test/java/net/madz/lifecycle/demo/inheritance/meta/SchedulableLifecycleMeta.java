package net.madz.lifecycle.demo.inheritance.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.inheritance.meta.SchedulableLifecycleMeta.Transitions.Assign;
import net.madz.lifecycle.demo.inheritance.meta.SchedulableLifecycleMeta.Transitions.Release;

@StateMachine
public interface SchedulableLifecycleMeta {

    @StateSet
    // abstract only indicate there is no final(@End) states definition
    public static abstract class States {

        @Initial
        @Functions({ @Function(transition = Assign.class, value = Busy.class) })
        public static class Idle {}
        @Function(transition = Release.class, value = Idle.class)
        public static class Busy {}
    }
    @TransitionSet
    public static class Transitions {

        public static class Assign {}
        public static class Release {}
    }
}
