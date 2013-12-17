package net.madz.lifecycle.demo.relational.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.demo.relational.meta.PlantResourceLifecycleMeta.Transitions.Assign;
import net.madz.lifecycle.demo.relational.meta.PlantResourceLifecycleMeta.Transitions.ConfirmMaintainOver;
import net.madz.lifecycle.demo.relational.meta.PlantResourceLifecycleMeta.Transitions.Maintain;
import net.madz.lifecycle.demo.relational.meta.PlantResourceLifecycleMeta.Transitions.Release;

@StateMachine
public interface PlantResourceLifecycleMeta {

    @StateSet
    public static class States {

        @Functions({ @Function(transition = Assign.class, value = Busy.class), @Function(transition = Maintain.class, value = Maintaining.class) })
        public static class Idle {}
        @Function(transition = Release.class, value = Idle.class)
        public static class Busy {}
        @Function(transition = ConfirmMaintainOver.class, value = Idle.class)
        public static class Maintaining {}
    }
    @TransitionSet
    public static class Transitions {

        public static class Assign {}
        public static class Release {}
        public static class Maintain {}
        public static class ConfirmMaintainOver {}
    }
}
