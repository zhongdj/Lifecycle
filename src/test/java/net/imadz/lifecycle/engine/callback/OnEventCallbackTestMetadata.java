package net.imadz.lifecycle.engine.callback;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.callback.OnEvent;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.engine.EngineTestBase;
import net.imadz.lifecycle.engine.callback.OnEventCallbackTestMetadata.BasicEventCallbackUseCase.Events.Confirm;
import net.imadz.lifecycle.engine.callback.OnEventCallbackTestMetadata.BasicEventCallbackUseCase.Events.PayOff;
import net.imadz.verification.VerificationException;
import org.junit.BeforeClass;

public class OnEventCallbackTestMetadata extends EngineTestBase {

  @BeforeClass
  public static void setup() throws VerificationException {
    registerMetaFromClass(OnEventCallbackTestMetadata.class);
  }

  @StateMachine
  public static interface BasicEventCallbackUseCase {

    @StateSet
    static interface States {
      @Initial
      @Transition(event = Confirm.class, value = AwaitingPayment.class)
      static class Draft {}

      @Transition(event = PayOff.class, value = Completed.class)
      static class AwaitingPayment {}

      @Final
      static class Completed {}
    }

    @EventSet
    static interface Events {
      static class Confirm {}

      static class PayOff {}
    }
  }

  @LifecycleMeta(BasicEventCallbackUseCase.class)
  public static class Order {
    @StateIndicator
    private String state = "Draft";
    private boolean confirmEventCalled = false;
    private int totalCallbacks = 0;

    @Event
    public void confirm() {
    }

    @Event
    public void payOff() {
    }

    @OnEvent(Confirm.class)
    public void expected_to_be_called_on_confirm_event(LifecycleContext<Order, String> context) {
      confirmEventCalled = true;
    }

    @OnEvent
    public void expected_to_be_called_on_all_event(LifecycleContext<Order, String> context) {
      totalCallbacks++;
    }

    public boolean isConfirmEventCalled() {
      return confirmEventCalled;
    }

    public int getTotalCallbacks() {
      return totalCallbacks;
    }

    private int relatedCallback = 0;

    @OnEvent(observableClass = ServiceOrder.class, mappedBy = "order")
    public void expected_to_be_Called_on_relation_event_callback(LifecycleContext<ServiceOrder, String> context) {
      final ServiceOrder so = context.getTarget();
      so.getOrder();
      relatedCallback++;
    }

    public int getRelatedCallback() {
      return relatedCallback;
    }


  }

  @StateMachine
  public static interface RelationalEventCallbackUseCase {

    @StateSet
    static interface States {
      @Initial
      @Transition(event = Events.ConfirmWork.class, value = AwaitingDeploying.class)
      static class Pending {}

      @Transition(event = Events.Complete.class, value = Completed.class)
      @InboundWhile(on = {BasicEventCallbackUseCase.States.AwaitingPayment.class,
          BasicEventCallbackUseCase.States.Completed.class}, relation = Relations.OrderRelation.class)
      static class AwaitingDeploying {}

      @Final
      static class Completed {}
    }

    @EventSet
    static interface Events {
      static class ConfirmWork {}

      static class Complete {}
    }

    @RelationSet
    static interface Relations {
      @RelateTo(BasicEventCallbackUseCase.class)
      static class OrderRelation {}
    }
  }

  @LifecycleMeta(RelationalEventCallbackUseCase.class)
  public static class ServiceOrder {
    private final Order order;

    @StateIndicator
    private String state = "Pending";

    public ServiceOrder(Order order) {
      super();
      this.order = order;
    }

    @Relation(RelationalEventCallbackUseCase.Relations.OrderRelation.class)
    public Order getOrder() {
      return order;
    }

    @Event
    public void confirmWork() {
    }

    @Event
    public void complete() {
    }

  }
}
