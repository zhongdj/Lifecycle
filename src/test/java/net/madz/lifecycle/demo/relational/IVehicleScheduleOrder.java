package net.madz.lifecycle.demo.relational;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Cancel;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Schedule;
import net.madz.lifecycle.demo.relational.meta.VehicleScheduleOrderLifecycleMeta.Transitions.Start;

@LifecycleMeta(VehicleScheduleOrderLifecycleMeta.class)
public interface IVehicleScheduleOrder {

    @Transition(Schedule.class)
    void doSchedule();

    @Transition(Start.class)
    void doStart();

    @Transition(Finish.class)
    void doFinish();

    @Transition(Cancel.class)
    void doCancel();

    @Transition
    void doTransport();

    @Transition
    void doConstruct();

    @StateIndicator
    String getState();
}
