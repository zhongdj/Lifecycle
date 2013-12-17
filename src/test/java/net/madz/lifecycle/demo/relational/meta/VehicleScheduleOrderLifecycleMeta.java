package net.madz.lifecycle.demo.relational.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Cancel;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Schedule;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Start;

@StateMachine
public interface VehicleScheduleOrderLifecycleMeta {

    @StateSet
    public static class States {

        @Initial
        @Function(transition = Schedule.class, value = Queued.class)
        public static class Created {}
        @Functions({ @Function(transition = Start.class, value = Ongoing.class), @Function(transition = Cancel.class, value = Cancelled.class) })
        public static class Queued {}
        @Functions({ @Function(transition = Finish.class, value = Finished.class), @Function(transition = Cancel.class, value = Cancelled.class) })
        public static class Ongoing {}
        @End
        public static class Finished {}
        @End
        public static class Cancelled {}
    }
    @TransitionSet
    public static class Transitions {

        public static class Schedule {}
        public static class Start {}
        public static class Finish {}
        public static class Cancel {}
        public static class DoTransport {}
        public static class DoConstruct {}
    }
}
