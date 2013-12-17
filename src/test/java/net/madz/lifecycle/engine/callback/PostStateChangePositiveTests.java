package net.madz.lifecycle.engine.callback;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import net.madz.lifecycle.engine.callback.CallbackTestMetadata.Invoice;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceItem;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceItemNonRelationalCallback;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceItemStateMachineMeta;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceNonRelationalCallback;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.InvoiceStateMachineMeta;
import net.madz.lifecycle.engine.callback.CallbackTestMetadata.OrderWithSpecifiedFromToCallback;

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
