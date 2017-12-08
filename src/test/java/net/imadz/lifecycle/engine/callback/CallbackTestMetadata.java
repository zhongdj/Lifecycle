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
package net.imadz.lifecycle.engine.callback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.Transitions;
import net.imadz.lifecycle.annotations.action.Condition;
import net.imadz.lifecycle.annotations.action.ConditionSet;
import net.imadz.lifecycle.annotations.action.Conditional;
import net.imadz.lifecycle.annotations.action.ConditionalEvent;
import net.imadz.lifecycle.annotations.callback.PostStateChange;
import net.imadz.lifecycle.annotations.callback.PreStateChange;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.engine.EngineTestBase;
import net.imadz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta.Conditions;
import net.imadz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta.Conditions.Payable;
import net.imadz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta.States.PaidOff;
import net.imadz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta.States.PartialPaid;
import net.imadz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta.Utilities.PayableJudger;
import net.imadz.verification.VerificationException;

import org.junit.Assert;
import org.junit.BeforeClass;

public class CallbackTestMetadata extends EngineTestBase {

    @BeforeClass
    public static void setup() throws VerificationException {
        registerMetaFromClass(CallbackTestMetadata.class);
    }

    @StateMachine
    static interface CallbackStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = Events.Start.class, value = { States.Started.class })
            static interface New {}
            @Transition(event = Events.Finish.class, value = { States.Finished.class })
            static interface Started {}
            @Final
            static interface Finished {}
        }
        @EventSet
        static interface Events {

            static interface Start {}
            static interface Finish {}
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class CallbackObjectBase extends ReactiveObject {

        public CallbackObjectBase() {
            initialState(CallbackStateMachine.States.New.class.getSimpleName());
        }

        protected int callbackInvokeCounter = 0;

        @Event
        public void start() {}

        @Event
        public void finish() {}

        public int getCallbackInvokeCounter() {
            return this.callbackInvokeCounter;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PreCallbackFromAnyToAny extends CallbackObjectBase {

        @PreStateChange
        public void interceptPreStateChange(LifecycleContext<PreCallbackFromAnyToAny, String> context) {
            this.callbackInvokeCounter++;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PreCallbackFromStartToAny extends CallbackObjectBase {

        @PreStateChange(from = CallbackStateMachine.States.Started.class)
        public void interceptPreStateChange(LifecycleContext<PreCallbackFromStartToAny, String> context) {
            this.callbackInvokeCounter++;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PreCallbackFromAnyToStart extends CallbackObjectBase {

        @PreStateChange(to = CallbackStateMachine.States.Started.class)
        public void interceptPreStateChange(LifecycleContext<PreCallbackFromAnyToStart, String> context) {
            this.callbackInvokeCounter++;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PostCallbackFromAnyToAny extends CallbackObjectBase {

        @PostStateChange
        public void interceptPostStateChange(LifecycleContext<PostCallbackFromAnyToAny, String> context) {
            this.callbackInvokeCounter++;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PostCallbackFromAnyToStart extends CallbackObjectBase {

        @PostStateChange(to = CallbackStateMachine.States.Started.class)
        public void interceptPostStateChange(LifecycleContext<PostCallbackFromAnyToStart, String> context) {
            this.callbackInvokeCounter++;
        }
    }
    @LifecycleMeta(CallbackStateMachine.class)
    public static class PostCallbackFromStartToAny extends CallbackObjectBase {

        @PostStateChange(from = CallbackStateMachine.States.Started.class)
        public void interceptPostStateChange(LifecycleContext<PostCallbackFromStartToAny, String> context) {
            this.callbackInvokeCounter++;
        }
    }

    @LifecycleMeta(CallbackStateMachine.class)
    public static class PostCallbackFromStartToAnySubClass extends PostCallbackFromStartToAny {

        @PostStateChange(from = CallbackStateMachine.States.Started.class)
        @Override
        public void interceptPostStateChange(LifecycleContext<PostCallbackFromStartToAny, String> context) {
            this.callbackInvokeCounter++;
        }
    }

    @StateMachine
    public static interface InvoiceStateMachineMeta {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = InvoiceStateMachineMeta.Events.Post.class, value = { InvoiceStateMachineMeta.States.Posted.class })
            static interface Draft {}
            @Transitions({ @Transition(event = InvoiceStateMachineMeta.Events.Pay.class, value = { States.PartialPaid.class,
                    InvoiceStateMachineMeta.States.PaidOff.class }) })
            static interface Posted {}
            @Transition(event = InvoiceStateMachineMeta.Events.Pay.class, value = { States.PartialPaid.class,
                    InvoiceStateMachineMeta.States.PaidOff.class })
            static interface PartialPaid {}
            @Final
            static interface PaidOff {}
        }
        @EventSet
        static interface Events {

            static interface Post {}
            @Conditional(condition = Payable.class, judger = PayableJudger.class, postEval = true)
            static interface Pay {}
        }
        @ConditionSet
        static interface Conditions {

            static interface Payable {

                BigDecimal getTotalAmount();

                BigDecimal getPayedAmount();
            }
        }
        public static class Utilities {

            public static class PayableJudger implements ConditionalEvent<Payable> {

                @Override
                public Class<?> doConditionJudge(Payable t) {
                    if ( 0 < t.getPayedAmount().compareTo(BigDecimal.ZERO) && 0 < t.getTotalAmount().compareTo(t.getPayedAmount()) ) {
                        return PartialPaid.class;
                    } else if ( 0 >= t.getTotalAmount().compareTo(t.getPayedAmount()) ) {
                        return PaidOff.class;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
    }
    @StateMachine
    public static interface InvoiceItemStateMachineMeta {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = InvoiceItemStateMachineMeta.Events.Pay.class, value = { InvoiceItemStateMachineMeta.States.Paid.class })
            static interface Unpaid {}
            @Final
            @InboundWhile(on = { InvoiceStateMachineMeta.States.Posted.class, InvoiceStateMachineMeta.States.PartialPaid.class },
                    relation = InvoiceItemStateMachineMeta.Relations.ParentInvoice.class)
            static interface Paid {}
        }
        @EventSet
        static interface Events {

            static interface Pay {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(InvoiceStateMachineMeta.class)
            static interface ParentInvoice {}
        }
    }
    @LifecycleMeta(InvoiceStateMachineMeta.class)
    public static class Invoice extends ReactiveObject implements InvoiceStateMachineMeta.Conditions.Payable {

        private final BigDecimal totalAmount;;
        private BigDecimal payedAmount = new BigDecimal(0D);
        private final List<InvoiceItem> items = new ArrayList<InvoiceItem>();

        public Invoice(final BigDecimal totalAmount) {
            initialState(InvoiceStateMachineMeta.States.Draft.class.getSimpleName());
            this.totalAmount = totalAmount;
        }

        @Condition(InvoiceStateMachineMeta.Conditions.Payable.class)
        public InvoiceStateMachineMeta.Conditions.Payable getPayable() {
            return this;
        }

        @Override
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        @Override
        public synchronized BigDecimal getPayedAmount() {
            return payedAmount;
        }

        @Event
        public void post() {}

        @Event(InvoiceStateMachineMeta.Events.Pay.class)
        @PostStateChange(to = InvoiceItemStateMachineMeta.States.Paid.class, observableName = "items", mappedBy = "parent")
        public synchronized void onItemPaied(LifecycleContext<InvoiceItem, String> context) {
            payedAmount = payedAmount.add(context.getTarget().getPayedAmount());
        }

        public void addItem(InvoiceItem invoiceItem) {
            if ( !items.contains(invoiceItem) ) {
                items.add(invoiceItem);
            }
        }

        public List<InvoiceItem> getItems() {
            return Collections.unmodifiableList(items);
        }
    }
    @LifecycleMeta(InvoiceItemStateMachineMeta.class)
    public static class InvoiceItem extends ReactiveObject {

        private int seq;
        private BigDecimal amount;
        private BigDecimal payedAmount;
        private final Invoice parent;

        public InvoiceItem(Invoice parent, BigDecimal amount) {
            initialState(InvoiceItemStateMachineMeta.States.Unpaid.class.getSimpleName());
            this.amount = amount;
            this.parent = parent;
            this.seq = this.parent.getItems().size() + 1;
            this.parent.addItem(this);
        }

        @Event
        public void pay(final BigDecimal amount) {
            if ( 0 < this.amount.compareTo(amount) ) {
                throw new IllegalArgumentException("paying amount is not enough to pay this item.");
            }
            this.payedAmount = amount;
        }

        public BigDecimal getPayedAmount() {
            return payedAmount;
        }

        @Relation(InvoiceItemStateMachineMeta.Relations.ParentInvoice.class)
        public Invoice getParent() {
            return this.parent;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( parent == null ) ? 0 : parent.hashCode() );
            result = prime * result + seq;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if ( this == obj ) return true;
            if ( obj == null ) return false;
            if ( getClass() != obj.getClass() ) return false;
            InvoiceItem other = (InvoiceItem) obj;
            if ( parent == null ) {
                if ( other.parent != null ) return false;
            } else if ( !parent.equals(other.parent) ) return false;
            if ( seq != other.seq ) return false;
            return true;
        }
    }
    @LifecycleMeta(InvoiceItemStateMachineMeta.class)
    public static class InvoiceItemNonRelationalCallback extends ReactiveObject {

        private int seq;
        private BigDecimal amount = new BigDecimal(0);
        private BigDecimal payedAmount = new BigDecimal(0);
        private final InvoiceNonRelationalCallback parent;

        public InvoiceItemNonRelationalCallback(InvoiceNonRelationalCallback parent, BigDecimal amount) {
            initialState(InvoiceItemStateMachineMeta.States.Unpaid.class.getSimpleName());
            this.amount = amount;
            this.parent = parent;
            this.seq = this.parent.getItems().size() + 1;
            this.parent.addItem(this);
        }

        @Event
        public void pay(BigDecimal amount) {
            if ( 0 < this.amount.compareTo(amount) ) {
                throw new IllegalArgumentException("paying amount is not enough to pay this item.");
            }
            this.payedAmount = amount;
            // parent.onItemPaied(this);
        }

        @PostStateChange(to = InvoiceItemStateMachineMeta.States.Paid.class)
        public void notifyParent(LifecycleContext<InvoiceItemNonRelationalCallback, String> context) {
            this.parent.onItemPaied(this);
        }

        public BigDecimal getPayedAmount() {
            return payedAmount;
        }

        @Relation(InvoiceItemStateMachineMeta.Relations.ParentInvoice.class)
        public InvoiceNonRelationalCallback getParent() {
            return this.parent;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( parent == null ) ? 0 : parent.hashCode() );
            result = prime * result + seq;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if ( this == obj ) return true;
            if ( obj == null ) return false;
            if ( getClass() != obj.getClass() ) return false;
            InvoiceItemNonRelationalCallback other = (InvoiceItemNonRelationalCallback) obj;
            if ( parent == null ) {
                if ( other.parent != null ) return false;
            } else if ( !parent.equals(other.parent) ) return false;
            if ( seq != other.seq ) return false;
            return true;
        }
    }
    @LifecycleMeta(InvoiceStateMachineMeta.class)
    public static class InvoiceNonRelationalCallback extends ReactiveObject implements Conditions.Payable {

        private final BigDecimal totalAmount;
        private BigDecimal payedAmount = new BigDecimal(0D);
        private final List<InvoiceItemNonRelationalCallback> items = new ArrayList<InvoiceItemNonRelationalCallback>();

        public InvoiceNonRelationalCallback(final BigDecimal totalAmount) {
            initialState(InvoiceStateMachineMeta.States.Draft.class.getSimpleName());
            this.totalAmount = totalAmount;
        }

        @Condition(InvoiceStateMachineMeta.Conditions.Payable.class)
        public Conditions.Payable getPayable() {
            return this;
        }

        @Override
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        @Override
        public synchronized BigDecimal getPayedAmount() {
            return payedAmount;
        }

        @Event
        public void post() {}

        @Event(InvoiceStateMachineMeta.Events.Pay.class)
        public synchronized void onItemPaied(InvoiceItemNonRelationalCallback item) {
            payedAmount = payedAmount.add(item.getPayedAmount());
        }

        public void addItem(InvoiceItemNonRelationalCallback invoiceItem) {
            if ( !items.contains(invoiceItem) ) {
                items.add(invoiceItem);
            }
        }

        public List<InvoiceItemNonRelationalCallback> getItems() {
            return Collections.unmodifiableList(items);
        }
    }
    @StateMachine
    public static interface OrderStateMachine {

        @StateSet
        public static interface States {

            @Initial
            @Transition(event = Events.Pay.class, value = { States.Paid.class })
            public static interface New {}
            @Transition(event = Events.Deliver.class, value = { States.Delivered.class })
            public static interface Paid {}
            @Final
            public static interface Delivered {}
        }
        @EventSet
        public static interface Events {

            public static interface Pay {}
            public static interface Deliver {}
        }
    }
    @StateMachine
    public static interface BigProductOrderStateMachine extends OrderStateMachine {

        @StateSet
        public static interface States extends OrderStateMachine.States {

            @Transition(event = Events.Cancel.class, value = { Cancelled.class })
            public static interface New extends OrderStateMachine.States.New {}
            @LifecycleOverride
            @Transition(event = Events.Install.class, value = { States.Installed.class })
            public static interface Delivered extends OrderStateMachine.States.Delivered {}
            @Final
            public static interface Installed {}
            @Final
            public static interface Cancelled {}
        }
        @EventSet
        public static interface Events extends OrderStateMachine.Events {

            public static interface Install {}
            public static interface Cancel {}
        }
    }
    @LifecycleMeta(OrderStateMachine.class)
    public static class OrderObject<T> extends ReactiveObject {

        protected int count = 0;

        public OrderObject() {
            initialState(OrderStateMachine.States.New.class.getSimpleName());
        }

        @Event
        public void pay() {}

        @Event
        public void deliver() {}

        public int getCount() {
            return count;
        }

        @PostStateChange(from = OrderStateMachine.States.New.class)
        public void interceptPostStateChange(LifecycleContext<T, String> context) {
            count++;
            System.out.println("Order is created.");
        }

        @PostStateChange(to = OrderStateMachine.States.Delivered.class)
        public void interceptPostStateChangeWhenOrderFinished(LifecycleContext<T, String> context) {
            count++;
            System.out.println("Order is delivered.");
        }
    }
    @LifecycleMeta(BigProductOrderStateMachine.class)
    public static class BigProductOrderObjectWithExtendsCallbackDefinition extends OrderObject<BigProductOrderObjectWithExtendsCallbackDefinition> {

        @Event
        public void install() {}

        @Event
        public void cancel() {}

        /**
         * Use case 1: extends the call back method on the non overridden
         * state "New".<br/>
         * Scenario: <li>StateOverride: No</li> <li>Lifecycle Override: No</li>
         * 
         * Expected Behavior:<br/>
         * When state transit from New or Delivered state, the
         * call back method will be invoked.
         */
        @PostStateChange(from = BigProductOrderStateMachine.States.Delivered.class)
        @Override
        public void interceptPostStateChange(LifecycleContext<BigProductOrderObjectWithExtendsCallbackDefinition, String> context) {
            count++;
            System.out.println("interceptPostStateChange in " + getClass().getSimpleName());
        }
    }
    @LifecycleMeta(BigProductOrderStateMachine.class)
    public static class BigProductOrderObjectWithOverridesCallbackDefinition extends OrderObject<BigProductOrderObjectWithOverridesCallbackDefinition> {

        @Event
        public void install() {}

        @Event
        public void cancel() {}

        /**
         * Use case 2: overrides the call back definition on the
         * overridden state "Delivered".<br/>
         * Scenario: <li>StateOverride: Yes</li> <li>Lifecycle Override: Yes</li>
         * Expected Behavior:<br/>
         * When state transit to state "Delivered",
         * this call back method will not be
         * invoked.
         * When state transit to state "Installed", this method will be
         * invoked.
         */
        @LifecycleOverride
        @PostStateChange(to = BigProductOrderStateMachine.States.Installed.class)
        @Override
        public void interceptPostStateChangeWhenOrderFinished(LifecycleContext<BigProductOrderObjectWithOverridesCallbackDefinition, String> context) {
            count++;
            System.out.println("Big Product Order is finished.");
        }
    }
    @LifecycleMeta(BigProductOrderStateMachine.class)
    public static class OrderWithSpecifiedFromToCallback extends OrderObject<OrderWithSpecifiedFromToCallback> {

        @Event
        public void install() {}

        @Event
        public void cancel() {}

        @PostStateChange(from = BigProductOrderStateMachine.States.New.class, to = BigProductOrderStateMachine.States.Cancelled.class)
        public void intercept(LifecycleContext<OrderWithSpecifiedFromToCallback, String> context) {
            count++;
            System.out.println("OrderWithSpecifiedFromToCallback is finished.");
        }
    }
    @LifecycleMeta(BigProductOrderStateMachine.class)
    public static class OrderWithSpecifiedPreFromToCallback extends OrderObject<OrderWithSpecifiedPreFromToCallback> {

        @Event
        public void install() {}

        @Event
        public void cancel() {}

        @PreStateChange(from = BigProductOrderStateMachine.States.Delivered.class, to = BigProductOrderStateMachine.States.Installed.class)
        public void intercept(LifecycleContext<OrderWithSpecifiedPreFromToCallback, String> context) {
            Assert.assertEquals(BigProductOrderStateMachine.States.Installed.class.getSimpleName(), context.getToStateName());
            Assert.assertEquals(BigProductOrderStateMachine.States.Installed.class.getSimpleName(), context.getToState());
            Assert.assertEquals(BigProductOrderStateMachine.States.Delivered.class.getSimpleName(), context.getFromStateName());
            Assert.assertEquals(BigProductOrderStateMachine.States.Delivered.class.getSimpleName(), context.getFromState());
            Assert.assertEquals(this, context.getTarget());
            try {
                Assert.assertEquals(OrderWithSpecifiedPreFromToCallback.class.getMethod("install"), context.getEventMethod());
            } catch (NoSuchMethodException ignored){} catch( SecurityException ignored) {}
            Assert.assertEquals(0, context.getArguments().length);
            count++;
            System.out.println("OrderWithSpecifiedPreFromToCallback is finished.");
        }
    }
}
