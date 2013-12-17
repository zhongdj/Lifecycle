package net.madz.lifecycle.engine.callback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PreStateChangePositiveTests extends CallbackTestMetadata {

    @Test
    public void should_increase_counter_if_any_transition_method_invoked() {
        final PreCallbackFromAnyToAny o = new PreCallbackFromAnyToAny();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(1, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(2, o.getCallbackInvokeCounter());
    }

    @Test
    public void should_increase_counter_if_transition_method_invoked_when_state_is_started() {
        final PreCallbackFromStartToAny o = new PreCallbackFromStartToAny();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(1, o.getCallbackInvokeCounter());
    }

    @Test
    public void should_increase_counter_if_transition_method_invoked_when_next_state_is_started() {
        final PreCallbackFromAnyToStart o = new PreCallbackFromAnyToStart();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(1, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(1, o.getCallbackInvokeCounter());
    }

    @Test
    public void should_increase_counter_if_transition_method_invoked_when_from_and_to_state_matched() {
        final OrderWithSpecifiedPreFromToCallback bigOrder = new OrderWithSpecifiedPreFromToCallback();
        assertEquals(0, bigOrder.getCount());
        bigOrder.pay();
        assertEquals(1, bigOrder.getCount());
        bigOrder.deliver();
        assertEquals(2, bigOrder.getCount());
        bigOrder.install();
        assertEquals(3, bigOrder.getCount());
    }
}
