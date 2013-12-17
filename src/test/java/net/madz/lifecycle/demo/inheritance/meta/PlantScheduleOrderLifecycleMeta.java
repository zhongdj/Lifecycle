package net.madz.lifecycle.demo.inheritance.meta;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.relation.ValidWhile;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Relations.ServiceOrder;
import net.madz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.inheritance.meta.PlantScheduleOrderLifecycleMeta.Transitions.Start;

@StateMachine(parentOn = ServiceOrderLifecycleMeta.class)
public interface PlantScheduleOrderLifecycleMeta extends OrderLifecycleMeta {

    @StateSet
    static class States extends OrderLifecycleMeta.States {

        @InboundWhile(relation = ServiceOrder.class, on = OrderLifecycleMeta.States.Queued.class)
        // Default @ValidWhile(relation="serviceOrder", on =
        // {ServiceOrderLifecycleMeta.States.Queued.class})
        @Functions({ @Function(transition = Start.class, value = Ongoing.class) })
        static class Queued extends OrderLifecycleMeta.States.Queued {}
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class })
        // Default @ValidWhile(IServiceOrder.States.Ongoing.class)
        @Functions({ @Function(transition = Finish.class, value = Finished.class) })
        static class Ongoing extends OrderLifecycleMeta.States.Ongoing {}
        @End
        @InboundWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class })
        @ValidWhile(relation = ServiceOrder.class, on = { ServiceOrderLifecycleMeta.States.Ongoing.class, ServiceOrderLifecycleMeta.States.Finished.class })
        // Default @Functions({})
        static class Finished extends OrderLifecycleMeta.States.Finished {}
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
