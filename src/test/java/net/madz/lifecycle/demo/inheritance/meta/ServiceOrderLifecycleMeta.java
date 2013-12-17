package net.madz.lifecycle.demo.inheritance.meta;

import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.ErrorMessage;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.InboundWhiles;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.ConcreteTruckResource;
import net.madz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.PlantResource;
import net.madz.lifecycle.demo.inheritance.meta.ServiceOrderLifecycleMeta.Relations.SummaryPlan;

@StateMachine
public interface ServiceOrderLifecycleMeta extends OrderLifecycleMeta {

    @StateSet
    public class States extends OrderLifecycleMeta.States {

        @InboundWhiles({ @InboundWhile(relation = SummaryPlan.class, on = { SummaryPlanLifecycleMeta.States.Ongoing.class }, otherwise = {
                @ErrorMessage(states = { SummaryPlanLifecycleMeta.States.VolumeLeftEmpty.class }, bundle = "scheduling", code = "100-0002"),
                @ErrorMessage(states = { SummaryPlanLifecycleMeta.States.Done.class }, bundle = "scheduling", code = "100-0003") }) })
        public static class Created extends OrderLifecycleMeta.States.Created {}
        @InboundWhiles({
                @InboundWhile(relation = PlantResource.class,
                        on = { PlantResourceLifecycleMeta.States.Idle.class, PlantResourceLifecycleMeta.States.Busy.class }),
                @InboundWhile(relation = ConcreteTruckResource.class, on = { ConcreteTruckResourceLifecycleMeta.States.Idle.class,
                        ConcreteTruckResourceLifecycleMeta.States.Busy.class }) })
        public static class Queued extends OrderLifecycleMeta.States.Queued {}
    }
    @TransitionSet
    public class Transitions extends OrderLifecycleMeta.Transitions {}
    @RelationSet
    public static class Relations {

        public static class SummaryPlan {}
        // default to @Relation("plantResource")
        public static class PlantResource {}
        // default to @Relation("concreteTruckResource")
        public static class ConcreteTruckResource {}
    }
}
