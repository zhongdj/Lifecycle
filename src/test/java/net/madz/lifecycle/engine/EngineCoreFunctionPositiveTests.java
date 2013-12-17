package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.madz.verification.VerificationException;

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
    public void should_support_inbound_while_with_non_conditional_transition() {
        final Customer customer = new Customer();
        customer.activate();
        InternetServiceOrderWithInboundWhile service = new InternetServiceOrderWithInboundWhile(new Date(), null, customer, "3 years");
        service.start();
        assertState(InternetServiceLifecycleMetaWithInboundWhile.States.InService.class, service);
    }

    @Test
    public void should_support_inbound_while_with_conditional_transition_prevalidate_inbound_while() {
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
    public void should_support_inbound_while_with_conditional_transition_postvalidate_inbound_while() {
        final PowerObject power = new PowerObject();
        final KeyBoardObjectPostValidateCondition keyboard = new KeyBoardObjectPostValidateCondition(power);
        keyboard.pressAnyKey();
        assertState(KeyBoardLifecycleMetadataPostValidateCondition.States.ReadingInput.class, keyboard);
    }

    @Test
    public void should_support_inbound_while_with_non_getter_conditional_transition_postvalidate_inbound_while() {
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
