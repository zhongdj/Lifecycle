Finite State Machine (FSM)


Try demos once you have following software installed:
1. JDK7+
2. maven 3+
3. github client

And then execute the following commands:

git clone https://github.com/zhongdj/LifecycleStaticWeaverDemos
cd LifecycleStaticWeaverDemos
mvn test

For further information, please refer demos projects:

1. (Suggest) https://github.com/zhongdj/LifecycleStaticWeaverDemos
2. https://github.com/zhongdj/LifecycleRuntimeBytecodeManipulationDemos

Using: Lifecycle-StaticWeaver-maven-plugin to process classes
https://github.com/zhongdj/Lifecycle-StaticWeaver-maven-plugin

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                  Quick Look: Stand-alone Reactive Object
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
@LifecycleMeta(ServiceableLifecycleMeta.class)
public interface IServiceOrder {

    @Transition(Schedule.class)
    void allocateResources();

    @Transition(Start.class)
    void confirmStart();

    @Transition(Finish.class)
    void confirmFinish();

    @StateIndicator
    String getServiceOrderState();
}

----------------------------------------------------------------------------------------------------------

@StateMachine
public interface ServiceableLifecycleMeta {

    @StateSet
    static class States {

        @Initial
        @Function(transition = Schedule.class, value = Queued.class)
        static class Created {}

        @Functions({ @Function(transition = Start.class, value = Ongoing.class),
                @Function(transition = Cancel.class, value = Cancelled.class) })
        static class Queued {}

        @Functions({ @Function(transition = Finish.class, value = Finished.class),
                @Function(transition = Cancel.class, value = Cancelled.class) })
        static class Ongoing {}

        @End
        static class Finished {}

        @End
        static class Cancelled {}
    }

    @TransitionSet
    static class Transitions {

        static class Schedule {}

        static class Start {}

        static class Finish {}

        static class Cancel {}
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                 Quick Look: Dependent Reactive Object
////////////////////////////////////////////////////////////////////////////////////////////////////////////
@LifecycleMeta(ServiceableLifecycleMeta.class)
public interface IServiceOrder {

    @Transition(Schedule.class)
    void allocateResources(@Relation(PlantResource.class) IPlantResource plantResource,
            @Relation(ConcreteTruckResource.class) IConcreteTruckResource truckResource);

    @Transition(ServiceableLifecycleMeta.Transitions.Start.class)
    void confirmStart();

    @Transition(ServiceableLifecycleMeta.Transitions.Finish.class)
    void confirmFinish();

    @StateIndicator
    String getServiceOrderState();
}

----------------------------------------------------------------------------------------------------------

@StateMachine
public interface ServiceableLifecycleMeta {

    @StateSet
    public static class States {

        @Initial
        @Function(transition = Schedule.class, value = Queued.class)
        @InboundWhiles({
                @InboundWhile(relation = PlantResource.class, on = { PlantResourceLifecycleMeta.States.Idle.class,
                        PlantResourceLifecycleMeta.States.Busy.class }),
                @InboundWhile(relation = PlantResource.class, on = {
                        ConcreteTruckResourceLifecycleMeta.States.Idle.class,
                        ConcreteTruckResourceLifecycleMeta.States.Busy.class }) })
        public static class Created {}

        @Functions({ @Function(transition = Start.class, value = Ongoing.class),
                @Function(transition = Cancel.class, value = Cancelled.class) })
        public static class Queued {}

        @Functions({ @Function(transition = Finish.class, value = Finished.class),
                @Function(transition = Cancel.class, value = Cancelled.class) })
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


////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                 Quick Look: Lifecycle Inheritance
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@LifecycleMeta(ConcreteTruckResourceLifecycleMeta.class)
@StateIndicator
//Default with getState
public interface IConcreteTruckResource {

    @Transition
    // default to @Transition(Assign.class) use assign -> Assign
    void assign();

    @Transition(ConcreteTruckResourceLifecycleMeta.Transitions.Release.class)
    void doRelease();

    @Transition
    // default to @Transition(Detach.class) use detach -> Detach
    void detach();
}

----------------------------------------------------------------------------------------------------------
@StateMachine
public interface ConcreteTruckResourceLifecycleMeta extends SchedulableLifecycleMeta {

