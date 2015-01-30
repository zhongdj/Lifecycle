package net.imadz.lifecycle.engine;

import static org.junit.Assert.assertEquals;
import net.imadz.bcel.intercept.DefaultStateMachineRegistry;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.relation.Relation;

import org.junit.BeforeClass;
import org.junit.Test;

public class SetInitialStatesTests extends MultipleStateMachineTestMetadata{
	@BeforeClass
    public static void setup() throws Throwable {
        registerMetaFromClass(SetInitialStatesTests.class);
    }

    @LifecycleMeta(PCPurchaseOrderStateMachine.class)
    public static interface PCPurchaseOrder {
    	
        @StateIndicator
        String getPurchaseOrderState();

        @Event(PCPurchaseOrderStateMachine.Events.Confirm.class)
        void doConfirm();

        @Event(PCPurchaseOrderStateMachine.Events.Start.class)
        void doStart();

        @Event(PCPurchaseOrderStateMachine.Events.Complete.class)
        void doComplete();

        @Event(PCPurchaseOrderStateMachine.Events.Abort.class)
        void doAbort();
    }
    
    public static class PCPurchaseOrderImpl implements PCPurchaseOrder {

    	private String pos;
    	
    	public PCPurchaseOrderImpl() {
    		DefaultStateMachineRegistry.getInstance().setInitialState(this);
    	}
		@Override
		public String getPurchaseOrderState() {
			return this.pos;
		}

		@Override
		public void doConfirm() {
			
		}

		@Override
		public void doStart() {
			
		}

		@Override
		public void doComplete() {
			
		}

		@Override
		public void doAbort() {
			
		}
    	
		@SuppressWarnings("unused")
		private void setPurchaseOrderState(String state) {
		    this.pos = state;
		}
    }
    @LifecycleMeta(PCManufactoringOrderStateMachine.class)
    public static interface ManufactoringOrder {

        @StateIndicator
        String getManufactoringOrderState();

        @Relation(PCManufactoringOrderStateMachine.Relations.PurchaseOrder.class)
        PCPurchaseOrder getPurchaseOrder();

        @Event(PCManufactoringOrderStateMachine.Events.Plan.class)
        void doPlan();

        @Event(PCManufactoringOrderStateMachine.Events.ConfirmBOM.class)
        void doConfirmBom();

        @Event(PCManufactoringOrderStateMachine.Events.MakeOSROMComplete.class)
        void doMakeOSRomComplete();

        @Event(PCManufactoringOrderStateMachine.Events.StartAssembling.class)
        void doStartAssembling();

        @Event(PCManufactoringOrderStateMachine.Events.StartDebugging.class)
        void doStartDebugging();

        @Event(PCManufactoringOrderStateMachine.Events.PackageComplete.class)
        void doConfirmPacakgeComplete();

        @Event(PCManufactoringOrderStateMachine.Events.TransferToLogistics.class)
        void doTransferToLogistics();
    }
    @LifecycleMeta(PCLogisticOrderStateMachine.class)
    public static interface LogisticOrder{

        @StateIndicator
        String getLogisticOrderState();

        @Relation(PCLogisticOrderStateMachine.Relations.PurchaseOrder.class)
        PCPurchaseOrder getPurchaseOrder();

        @Relation(PCLogisticOrderStateMachine.Relations.ManufactureOrder.class)
        ManufactoringOrder getManufactoringOrder();

        @Event(PCLogisticOrderStateMachine.Events.Confirm.class)
        void doConfirmLogisticOrder();

        @Event(PCLogisticOrderStateMachine.Events.Schedule.class)
        void doSchedule();

        @Event(PCLogisticOrderStateMachine.Events.DoPickup.class)
        void doPickup();

        @Event(PCLogisticOrderStateMachine.Events.StartTransport.class)
        void doStartTransport();

