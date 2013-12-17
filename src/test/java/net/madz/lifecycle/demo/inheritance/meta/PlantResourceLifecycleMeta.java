package net.madz.lifecycle.demo.inheritance.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.demo.inheritance.meta.PlantResourceLifecycleMeta.Transitions.ConfirmMaintainOver;
import net.madz.lifecycle.demo.inheritance.meta.PlantResourceLifecycleMeta.Transitions.Maintain;

@StateMachine
public interface PlantResourceLifecycleMeta extends SchedulableLifecycleMeta {

    @StateSet
    public static class States extends SchedulableLifecycleMeta.States {

        @Function(transition = Maintain.class, value = Maintaining.class)
        public static class Idle extends SchedulableLifecycleMeta.States.Idle {}
        @Function(transition = ConfirmMaintainOver.class, value = Idle.class)
        public static class Maintaining {}
    }
    @TransitionSet
    public static class Transitions extends SchedulableLifecycleMeta.Transitions {

        public static class Maintain {}
        public static class ConfirmMaintainOver {}
    }
}
