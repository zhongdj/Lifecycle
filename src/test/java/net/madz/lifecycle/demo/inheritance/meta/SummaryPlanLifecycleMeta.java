package net.madz.lifecycle.demo.inheritance.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.action.ConditionSet;
import net.madz.lifecycle.annotations.action.Conditional;
import net.madz.lifecycle.annotations.action.ConditionalTransition;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Conditions.VolumeMeasurable;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.States.Ongoing;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.States.VolumeLeftEmpty;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Transitions.AdjustTotalVolume;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Transitions.ConfirmFinish;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Transitions.CreateServiceOrder;
import net.madz.lifecycle.demo.inheritance.meta.SummaryPlanLifecycleMeta.Utils.VolumeMeasurableTransition;

@StateMachine
public interface SummaryPlanLifecycleMeta {

    @StateSet
    public static class States {

        @Functions({ @Function(transition = CreateServiceOrder.class, value = { Ongoing.class, VolumeLeftEmpty.class }),
                @Function(transition = ConfirmFinish.class, value = Done.class),
                @Function(transition = AdjustTotalVolume.class, value = { Ongoing.class, VolumeLeftEmpty.class }) })
        @Initial
        public static class Ongoing {}
        @Functions({ @Function(transition = ConfirmFinish.class, value = Done.class),
                @Function(transition = AdjustTotalVolume.class, value = { Ongoing.class, VolumeLeftEmpty.class }) })
        public static class VolumeLeftEmpty {}
        @End
        public static class Done {}
    }
    @TransitionSet
    public static class Transitions {

        @Conditional(judger = VolumeMeasurableTransition.class, condition = VolumeMeasurable.class)
        public static class CreateServiceOrder {}
        @Conditional(judger = VolumeMeasurableTransition.class, condition = VolumeMeasurable.class)
        public static class AdjustTotalVolume {}
        public static class ConfirmFinish {}
    }
    @ConditionSet
    public static class Conditions {

        public static interface VolumeMeasurable {

            boolean isVolumeLeft();
        }
    }
    static class Utils {

        public static class VolumeMeasurableTransition implements ConditionalTransition<VolumeMeasurable> {

            public Class<?> doConditionJudge(VolumeMeasurable measurable) {
                if ( !measurable.isVolumeLeft() ) {
                    return VolumeLeftEmpty.class;
                } else {
                    return Ongoing.class;
                }
            }
        }
    }
}
