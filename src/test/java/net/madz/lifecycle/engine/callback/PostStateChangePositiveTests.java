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
 * 
 */
package net.madz.lifecycle.engine.callback;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class PostStateChangePositiveTests extends CallbackTestMetadata {

    @Test
    public void should_increase_counter_if_any_transition_method_invoked() {
        final PostCallbackFromAnyToAny o = new PostCallbackFromAnyToAny();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(1, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(2, o.getCallbackInvokeCounter());
    }

    @Test
    public void should_increase_counter_only_if_transition_method_invoked_when_state_is_started() {
        final PostCallbackFromStartToAny o = new PostCallbackFromStartToAny();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(1, o.getCallbackInvokeCounter());
    }

    @Test
    public void should_increase_counter_only_if_transition_method_invoked_when_next_state_is_started() {
        final PostCallbackFromAnyToStart o = new PostCallbackFromAnyToStart();
        assertEquals(0, o.getCallbackInvokeCounter());
        o.start();
        assertEquals(1, o.getCallbackInvokeCounter());
        o.finish();
        assertEquals(1, o.getCallbackInvokeCounter());
    }

    @Test
    public void non_relational_callback() {
        final InvoiceNonRelationalCallback invoice = new InvoiceNonRelationalCallback(new BigDecimal(10000.0D));
        final InvoiceItemNonRelationalCallback itemOne = new InvoiceItemNonRelationalCallback(invoice, new BigDecimal(4000.0D));
        final InvoiceItemNonRelationalCallback itemTwo = new InvoiceItemNonRelationalCallback(invoice, new BigDecimal(4000.0D));
        final InvoiceItemNonRelationalCallback itemThree = new InvoiceItemNonRelationalCallback(invoice, new BigDecimal(2000.0D));
        invoice.post();
        assertState(InvoiceStateMachineMeta.States.Posted.class, invoice);
        assertState(InvoiceItemStateMachineMeta.States.Unpaid.class, itemOne);
        itemOne.pay(new BigDecimal(4000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemOne);
        assertState(InvoiceStateMachineMeta.States.PartialPaid.class, invoice);
        itemTwo.pay(new BigDecimal(4000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemTwo);
        assertState(InvoiceStateMachineMeta.States.PartialPaid.class, invoice);
        itemThree.pay(new BigDecimal(2000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemThree);
        assertState(InvoiceStateMachineMeta.States.PaidOff.class, invoice);
    }

    @Test
    public void relational_callback() {
        final Invoice invoice = new Invoice(new BigDecimal(10000.0D));
        final InvoiceItem itemOne = new InvoiceItem(invoice, new BigDecimal(4000.0D));
        final InvoiceItem itemTwo = new InvoiceItem(invoice, new BigDecimal(4000.0D));
        final InvoiceItem itemThree = new InvoiceItem(invoice, new BigDecimal(2000.0D));
        invoice.post();
        assertState(InvoiceStateMachineMeta.States.Posted.class, invoice);
        assertState(InvoiceItemStateMachineMeta.States.Unpaid.class, itemOne);
        itemOne.pay(new BigDecimal(4000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemOne);
        assertState(InvoiceStateMachineMeta.States.PartialPaid.class, invoice);
        itemTwo.pay(new BigDecimal(4000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemTwo);
        assertState(InvoiceStateMachineMeta.States.PartialPaid.class, invoice);
        itemThree.pay(new BigDecimal(2000.0D));
        assertState(InvoiceItemStateMachineMeta.States.Paid.class, itemThree);
        assertState(InvoiceStateMachineMeta.States.PaidOff.class, invoice);
    }

    @Test
    public void should_increase_counter_if_transition_method_invoked_when_specified_from_and_to_states_matched() {
        final OrderWithSpecifiedFromToCallback bigOrder = new OrderWithSpecifiedFromToCallback();
        assertEquals(0, bigOrder.getCount());
        bigOrder.cancel();
        assertEquals(2, bigOrder.getCount());
    }
}
