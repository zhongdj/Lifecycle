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

import java.util.Date;

import net.imadz.verification.VerificationException;

import org.junit.Test;

public class EngineCoreFunctionPositiveTests extends CoreFuntionTestMetadata {

    @Test
    public void should_support_standalone_object_without_relation_lifecycle() throws VerificationException {
        Customer customer = new Customer();
        customer.activate();
        assertEquals(CustomerLifecycleMeta.States.Active.class.getSimpleName(), customer.getState());
        customer.suspend();
        assertEquals(CustomerLifecycleMeta.States.Suspended.class.getSimpleName(), customer.getState());
        customer.resume();
        assertEquals(CustomerLifecycleMeta.States.Active.class.getSimpleName(), customer.getState());
        customer.cancel();
        assertEquals(CustomerLifecycleMeta.States.Canceled.class.getSimpleName(), customer.getState());
    }

    @Test
    public void should_support_standalone_object_with_definite_relation() {
        Customer customer = new Customer();
        customer.activate();
        final InternetServiceOrder order = new InternetServiceOrder(new Date(), null, customer, "1 year");
        order.start();
        assertEquals(InternetServiceLifecycleMeta.States.InService.class.getSimpleName(), order.getState());
    }

    @Test
    public void should_support_inherited_valid_while_relation_validation() {
        final InternetTVServiceProvider provider = new InternetTVServiceProvider();
        assertEquals(InternetTVProviderLifecycle.States.ServiceAvailable.class.getSimpleName(), provider.getState());
        Customer customer = new Customer();
        customer.activate();
        assertEquals(CustomerLifecycleMeta.States.Active.class.getSimpleName(), customer.getState());
        final InternetTVService service = new InternetTVService(customer);
        service.setProvider(provider);
        service.start();
        assertEquals(InternetServiceLifecycleMeta.States.InService.class.getSimpleName(), service.getState());
    }

    @Test
    public void should_support_overrides_inherited_valid_while_relation_validation_positive_with_super_valid_while() {
        final VOIPProvider provider = new VOIPProvider();
        final Customer customer = new Customer();
        assertEquals(CustomerLifecycleMeta.States.Draft.class.getSimpleName(), customer.getState());
        final VOIPService service = new VOIPService(customer);
        assertEquals(VOIPServiceLifecycleMeta.States.New.class.getSimpleName(), service.getState());
        service.setProvider(provider);
        service.start();
        assertEquals(VOIPServiceLifecycleMeta.States.InService.class.getSimpleName(), service.getState());
    }

    @Test
    public void should_support_inbound_while_with_non_conditional_event() {
        final Customer customer = new Customer();
        customer.activate();
        InternetServiceOrderWithInboundWhile service = new InternetServiceOrderWithInboundWhile(new Date(), null, customer, "3 years");
        service.start();
        assertState(InternetServiceLifecycleMetaWithInboundWhile.States.InService.class, service);
    }

    @Test
    public void should_support_inbound_while_with_conditional_event_prevalidate_inbound_while() {
        final PowerObject power = new PowerObject();
        final KeyBoardObjectPreValidateCondition keyboard = new KeyBoardObjectPreValidateCondition(power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPreValidateCondition.States.ReadingInput.class, keyboard);
        assertState(PowerLifecycleMetadata.States.PowerOn.class, power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPreValidateCondition.States.Broken.class, keyboard);
        assertState(PowerLifecycleMetadata.States.PowerOn.class, power);
    }

    @Test
    public void should_support_inbound_while_with_conditional_event_postvalidate_inbound_while() {
        final PowerObject power = new PowerObject();
        final KeyBoardObjectPostValidateCondition keyboard = new KeyBoardObjectPostValidateCondition(power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPostValidateCondition.States.ReadingInput.class, keyboard);
    }

    @Test
    public void should_support_inbound_while_with_non_getter_conditional_event_postvalidate_inbound_while() {
        final NonGetterConditionPowerObject power = new NonGetterConditionPowerObject();
        final NonGetterConditionKeyBoardObjectPostValidateCondition keyboard = new NonGetterConditionKeyBoardObjectPostValidateCondition(power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPostValidateCondition.States.ReadingInput.class, keyboard);
    }

    @Test
    public void should_support_relations_concreted_on_fields_in_hierarachy_classes() {
        final Customer customer = new Customer();
        customer.activate();
        final InternetTVServiceProvider tvProvider = new InternetTVServiceProvider();
        InternetTVServiceWithRelationOnFields tvService = new InternetTVServiceWithRelationOnFields(customer, tvProvider);
        tvService.start();
        assertState(InternetTVServiceLifecycle.States.InService.class, tvService);
    }

    @Test
    public void should_support_validwhile_nullable_true() {
        MemberShip memberShip = null;
        OrderValidWhileNullable order = new OrderValidWhileNullable(memberShip);
        assertState(OrderValidWhileNullableLifecycleMeta.States.Draft.class, order);
        order.pay();
        assertState(OrderValidWhileNullableLifecycleMeta.States.Paid.class, order);
    }

    @Test
    public void should_support_inboundwhile_nullable_true() {
        MemberShip memberShip = null;
        OrderInboundWhileNullable order = new OrderInboundWhileNullable(memberShip);
        assertState(OrderInboundWhileNullableLifecycleMeta.States.Draft.class, order);
        order.pay();
        assertState(OrderInboundWhileNullableLifecycleMeta.States.Paid.class, order);
    }
}