        @Event(PCLogisticOrderStateMachine.Events.CustomerConfirmReceive.class)
        void doCustomerConfirmReceive();
    }
    @net.imadz.lifecycle.annotations.ReactiveObject
    public static class PurchaseOrderImpl implements PCPurchaseOrder, ManufactoringOrder, LogisticOrder {

        private String logisticOrderState;
        private String manufactoringOrderState;
        private String purchaseOrderState;
        
        public PurchaseOrderImpl() {
        	DefaultStateMachineRegistry.getInstance().setInitialState(this);
        }
        @Override
        public String getLogisticOrderState() {
            return logisticOrderState;
        }

        @SuppressWarnings("unused")
        private void setLogisticOrderState(String state) {
            this.logisticOrderState = state;
        }

        @Override
        @Relation(PCLogisticOrderStateMachine.Relations.ManufactureOrder.class)
        public ManufactoringOrder getManufactoringOrder() {
            return this;
        }

        @Override
        @Event(PCLogisticOrderStateMachine.Events.Schedule.class)
        public void doSchedule() {}

        @Override
        @Event(PCLogisticOrderStateMachine.Events.DoPickup.class)
        public void doPickup() {
            getManufactoringOrder().doTransferToLogistics();
        }

        @Override
        @Event(PCLogisticOrderStateMachine.Events.StartTransport.class)
        public void doStartTransport() {}

        @Override
        @Event(PCLogisticOrderStateMachine.Events.CustomerConfirmReceive.class)
        public void doCustomerConfirmReceive() {
            getPurchaseOrder().doComplete();
        }

        @Override
        public String getManufactoringOrderState() {
            return manufactoringOrderState;
        }

        @SuppressWarnings("unused")
        private void setManufactoringOrderState(String state) {
            this.manufactoringOrderState = state;
        }

        @Override
        public PCPurchaseOrder getPurchaseOrder() {
            return this;
        }

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.Plan.class)
        public void doPlan() {}

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.ConfirmBOM.class)
        public void doConfirmBom() {
            getPurchaseOrder().doStart();
        }

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.MakeOSROMComplete.class)
        public void doMakeOSRomComplete() {}

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.StartAssembling.class)
        public void doStartAssembling() {}

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.StartDebugging.class)
        public void doStartDebugging() {}

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.PackageComplete.class)
        public void doConfirmPacakgeComplete() {}

        @Override
        @Event(PCManufactoringOrderStateMachine.Events.TransferToLogistics.class)
        public void doTransferToLogistics() {}

        @Override
        public String getPurchaseOrderState() {
            return purchaseOrderState;
        }

        @SuppressWarnings("unused")
        private void setPurchaseOrderState(String state) {
            this.purchaseOrderState = state;
        }

        @Override
        @Event(PCLogisticOrderStateMachine.Events.Confirm.class)
        public void doConfirmLogisticOrder() {}

        @Override
        @Event(PCPurchaseOrderStateMachine.Events.Start.class)
        public void doStart() {}

        @Override
        @Event(PCPurchaseOrderStateMachine.Events.Complete.class)
        public void doComplete() {}

        @Override
        @Event(PCPurchaseOrderStateMachine.Events.Abort.class)
        public void doAbort() {}

        @Override
        @Event(PCPurchaseOrderStateMachine.Events.Confirm.class)
        public void doConfirm() {}
    }

	@Test
	public void testClassImplementsMultipleStateMachines() {
		final PurchaseOrderImpl order = new PurchaseOrderImpl();
        assertEquals(PCPurchaseOrderStateMachine.States.Draft.class.getSimpleName(), order.getPurchaseOrderState());
        assertEquals(PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName(), order.getManufactoringOrderState());
        assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), order.getLogisticOrderState());
	}
	
	@Test
	public void testSimpleStateMachine() {
		final PCPurchaseOrderImpl pcorder = new PCPurchaseOrderImpl();
		assertEquals(PCPurchaseOrderStateMachine.States.Draft.class.getSimpleName(), pcorder.getPurchaseOrderState());
	}

}
