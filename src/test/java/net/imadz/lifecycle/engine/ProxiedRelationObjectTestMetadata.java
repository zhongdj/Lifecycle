package net.imadz.lifecycle.engine;

import net.imadz.lifecycle.annotations.*;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Barry on 19/12/2016.
 */
public class ProxiedRelationObjectTestMetadata extends EngineTestBase {


    @StateMachine
    public static interface TheRelationFSM {
        @StateSet
        static interface States {
            @Initial
            @Transition(event = Events.Complete.class, value = Completed.class)
            static class New {
            }

            @Final
            static class Completed {
            }
        }

        @EventSet
        static interface Events {

            static class Complete {
            }
        }
    }

    @LifecycleMeta(TheRelationFSM.class)
    public static interface TheRelation {
        @StateIndicator
        String getState();

        @Event(TheRelationFSM.Events.Complete.class)
        void doComplete();
    }

    @LifecycleMeta(TheRelationFSM.class)
    public static class TheRelationImpl extends ReactiveObject implements TheRelation {

        public TheRelationImpl() {
            initialState(TheRelationFSM.States.New.class.getSimpleName());
        }

        @Override
        @Event(TheRelationFSM.Events.Complete.class)
        public void doComplete() {
            //doing nothing
        }

        @Override
        public String toString() {
            return "I am a Proxied Object";
        }
    }

    @StateMachine
    public static interface TheRelationalFSM {
        @StateSet
        static interface States {
            @Initial
            @Transition(event = Events.Plan.class, value = Planned.class)
            @ValidWhile(relation = Relations.CertainRelation.class, on = {TheRelationFSM.States.Completed.class})
            static class Created {
            }

            @Transition(event = Events.Finish.class, value = Done.class)
            static class Planned {
            }

            @Final
            static class Done {
            }
        }

        @EventSet
        static interface Events {
            static class Plan {
            }

            static class Finish {
            }
        }

        @RelationSet
        static interface Relations {
            @RelateTo(TheRelationFSM.class)
            static interface CertainRelation {
            }
        }
    }

    @LifecycleMeta(TheRelationalFSM.class)
    public static class TheRelationalObject extends ReactiveObject {
        public TheRelationalObject(TheRelationImpl theRelation) {
            initialState(TheRelationalFSM.States.Created.class.getSimpleName());
            this.theRelation = theRelation;
        }

        public final TheRelationImpl theRelation;

        @Event
        public void plan() {
        }

        @Event
        public void finish() {
        }

        @Relation(TheRelationalFSM.Relations.CertainRelation.class)
        public TheRelation getTheRelation() {
            //JAVA DYNAMIC PROXY
            return (TheRelation) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{TheRelation.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if ("setState".equals(method.getName())) {
                        throw new IllegalAccessException("setState cannot be accessed directly.");
                    } else if ("getState".equals(method.getName())) {
                        return theRelation.getState();
                    } else if ("doComplete".equals(method.getName())) {
                        theRelation.doComplete();
                        return null;
                    } else if ("toString".equals(method.getName())) {
                        return "I am a Proxied Object";
                    } else {
                        System.out.println(method.toGenericString());
                        throw new NoSuchMethodException();
                    }
                }
            });
        }
    }

}
