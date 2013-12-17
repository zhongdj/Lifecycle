package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;

import org.junit.Test;

public class EngineCoreFunctionNegativeTests extends CoreFuntionTestMetadata {

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_valid_while_relation_object_not_in_expected_states() throws LifecycleException {
        Customer customer = new Customer();
        customer.activate();
        customer.cancel();
        assertEquals(CustomerLifecycleMeta.States.Canceled.class.getSimpleName(), customer.getState());
        InternetServiceOrder order = new InternetServiceOrder(new Date(), null, customer, "1 year");
        try {
            order.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, customer, order, CustomerLifecycleMeta.States.Active.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_inherited_valid_while_relation_object_not_in_expected_states() throws LifecycleException {
        final InternetTVServiceProvider provider = new InternetTVServiceProvider();
        assertEquals(InternetTVProviderLifecycle.States.ServiceAvailable.class.getSimpleName(), provider.getState());
        final Customer customer = new Customer();
        customer.activate();
        assertEquals(CustomerLifecycleMeta.States.Active.class.getSimpleName(), customer.getState());
        customer.cancel();
        assertEquals(CustomerLifecycleMeta.States.Canceled.class.getSimpleName(), customer.getState());
        final InternetTVService service = new InternetTVService(customer);
        service.setProvider(provider);
        try {
            service.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, customer, service, CustomerLifecycleMeta.States.Active.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_self_valid_while_relation_object_not_in_expected_states() throws LifecycleException {
        final InternetTVServiceProvider provider = new InternetTVServiceProvider();
        assertEquals(InternetTVProviderLifecycle.States.ServiceAvailable.class.getSimpleName(), provider.getState());
        provider.shutdown();
        assertEquals(InternetTVProviderLifecycle.States.Closed.class.getSimpleName(), provider.getState());
        final Customer customer = new Customer();
        customer.activate();
        assertEquals(CustomerLifecycleMeta.States.Active.class.getSimpleName(), customer.getState());
        final InternetTVService service = new InternetTVService(customer);
        service.setProvider(provider);
        try {
            service.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, provider, service, VOIPProviderLifecycleMeta.States.ServiceAvailable.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_overrides_inherited_valid_while_relation_and_validation_negative_with_self_valid_while() throws LifecycleException {
        final VOIPProvider provider = new VOIPProvider();
        final Customer customer = new Customer();
        assertEquals(CustomerLifecycleMeta.States.Draft.class.getSimpleName(), customer.getState());
        final VOIPService service = new VOIPService(customer);
        assertEquals(VOIPServiceLifecycleMeta.States.New.class.getSimpleName(), service.getState());
        provider.shutdown();
        assertEquals(VOIPProviderLifecycleMeta.States.Closed.class.getSimpleName(), provider.getState());
        service.setProvider(provider);
        try {
            service.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, provider, service, VOIPProviderLifecycleMeta.States.ServiceAvailable.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9002_if_violates_inbound_while_with_non_conditional_transition() {
        final Customer customer = new Customer();
        customer.activate();
        InternetServiceOrderWithInboundWhile service = new InternetServiceOrderWithInboundWhile(new Date(), null, customer, "3 years");
        customer.cancel();
        assertEquals(CustomerLifecycleMeta.States.Canceled.class.getSimpleName(), customer.getState());
        try {
            service.start();
        } catch (LifecycleException e) {
            assertViolateInboundWhileRelationConstraint(e, InternetServiceLifecycleMetaWithInboundWhile.Transitions.Start.class,
                    InternetServiceLifecycleMetaWithInboundWhile.States.InService.class, service, customer, CustomerLifecycleMeta.States.Active.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9002_if_violates_inbound_while_with_conditional_transition_prevalidate() {
        final PowerObject power = new PowerObject();
        final KeyBoardObjectPreValidateCondition keyboard = new KeyBoardObjectPreValidateCondition(power);
        power.shutDown();
        assertState(PowerLifecycleMetadata.States.PowerOff.class, power);
        try {
            keyboard.pressAnyKey();
        } catch (LifecycleException e) {
            assertViolateInboundWhileRelationConstraint(e, KeyBoardLifecycleMetadataPreValidateCondition.Transitions.PressAnyKey.class,
                    KeyBoardLifecycleMetadataPreValidateCondition.States.ReadingInput.class, keyboard, power, PowerLifecycleMetadata.States.PowerOn.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9002_if_violates_inbound_while_with_conditional_transition_postvalidate() {
        final PowerObject power = new PowerObject();
        final KeyBoardObjectPostValidateCondition keyboard = new KeyBoardObjectPostValidateCondition(power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPostValidateCondition.States.ReadingInput.class, keyboard);
        try {
            keyboard.pressAnyKey();
        } catch (LifecycleException e) {
            assertViolateInboundWhileRelationConstraint(e, KeyBoardLifecycleMetadataPostValidateCondition.Transitions.PressAnyKey.class,
                    KeyBoardLifecycleMetadataPostValidateCondition.States.Broken.class, keyboard, power, PowerLifecycleMetadata.States.PowerOn.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_violates_relations_concreted_on_fields_in_hierarachy_classes_negative1() {
        final Customer customer = new Customer();
        final InternetTVServiceProvider tvProvider = new InternetTVServiceProvider();
        final InternetTVServiceWithRelationOnFields tvService = new InternetTVServiceWithRelationOnFields(customer, tvProvider);
        try {
            tvService.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, customer, tvService, CustomerLifecycleMeta.States.Active.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_violates_relations_concreted_on_fields_in_hierarachy_classes_negative2() {
        final Customer customer = new Customer();
        customer.activate();
        final InternetTVServiceProvider tvProvider = new InternetTVServiceProvider();
        tvProvider.shutdown();
        final InternetTVServiceWithRelationOnFields tvService = new InternetTVServiceWithRelationOnFields(customer, tvProvider);
        try {
            tvService.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, tvProvider, tvService, InternetTVProviderLifecycle.States.ServiceAvailable.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9004_if_relation_object_null_but_validwhile_not_nullable_true() {
        MemberShip memberShip = null;
        OrderValidWhileNotNullable order = new OrderValidWhileNotNullable(memberShip);
        assertState(OrderValidWhileNotNullableLifecycleMeta.States.Draft.class, order);
        try {
            order.pay();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.VALID_WHILE_RELATION_TARGET_IS_NULL,
                    OrderValidWhileNotNullableLifecycleMeta.Relations.MemberShipRelation.class, "nullable = false",
                    OrderValidWhileNotNullableLifecycleMeta.States.Draft.class);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9005_if_relation_object_null_but_inboundwhile_not_nullable_true() {
        MemberShip memberShip = null;
        OrderInboundWhileNotNullable order = new OrderInboundWhileNotNullable(memberShip);
        assertState(OrderInboundWhileNotNullableLifecycleMeta.States.Draft.class, order);
        try {
            order.pay();
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.INBOUND_WHILE_RELATION_TARGET_IS_NULL,
                    OrderInboundWhileNotNullableLifecycleMeta.Relations.MemberShipRelation.class, "nullable = false",
                    OrderInboundWhileNotNullableLifecycleMeta.States.Draft.class);
        }
    }
}
