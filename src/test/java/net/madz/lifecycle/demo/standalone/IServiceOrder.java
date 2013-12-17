package net.madz.lifecycle.demo.standalone;

import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Transitions.Cancel;
import net.madz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Transitions.Finish;
import net.madz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Transitions.Schedule;
import net.madz.lifecycle.demo.standalone.ServiceableLifecycleMeta.Transitions.Start;

/**
 * This interface is the business interface for some domain.
 * ONLY two things to be noticed:
 * 1. Provide the @StateIndicator
 * 1.1 Directly put @StateIndicator on getter method or on field, and state
 * setter will never open for application
 * 1.2 Or to put @StateIndicator on Type definition, and provide a state
 * indicator property name.
 * 
 * 2. Specify actions corresponding to the transitions defined at life cycle
 * meta data with @Transition
 * 2.1 Leave @Transition with default value while the action method name equals
 * with the transition class simple name
 * 2.2 Specify the transition value with the defined transition class when their
 * names are not equal
 * 
 * @author Barry
 * 
 */
@LifecycleMeta(ServiceableLifecycleMeta.class)
public interface IServiceOrder {

    @Transition(Schedule.class)
    void allocateResources(final long summaryPlanId, final long truckResourceId, final long plangResourceId);

    @Transition(Start.class)
    void confirmStart();

    @Transition(Finish.class)
    void confirmFinish();

    @Transition(Cancel.class)
    void cancel();

    @StateIndicator
    String getServiceOrderState();
}