    @StateSet
    public static class States extends SchedulableLifecycleMeta.States {

        @Initial
        @Function(transition = Detach.class, value = Detached.class)
        public static class Idle extends SchedulableLifecycleMeta.States.Idle {}

        @End
        public static class Detached {}
    }

    @TransitionSet
    public class Transitions extends SchedulableLifecycleMeta.Transitions {

        public static class Detach {}
    }
}
----------------------------------------------------------------------------------------------------------
@StateMachine
public interface SchedulableLifecycleMeta {

    @StateSet
    public static class States {

        @Functions({ @Function(transition = Assign.class, value = Busy.class) })
        public static class Idle {}

        @Function(transition = Release.class, value = Idle.class)
        public static class Busy {}
    }

    @TransitionSet
    public static class Transitions {

        public static class Assign {}

        public static class Release {}
    }
}




////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                 Quick Look: Long Process Reactive Live Object
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@StateMachine
public interface IDownloadProcess {

    @StateSet
    static class States {

        @Initial
        @Functions({ @Function(transition = Prepare.class, value = Queued.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class New {}

        @Running
        @Functions({ @Function(transition = Pause.class, value = Paused.class),
                @Function(transition = Start.class, value = Started.class),
                @Function(transition = Deactive.class, value = InactiveQueued.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class Queued {}

        @Running
        @Functions({ @Function(transition = Pause.class, value = Paused.class),
                @Function(transition = Receive.class, value = Started.class),
                @Function(transition = Deactive.class, value = InactiveStarted.class),
                @Function(transition = Err.class, value = Failed.class),
                @Function(transition = Finish.class, value = Finished.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class Started {}

        @Corrupted(recoverPriority = 1)
        @Functions({ @Function(transition = Activate.class, value = Queued.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class InactiveQueued {}

        @Corrupted(recoverPriority = 0)
        @Functions({ @Function(transition = Activate.class, value = Queued.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class InactiveStarted {}

        @Stopped
        @Functions({ @Function(transition = Resume.class, value = New.class),
                @Function(transition = Restart.class, value = New.class),
                @Function(transition = Remove.class, value = Removed.class) })
        static class Paused {}

        @Stopped
        @Functions({ @Function(transition = Restart.class, value = New.class),
                @Function(transition = Resume.class, value = New.class),
                @Function(transition = Remove.class, value = Removed.class), })
        static class Failed {}

        @Stopped
        @Functions({ @Function(transition = Restart.class, value = New.class),
                @Function(transition = Remove.class, value = Removed.class), })
        static class Finished {}

        @End
        static class Removed {}
    }

    @TransitionSet
    static class Transitions {

        @Recover
        @Timeout(3000L)
        static class Activate {}

        @Corrupt
        @Timeout(3000L)
        static class Deactive {}

        @Fail
        @Timeout(3000L)
        static class Err {}

        static class Prepare {}

        static class Start {}

        static class Resume {}

        static class Pause {}

        static class Finish {}

        static class Receive {}

        @Redo
        @Timeout(3000L)
        static class Restart {}

        static class Remove {}
    }
}

Lifecycle exists since information variations happening always. A subset of information varies together, and others do not. To simplify describing system states(data) 
manipulation by instructions, a block of data and a few blocks of instructions were introduced to make the expression more concisely and clearly, 
such as the concepts: Struct and Function constructs in C programming language, or Class in java (OO) programming language. 

C's structure or Java's Class can shrink the scope of data or states of system, based on structure or class, 
new concepts (types) can be created to describe a new SCOPE. Granularity always be there while talking about scope, because scope is relative.
One subset of information(data) was created and was destroyed at the moment of another subset might be just destroyed and recreated again. And
once go into one subset of data, the states inside also can vary at different stage or moment. Defects happen because some state should 
be changed at some stage but it did not, or some state changed incorrectly, or some state should be recognized but they were missed, or scoping incorrectly, or ignore the relationships states validation during state change.  

To reduce defects creation, at logical concept layer, programming language provides many constructs (class, interface, inheritance and etc.) and experts create many design principles. 
Such as Single Responsibility Principle, which is actually down-sizing the scope of the subset of data and the scope of instructions blocks to make
it to become easier to manage the variations on the data. On the contrary side, once much more data or state be included in a certain scope (struct or class),
there will be much more scopes inside the enclosing scope (struct or class), which will make the enclosing scope(struct or class) be too hard and too complicated to understand and to maintain.  

Life cycle is a fundamental concept and a tool to help to identify, and describe the variation rules of a subset data,  and to design scopes from the variation rule. 
And this is called abstraction, which is defined as to Understand, to Differentiate, to Name, to Express. 
Such as to identify several classes, which can be also named as entities or relationships. And to identify several interfaces through interactions inside relationships.
For example, an JMSQueue, Producer, Consumer are all entities, and a Connection is a relationship between Producer/Consumer and JMS Queue.
And JMSQueue define a scope of state(data), which will vary totally different with a scope of a business Order.
And the Connection define a scope of interactions between those entities. So JMSQueue's life cycle is different from business order's.
During a brief life cycle of JMSQueue, such as Created, Initialized, Running, Stopped, and Destroyed. Connections ONLY happen while JMSQueue's Running stage(state).
That's a kind of difference between Entities and Relationships. 

Sometimes an entity or a relationship's life cycle is naturally simple, and some times life cycle is naturally complex. 
While the complexity is identified among entities, such as: dependencies, aggregation, composition, and so on. And then many boring state validity checks should be coded and performed appropriately.    
Lacking of validation check on the scope itself or the scopes related will lead to mis-operation on the scoped data, which will make system state a chaos, especially after the error state get into system, and it had been
processed for a lot of operations. So both state validity check and appropriate state set are important for reducing defect numbers and constructing robust systems.  

Lifecycle project aims at describing an entity's or a relationship's life cycle by define a StateMachine, and provides life cycle definition in a declarative style, 
and it focus on life cycle non functional requirements at following areas:

1. Implicit life cycle service for reactive (business) object.

1.1 Hiding state transition validation from application developer perspective.

Including: 

1.1.1 Stand-alone reactive object state transition validation
1.1.2 Independent reactive object state transition validation
1.1.2.1 Child reactive object state transition validation in a deep hierarchical business model, whose lifecycle is totally covered by parent object.
NOTE: Once the root reactive object is going to transit to a new State, such as Locked or Cancelled, which will influence all of the children under
many different layers, finish the Database Transaction fast on the root first, and then all the children's states are invalid to perform any operation, 
except state synchronization transition from parent.  

1.1.2.2 Relative reactive objects state transition validation, whose lifecycle is dependent on some other object.

1.2 Hiding setting business object's state indicator operations from both business object client and application developer perspective.
1.2.1 setting state for Stand-alone reactive object
1.2.2 setting state for Independent reactive object and avoid concurrency issues on related objects.
1.2.2.1 setting state for Child reactive object state transition with touch all parent objects within Optimistic Lock context(MAYBE in Enterprise app)
1.2.2.2 setting state for Child reactive object state transition with touch all parent objects within Pessimistic Lock context(MAYBE in Mobile app) 
1.2.2.3 setting state for relative reactive object state transition with touch all parent objects within Optimistic Lock context(MAYBE in Enterprise app)
1.2.2.4 setting state for relative reactive object state transition with touch all parent objects within Pessimistic Lock context(MAYBE in Mobile app)

2. Implicit life cycle service for long time recoverable process (Reactive object) with transient illegal state fix and error handling, such as download process.
2.1 Provide a serials of annotations to modeling process states, such as category them into @Initial, @Running, @Stopped, @End groups, 
    to differentiate key state of the process, for example, before all the service started, a @Running process must be a invalid state which needs to be fixed (corrupted) 
2.2 Provide a serials of annotations to modeling process transitions, such as @Corrupt, @Fail, @Redo, @Recover, @Timeout, to provide further operation to the process, 
    such as the invalid process mentioned above needs to be applied with @Corrupt operation.

3. Provide life cycle events from reactive object's state change.

4. Provide life cycle intercepts during reactive object's prior or post state changes happens.

