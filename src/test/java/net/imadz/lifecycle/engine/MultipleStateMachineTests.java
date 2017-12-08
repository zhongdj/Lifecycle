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
package net.imadz.lifecycle.engine;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.callback.PostStateChange;
import net.imadz.lifecycle.annotations.relation.Relation;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultipleStateMachineTests extends MultipleStateMachineTestMetadata {

  @BeforeClass
  public static void setup() throws Throwable {
    registerMetaFromClass(MultipleStateMachineTests.class);
  }

  public static interface Top {
    @PostStateChange
    void increment(LifecycleContext<Top, String> context);
  }

  @LifecycleMeta(PCPurchaseOrderStateMachine.class)
  public static interface PCPurchaseOrder extends Top {

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

  @LifecycleMeta(PCManufactoringOrderStateMachine.class)
  public static interface ManufactoringOrder extends Top {

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
  public static interface LogisticOrder extends Top {

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

    private String logisticOrderState = PCLogisticOrderStateMachine.States.Draft.class.getSimpleName();
    private String manufactoringOrderState = PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName();
    private String purchaseOrderState = PCPurchaseOrderStateMachine.States.Draft.class.getSimpleName();
    private int count = 0;

    @Override
    public String getLogisticOrderState() {
      return logisticOrderState;
    }

    @SuppressWarnings("unused")
    private void setLogisticOrderState(String state) {
      this.logisticOrderState = state;
    }

    @PostStateChange
    @Override
    public void increment(LifecycleContext<Top, String> context) {
      count = count + 1;
    }

    public int getCount() {
      return count;
    }

    @Override
    @Relation(PCLogisticOrderStateMachine.Relations.ManufactureOrder.class)
    public ManufactoringOrder getManufactoringOrder() {
      return this;
    }

    @Override
    @Event(PCLogisticOrderStateMachine.Events.Schedule.class)
    public void doSchedule() {
    }

    @Override
    @Event(PCLogisticOrderStateMachine.Events.DoPickup.class)
    public void doPickup() {
      getManufactoringOrder().doTransferToLogistics();
    }

    @Override
    @Event(PCLogisticOrderStateMachine.Events.StartTransport.class)
    public void doStartTransport() {
    }

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
    public void doPlan() {
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.ConfirmBOM.class)
    public void doConfirmBom() {
      getPurchaseOrder().doStart();
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.MakeOSROMComplete.class)
    public void doMakeOSRomComplete() {
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.StartAssembling.class)
    public void doStartAssembling() {
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.StartDebugging.class)
    public void doStartDebugging() {
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.PackageComplete.class)
    public void doConfirmPacakgeComplete() {
    }

    @Override
    @Event(PCManufactoringOrderStateMachine.Events.TransferToLogistics.class)
    public void doTransferToLogistics() {
    }

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
    public void doConfirmLogisticOrder() {
    }

    @Override
    @Event(PCPurchaseOrderStateMachine.Events.Start.class)
    public void doStart() {
    }

    @Override
    @Event(PCPurchaseOrderStateMachine.Events.Complete.class)
    public void doComplete() {
    }

    @Override
    @Event(PCPurchaseOrderStateMachine.Events.Abort.class)
    public void doAbort() {
    }

    @Override
    @Event(PCPurchaseOrderStateMachine.Events.Confirm.class)
    public void doConfirm() {
    }
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
      assertEquals(0, order.getCount());
    }
    purchaseOrder.doConfirm();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Confirmed.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Draft.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(1, order.getCount());
    }
    manufactoringOrder.doConfirmBom();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.BOMConfirmed.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(3, order.getCount());//Since doConfirmBom triggered another state change.
    }
    manufactoringOrder.doPlan();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Planned.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(4, order.getCount());
    }
    manufactoringOrder.doMakeOSRomComplete();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.OSROMReady.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(5, order.getCount());
    }
    manufactoringOrder.doStartAssembling();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.AssemblingOnProductLine.class.getSimpleName(), manufactoringOrder
          .getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(6, order.getCount());
    }
    manufactoringOrder.doStartDebugging();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.DebuggingOnProductLine.class.getSimpleName(), manufactoringOrder
          .getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(7, order.getCount());
    }
    manufactoringOrder.doConfirmPacakgeComplete();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Draft.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(8, order.getCount());
    }
    logisticOrder.doConfirmLogisticOrder();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Confirmed.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(9, order.getCount());
    }
    logisticOrder.doSchedule();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Packaged.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Scheduled.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(10, order.getCount());
    }
    logisticOrder.doPickup();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Picked.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(12, order.getCount()); //since do pickup triggered another state change
    }
    logisticOrder.doStartTransport();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Ongoing.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Transporting.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(13, order.getCount());
    }
    logisticOrder.doCustomerConfirmReceive();
    {
      assertEquals(PCPurchaseOrderStateMachine.States.Completed.class.getSimpleName(), purchaseOrder.getPurchaseOrderState());
      assertEquals(PCManufactoringOrderStateMachine.States.Done.class.getSimpleName(), manufactoringOrder.getManufactoringOrderState());
      assertEquals(PCLogisticOrderStateMachine.States.Received.class.getSimpleName(), logisticOrder.getLogisticOrderState());
      assertEquals(15, order.getCount()); //since do customer confirm receive triggered another state change
    }

  }
}
