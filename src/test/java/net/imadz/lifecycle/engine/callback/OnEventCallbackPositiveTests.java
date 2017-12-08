package net.imadz.lifecycle.engine.callback;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OnEventCallbackPositiveTests extends OnEventCallbackTestMetadata {

  @Test
  public void it_should_invoke_on_event_call_back() {
    Order o = new Order();
    assertEquals(0, o.getTotalCallbacks());
    assertFalse(o.isConfirmEventCalled());
    o.confirm();
    assertEquals(1, o.getTotalCallbacks());
    assertTrue(o.isConfirmEventCalled());
    o.payOff();
    assertEquals(2, o.getTotalCallbacks());
  }

  @Test
  public void it_should_invoke_relational_on_event_call_back() {
    final Order o = new Order();
    final ServiceOrder so = new ServiceOrder(o);
    o.confirm();
    o.payOff();
    assertEquals(0, o.getRelatedCallback());
    so.confirmWork();
    so.complete();
    assertEquals(2, o.getRelatedCallback());
  }
}
