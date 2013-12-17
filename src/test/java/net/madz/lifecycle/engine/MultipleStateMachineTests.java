/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.relation.Relation;

import org.junit.BeforeClass;
import org.junit.Test;

public class MultipleStateMachineTests extends MultipleStateMachineTestMetadata {

    @BeforeClass
    public static void setup() throws Throwable {
        registerMetaFromClass(MultipleStateMachineTests.class);
    }

    @LifecycleMeta(PCPurchaseOrderStateMachine.class)
    public static interface PCPurchaseOrder {

        @StateIndicator
        String getPurchaseOrderState();

        @Transition(PCPurchaseOrderStateMachine.Transitions.Confirm.class)
        void doConfirm();

        @Transition(PCPurchaseOrderStateMachine.Transitions.Start.class)
        void doStart();

        @Transition(PCPurchaseOrderStateMachine.Transitions.Complete.class)
        void doComplete();

        @Transition(PCPurchaseOrderStateMachine.Transitions.Abort.class)
        void doAbort();
    }
    @LifecycleMeta(PCManufactoringOrderStateMachine.class)
    public static interface ManufactoringOrder {

        @StateIndicator
        String getManufactoringOrderState();

        @Relation(PCManufactoringOrderStateMachine.Relations.PurchaseOrder.class)
        PCPurchaseOrder getPurchaseOrder();

        @Transition(PCManufactoringOrderStateMachine.Transitions.Plan.class)
        void doPlan();

        @Transition(PCManufactoringOrderStateMachine.Transitions.ConfirmBOM.class)
        void doConfirmBom();

        @Transition(PCManufactoringOrderStateMachine.Transitions.MakeOSROMComplete.class)
        void doMakeOSRomComplete();

        @Transition(PCManufactoringOrderStateMachine.Transitions.StartAssembling.class)
        void doStartAssembling();

        @Transition(PCManufactoringOrderStateMachine.Transitions.StartDebugging.class)
        void doStartDebugging();

        @Transition(PCManufactoringOrderStateMachine.Transitions.PackageComplete.class)
        void doConfirmPacakgeComplete();

        @Transition(PCManufactoringOrderStateMachine.Transitions.TransferToLogistics.class)
        void doTransferToLogistics();
    }
    @LifecycleMeta(PCLogisticOrderStateMachine.class)
    public static interface LogisticOrder {

        @StateIndicator
        String getLogisticOrderState();

        @Relation(PCLogisticOrderStateMachine.Relations.PurchaseOrder.class)
        PCPurchaseOrder getPurchaseOrder();

        @Relation(PCLogisticOrderStateMachine.Relations.ManufactureOrder.class)
        ManufactoringOrder getManufactoringOrder();

        @Transition(PCLogisticOrderStateMachine.Transitions.Confirm.class)
        void doConfirmLogisticOrder();

        @Transition(PCLogisticOrderStateMachine.Transitions.Schedule.class)
        void doSchedule();

        @Transition(PCLogisticOrderStateMachine.Transitions.DoPickup.class)
        void doPickup();

        @Transition(PCLogisticOrderStateMachine.Transitions.StartTransport.class)
        void doStartTransport();

        @Transition(PCLogisticOrderStateMachine.Transitions.CustomerConfirmReceive.class)
        void doCustomerConfirmReceive();
    }
    @net.madz.lifecycle.annotations.ReactiveObject
    public static class PurchaseOrderImpl implements PCPurchaseOrder, ManufactoringOrder, LogisticOrder {

        private String logisticOrderState = PCLogisticOrderStateMachine.States.Draft.class.getSimpleName();
        private String manufactoringOrderState = PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName();
        private String purchaseOrderState = PCPurchaseOrderStateMachine.States.Draft.class.getSimpleName();

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
        @Transition(PCLogisticOrderStateMachine.Transitions.Schedule.class)
        public void doSchedule() {}

        @Override
        @Transition(PCLogisticOrderStateMachine.Transitions.DoPickup.class)
        public void doPickup() {
            getManufactoringOrder().doTransferToLogistics();
        }

        @Override
        @Transition(PCLogisticOrderStateMachine.Transitions.StartTransport.class)
        public void doStartTransport() {}

        @Override
        @Transition(PCLogisticOrderStateMachine.Transitions.CustomerConfirmReceive.class)
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
        @Transition(PCManufactoringOrderStateMachine.Transitions.Plan.class)
        public void doPlan() {}

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.ConfirmBOM.class)
        public void doConfirmBom() {
            getPurchaseOrder().doStart();
        }

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.MakeOSROMComplete.class)
        public void doMakeOSRomComplete() {}

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.StartAssembling.class)
        public void doStartAssembling() {}

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.StartDebugging.class)
        public void doStartDebugging() {}

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.PackageComplete.class)
        public void doConfirmPacakgeComplete() {}

        @Override
        @Transition(PCManufactoringOrderStateMachine.Transitions.TransferToLogistics.class)
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
        @Transition(PCLogisticOrderStateMachine.Transitions.Confirm.class)
        public void doConfirmLogisticOrder() {}

        @Override
        @Transition(PCPurchaseOrderStateMachine.Transitions.Start.class)
        public void doStart() {}

        @Override
        @Transition(PCPurchaseOrderStateMachine.Transitions.Complete.class)
        public void doComplete() {}

        @Override
        @Transition(PCPurchaseOrderStateMachine.Transitions.Abort.class)
        public void doAbort() {}

        @Override
        @Transition(PCPurchaseOrderStateMachine.Transitions.Confirm.class)
        public void doConfirm() {}
    }

    @Test
    public void should_support_one_business_implementation_with_multiple_state_machines() {
        final PurchaseOrderImpl order = new PurchaseOrderImpl();
        // tricks
        final PCPurchaseOrder purchaseOrder = order;
        final ManufactoringOrder manufactoringOrder = order;
        final LogisticOrder logisticOrder = order;
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Draft.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        purchaseOrder.doConfirm();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Confirmed.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doConfirmBom();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.BOMConfirmed.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doPlan();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Planned.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doMakeOSRomComplete();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.OSROMReady.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doStartAssembling();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.AssemblingOnProductLine.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doStartDebugging();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.DebuggingOnProductLine.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        manufactoringOrder.doConfirmPacakgeComplete();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        logisticOrder.doConfirmLogisticOrder();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Confirmed.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        logisticOrder.doSchedule();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Scheduled.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        logisticOrder.doPickup();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Picked.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        logisticOrder.doStartTransport();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Transporting.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
        logisticOrder.doCustomerConfirmReceive();
        {
            assertEquals(PCPurchaseOrderStateMachine.States.Completed.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
            assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
            assertEquals(PCLogisticOrderStateMachine.States.Received.class.getSimpleName(), logisticOrder.getLogisticOrderState());
        }
    }
}
