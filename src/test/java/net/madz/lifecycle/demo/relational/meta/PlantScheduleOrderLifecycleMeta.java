package net.madz.lifecycle.demo.relational.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.relation.ValidWhile;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.demo.relational.meta.PlantScheduleOrderLifecycleMeta.Relations.ServiceOrder;
import net.madz.lifecycle.demo.relational.meta.PlantScheduleOrderLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.relational.meta.PlantScheduleOrderLifecycleMeta.Transitions.Start;

@StateMachine(parentOn = ServiceableLifecycleMeta.class)
public interface PlantScheduleOrderLifecycleMeta {

    @StateSet
    static class States {

        @Initial
        @Functions({ @Function(transition = Start.class, value = Working.class) })
        static class Created {}
        /*
         * on=Queued: ServiceOrder state change last, while Plant Operator
         * Triggers or Truck Driver Triggers
         */
        @Functions({ @Function(transition = Finish.class, value = Done.class) })
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceableLifecycleMeta.States.Queued.class })
        @ValidWhile(relation = ServiceOrder.class, on = { ServiceableLifecycleMeta.States.Queued.class, ServiceableLifecycleMeta.States.Ongoing.class })
        static class Working {}
        @End
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceableLifecycleMeta.States.Ongoing.class })
        @ValidWhile(relation = ServiceOrder.class, on = { ServiceableLifecycleMeta.States.Ongoing.class, ServiceableLifecycleMeta.States.Finished.class })
        // Default @Functions({})
        static class Done {}
    }
    @TransitionSet
    public static class Transitions {

        public static class Start {}
        public static class Finish {}
    }
    @RelationSet
    public static class Relations {

        public static class ServiceOrder {}
    }
}
