package net.imadz.lifecycle.engine.callback.priority;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.callback.PostStateChange;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.engine.EngineTestBase;
import net.imadz.lifecycle.engine.callback.priority.PriorityTestMetadata.CallbackStateMachine.States.New;
import net.imadz.lifecycle.engine.callback.priority.PriorityTestMetadata.CallbackStateMachine.States.Started;
import net.imadz.verification.VerificationException;

import org.junit.BeforeClass;

public class PriorityTestMetadata extends EngineTestBase {
	@BeforeClass
	public static void setup() throws VerificationException {
		registerMetaFromClass(PriorityTestMetadata.class);
	}

	@StateMachine
	public static interface CallbackStateMachine {

		@StateSet
		static interface States {

			@Initial
			@Transition(event = Events.Start.class, value = { States.Started.class })
			static interface New {
			}

			@Transition(event = Events.Finish.class, value = { States.Finished.class })
			static interface Started {
			}

			@Final
			static interface Finished {
			}
		}

		@EventSet
		static interface Events {

			static interface Start {
			}

			static interface Finish {
			}
		}
	}

	@LifecycleMeta(CallbackStateMachine.class)
	public static class CallbackObjectBase extends ReactiveObject {

		public CallbackObjectBase() {
			initialState(CallbackStateMachine.States.New.class.getSimpleName());
		}

		protected String callbackInvokeAcc = "";

		@Event
		public void start() {
		}

		@Event
		public void finish() {
		}

		public String getCallbackInvokeCounter() {
			return this.callbackInvokeAcc;
		}
	}

	@LifecycleMeta(CallbackStateMachine.class)
	public static class InvokeByPriorityWithoutHierarchy extends
			CallbackObjectBase {

		@PostStateChange(priority = 1)
		public void interceptPostStateChange(
				LifecycleContext<InvokeByPriorityWithoutHierarchy, String> context) {
			this.callbackInvokeAcc +="1," ;
		}

		@PostStateChange(priority = 2, from = New.class)
		public void interceptPostStateChange2(
				LifecycleContext<InvokeByPriorityWithoutHierarchy, String> context) {
			this.callbackInvokeAcc += "2,";
		}

		@PostStateChange(priority = 3, to = Started.class)
		public void interceptPostStateChange3(
				LifecycleContext<InvokeByPriorityWithoutHierarchy, String> context) {
			this.callbackInvokeAcc += "3,";
		}
		@PostStateChange(priority = 4, from=New.class, to = Started.class)
		public void interceptPostStateChange4(
				LifecycleContext<InvokeByPriorityWithoutHierarchy, String> context) {
			this.callbackInvokeAcc += "4,";
		}
	}
	
	@LifecycleMeta(CallbackStateMachine.class)
	public static class GrandFather extends CallbackObjectBase {
		@PostStateChange(priority = 1)
		public void interceptPostStateChange(
				LifecycleContext<GrandFather, String> context) {
			this.callbackInvokeAcc += "GrandFather,";
		}
	}
	
	@LifecycleMeta(CallbackStateMachine.class)
	public static class Father extends GrandFather {
		@PostStateChange(priority = 1)
		public void interceptPostStateChange2(
				LifecycleContext<Father, String> context) {
			this.callbackInvokeAcc += "Father,";
		}
	}
	
	@LifecycleMeta(CallbackStateMachine.class)
	public static class Son extends Father {
		@PostStateChange(priority = 1)
		public void interceptPostStateChange3(
				LifecycleContext<Son, String> context) {
			this.callbackInvokeAcc += "Son,";
		}
	}
	
	@LifecycleMeta(CallbackStateMachine.class)
	public static class InvokeByGeneralizeDimensionWhenPrioritySame extends
			CallbackObjectBase {

		@PostStateChange(priority = 1)
		public void interceptPostStateChange(
				LifecycleContext<InvokeByGeneralizeDimensionWhenPrioritySame, String> context) {
			this.callbackInvokeAcc += "common,";
		}

		@PostStateChange(priority = 1, from = New.class)
		public void interceptPostStateChange2(
				LifecycleContext<InvokeByGeneralizeDimensionWhenPrioritySame, String> context) {
			this.callbackInvokeAcc += "from,";
		}

		@PostStateChange(priority = 1, to = Started.class)
		public void interceptPostStateChange3(
				LifecycleContext<InvokeByGeneralizeDimensionWhenPrioritySame, String> context) {
			this.callbackInvokeAcc += "to,";
		}
		@PostStateChange(priority = 1, from=New.class, to = Started.class)
		public void interceptPostStateChange4(
				LifecycleContext<InvokeByGeneralizeDimensionWhenPrioritySame, String> context) {
			this.callbackInvokeAcc += "specific,";
		}
	}

}
