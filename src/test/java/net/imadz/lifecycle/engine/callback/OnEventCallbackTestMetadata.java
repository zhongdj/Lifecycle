package net.imadz.lifecycle.engine.callback;

import org.junit.BeforeClass;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.callback.OnEvent;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.engine.EngineTestBase;
import net.imadz.lifecycle.engine.callback.OnEventCallbackTestMetadata.BasicEventCallbackUseCase.Events.*;
import net.imadz.verification.VerificationException;

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
			@Transition(event=Confirm.class, value = AwaitingPayment.class)
			static class Draft {}
			@Transition(event=PayOff.class, value = Completed.class)
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
		public void confirm() {}
		@Event
		public void payOff() {}
		
		@OnEvent(Confirm.class)
		public void expected_to_be_called_on_confirm_event(LifecycleContext<Order, String> context) {
			confirmEventCalled = true;
		}
		
		@OnEvent
		public void expected_to_be_called_on_all_event(LifecycleContext<Order, String> context) {
			totalCallbacks ++;
		}
		
		public boolean isConfirmEventCalled() {
			return confirmEventCalled;
		}
		public int getTotalCallbacks() {
			return totalCallbacks;
		}
		
		
	}
}
