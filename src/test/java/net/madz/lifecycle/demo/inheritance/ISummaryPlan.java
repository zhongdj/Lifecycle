package net.madz.lifecycle.demo.inheritance;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.action.Condition;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Conditions.VolumeMeasurable;

@LifecycleMeta(SummaryPlanLifecycleMeta.class)
public interface ISummaryPlan {

    @Transition(SummaryPlanLifecycleMeta.Transitions.CreateServiceOrder.class)
    IServiceOrder createServiceOrder(IPlantResource p, IConcreteTruckResource c, double volume);

    @Transition(SummaryPlanLifecycleMeta.Transitions.AdjustTotalVolume.class)
    void adjustVolume(double volume);

    @Transition(SummaryPlanLifecycleMeta.Transitions.ConfirmFinish.class)
    void confirmFinish();

    @Condition(SummaryPlanLifecycleMeta.Conditions.VolumeMeasurable.class)
    VolumeMeasurable getVolumeMeasurable();

    @StateIndicator
    String getState();
}
