package net.madz.lifecycle.demo.inheritance;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.demo.inheritance.meta.PlantResourceLifecycleMeta;

@LifecycleMeta(PlantResourceLifecycleMeta.class)
public interface IPlantResource {

    @Transition(PlantResourceLifecycleMeta.Transitions.Assign.class)
    void assign();

    @Transition(PlantResourceLifecycleMeta.Transitions.Release.class)
    void release();

    @Transition(PlantResourceLifecycleMeta.Transitions.Maintain.class)
    void doMaintain();

    @Transition(PlantResourceLifecycleMeta.Transitions.ConfirmMaintainOver.class)
    void confirmMaintainOver();

    @StateIndicator
    String getState();
}
