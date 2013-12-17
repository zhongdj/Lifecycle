package net.madz.lifecycle.engine.callback;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CallbackExtendedAndOverrideTests extends CallbackTestMetadata {

    @Before
    public void base() {
        final OrderObject<?> order = new OrderObject<>();
        assertEquals(0, order.getCount());
        order.pay();
        assertEquals(1, order.getCount());
        order.deliver();
        assertEquals(2, order.getCount());
    }

    @Test
    public void should_increase_counter_if_transition_method_invoked_when_state_is_new_or_delivered() {
        final BigProductOrderObjectWithExtendsCallbackDefinition bigOrder = new BigProductOrderObjectWithExtendsCallbackDefinition();
        assertEquals(0, bigOrder.getCount());
        bigOrder.pay();
        assertEquals(1, bigOrder.getCount());
        bigOrder.deliver();
        assertEquals(2, bigOrder.getCount());
        bigOrder.install();
        assertEquals(3, bigOrder.getCount());
    }

    @Test
    public void should_not_increase_counter_if_next_state_is_delivered_when_callback_definition_overrided() {
        final BigProductOrderObjectWithOverridesCallbackDefinition bigOrder = new BigProductOrderObjectWithOverridesCallbackDefinition();
        assertEquals(0, bigOrder.getCount());
        bigOrder.pay();
        assertEquals(1, bigOrder.getCount());
        bigOrder.deliver();
        assertEquals(1, bigOrder.getCount());
        bigOrder.install();
        assertEquals(2, bigOrder.getCount());
    }

}
