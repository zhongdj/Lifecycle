/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.imadz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.LifecycleEvent;
import net.imadz.lifecycle.LifecycleEventHandler;
import net.imadz.lifecycle.LifecycleException;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.engine.LifecycleLockTestMetadata.CustomerObject;
import net.imadz.lifecycle.engine.LifecycleLockTestMetadata.CustomerStateMachine;
import net.imadz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.imadz.verification.VerificationException;

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
