package net.madz.lifecycle.demo.inheritance;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.demo.inheritance.meta.VehicleScheduleOrderLifecycleMeta;
import net.madz.lifecycle.demo.inheritance.meta.VehicleScheduleOrderLifecycleMeta.States.Ongoing.SubTransitions.DoConstruct;
import net.madz.lifecycle.demo.inheritance.meta.VehicleScheduleOrderLifecycleMeta.States.Ongoing.SubTransitions.DoTransport;

@LifecycleMeta(VehicleScheduleOrderLifecycleMeta.class)
public interface IVehicleScheduleOrder {

    @Transition(VehicleScheduleOrderLifecycleMeta.Transitions.Start.class)
    void doLoad();

    @Transition(DoTransport.class)
    void doTransport();

    @Transition(DoConstruct.class)
    void doConstruct();
}
