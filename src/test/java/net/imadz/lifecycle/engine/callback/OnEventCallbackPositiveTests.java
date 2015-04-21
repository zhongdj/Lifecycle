package net.imadz.lifecycle.engine.callback;

import org.junit.Test;

import static org.junit.Assert.*;

public class OnEventCallbackPositiveTests extends OnEventCallbackTestMetadata {

	@Test
	public void it_should_invoke_on_event_call_back() {
		Order o = new Order();
		assertEquals(0,  o.getTotalCallbacks());
		assertFalse(o.isConfirmEventCalled());
		o.confirm();
		assertEquals(1,  o.getTotalCallbacks());
		assertTrue(o.isConfirmEventCalled());
		o.payOff();
		assertEquals(2,  o.getTotalCallbacks());
	}
}
