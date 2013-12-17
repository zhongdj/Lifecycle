package net.madz.lifecycle.demo.relational.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.relational.meta.ConcreteTruckResourceLifecycleMeta.Transitions.Assign;
import net.madz.lifecycle.demo.relational.meta.ConcreteTruckResourceLifecycleMeta.Transitions.Detach;
import net.madz.lifecycle.demo.relational.meta.ConcreteTruckResourceLifecycleMeta.Transitions.Release;

@StateMachine
public interface ConcreteTruckResourceLifecycleMeta {

    @StateSet
    public static class States {

        @Initial
        @Functions({ @Function(transition = Assign.class, value = Busy.class), @Function(transition = Detach.class, value = Detached.class) })
        public static class Idle {}
        @Function(transition = Release.class, value = Idle.class)
        public static class Busy {}
        @End
        public static class Detached {}
    }
    @TransitionSet
    public class Transitions {

        public static class Assign {}
        public static class Release {}
        public static class Detach {}
    }
}
