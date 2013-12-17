package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.LifecycleEvent;
import net.madz.lifecycle.LifecycleEventHandler;
import net.madz.lifecycle.LifecycleException;
import net.madz.lifecycle.engine.LifecycleLockTestMetadata.CustomerObject;
import net.madz.lifecycle.engine.LifecycleLockTestMetadata.CustomerStateMachine;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LifecycleEventTests extends EngineTestBase {

    public static class TestLifecycleEventHandler implements LifecycleEventHandler {

        static List<LifecycleEvent> eventList = new ArrayList<>();

        @Override
        public void onEvent(LifecycleEvent event) {
            eventList.add(event);
        }
    }

    @Test
    public void should_fire_lifecycle_event_if_transition_method_invoked_after_lifecycleEventHandler_registered() throws VerificationException {
        @LifecycleRegistry({ CustomerObject.class, TestLifecycleEventHandler.class })
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
        assertEquals(0, TestLifecycleEventHandler.eventList.size());
        final CustomerObject customer = new CustomerObject();
        assertEquals(0, TestLifecycleEventHandler.eventList.size());
        customer.confirm();
        {
            assertEquals(1, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Draft.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class, CustomerStateMachine.Transitions.Confirm.class);
        }
        customer.suspend();
        {
            assertEquals(2, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceSuspended.class,
                    CustomerStateMachine.States.Confirmed.Transitions.Suspend.class);
        }
        customer.resume();
        {
            assertEquals(3, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceSuspended.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class, CustomerStateMachine.States.Confirmed.Transitions.Resume.class);
        }
        customer.terminateService();
        {
            assertEquals(4, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceExpired.class,
                    CustomerStateMachine.States.Confirmed.Transitions.TerminateService.class);
        }
        customer.renew();
        {
            assertEquals(5, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceExpired.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class, CustomerStateMachine.States.Confirmed.Transitions.Renew.class);
        }
        customer.terminateService();
        {
            assertEquals(6, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.InService.class,
                    CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceExpired.class,
                    CustomerStateMachine.States.Confirmed.Transitions.TerminateService.class);
        }
        customer.abandon();
        {
            assertEquals(7, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Confirmed.ConfirmedStates.ServiceExpired.class,
                    CustomerStateMachine.States.Recycled.class, CustomerStateMachine.States.Confirmed.Transitions.Abandon.class);
        }
        customer.putBack();
        {
            assertEquals(8, TestLifecycleEventHandler.eventList.size());
            LifecycleEvent lifecycleEvent = TestLifecycleEventHandler.eventList.get(TestLifecycleEventHandler.eventList.size() - 1);
            assertLifecycleEvent(customer, lifecycleEvent, CustomerStateMachine.States.Recycled.class, CustomerStateMachine.States.Draft.class,
                    CustomerStateMachine.Transitions.PutBack.class);
        }
        try {
            customer.renew();
        } catch (LifecycleException e) {
            assertEquals(8, TestLifecycleEventHandler.eventList.size());
        }
    }

    private void assertLifecycleEvent(CustomerObject reactiveObject, LifecycleEvent lifecycleEvent, Class<?> fromClass, Class<?> toClass, Class<?> transition) {
        assertEquals(reactiveObject, lifecycleEvent.getReactiveObject());
        assertEquals(fromClass.getSimpleName(), lifecycleEvent.fromState());
        assertEquals(toClass.getSimpleName(), lifecycleEvent.toState());
        lifecycleEvent.endTime();
        lifecycleEvent.startTime();
        assertEquals(transition.getSimpleName(), lifecycleEvent.transition());
        assertEquals(TransitionTypeEnum.Common, lifecycleEvent.transitionType());
    }
}
