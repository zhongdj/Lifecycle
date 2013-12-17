package net.madz.lifecycle.demo.relational.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.InboundWhiles;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Relations.ConcreteTruckResource;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Relations.PlantResource;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Transitions.Cancel;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Transitions.Schedule;
import net.madz.lifecycle.demo.relational.meta.ServiceableLifecycleMeta.Transitions.Start;

@StateMachine
public interface ServiceableLifecycleMeta {

    @StateSet
    public static class States {

        @Initial
        @Function(transition = Schedule.class, value = Queued.class)
        public static class Created {}
        @Functions({ @Function(transition = Start.class, value = Ongoing.class), @Function(transition = Cancel.class, value = Cancelled.class) })
        @InboundWhiles({
                @InboundWhile(relation = PlantResource.class,
                        on = { PlantResourceLifecycleMeta.States.Idle.class, PlantResourceLifecycleMeta.States.Busy.class }),
                @InboundWhile(relation = ConcreteTruckResource.class, on = { ConcreteTruckResourceLifecycleMeta.States.Idle.class,
                        ConcreteTruckResourceLifecycleMeta.States.Busy.class }) })
        // Default @ValidWhiles = @InboundWhiles or Default @ValidWhile =
        // @InboundWhile
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
    }
    @RelationSet
    public static class Relations {

        // default to @Relation("plantResource")
        public static class PlantResource {}
        // default to @Relation("concreteTruckResource")
        public static class ConcreteTruckResource {}
    }
}
