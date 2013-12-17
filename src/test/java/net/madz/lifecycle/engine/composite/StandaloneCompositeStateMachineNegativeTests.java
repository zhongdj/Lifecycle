package net.madz.lifecycle.engine.composite;

import static org.junit.Assert.fail;
import net.madz.lifecycle.LifecycleCommonErrors;
import net.madz.lifecycle.LifecycleException;

import org.junit.Test;

public class StandaloneCompositeStateMachineNegativeTests extends EngineCoreCompositeStateMachineMetadata {

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Part I: Stand alone composite state machine (composite state machine
    // without inheritance)
    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test(expected = LifecycleException.class)
    public void should_throw_002_9000_if_invoke_illegal_transition_method() {
        final ProductOrder order = new ProductOrder();
        assertState(OrderLifecycle.States.Created.class, order);
        order.start();
        assertState(OrderLifecycle.States.Started.SubStates.OrderCreated.class, order);
        try {
            order.doDeliver();
            fail("should throw LifecycleException");
        } catch (LifecycleException e) {
            try {
                assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE,
                        OrderLifecycle.States.Started.SubTransitions.DoDeliver.class.getSimpleName(),
                        OrderLifecycle.States.Started.SubStates.OrderCreated.class.getSimpleName(), order);
            } catch (LifecycleException ex) {}
        }
        order.doProduce();
        try {
            order.confirmComplete();
            fail("should throw LifecycleException");
        } catch (LifecycleException e) {
            assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_TRANSITION_ON_STATE,
                    OrderLifecycle.States.Started.SubTransitions.ConfirmComplete.class.getSimpleName(),
                    OrderLifecycle.States.Started.SubStates.Producing.class.getSimpleName(), order);
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_violates_sharing_validwhile_constraint() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final ProductOrderSharingValidWhile order = new ProductOrderSharingValidWhile(contract);
        try {
            order.start();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, order, ContractLifecycle.States.Active.class);
        }
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        order.start();
        assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.OrderCreated.class, order);
        contract.cancel();
        assertState(ContractLifecycle.States.Canceled.class, contract);
        try {
            order.doProduce();
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, order, ContractLifecycle.States.Active.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_violates_validwhile_constraint_referencing_outer_relation() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final ProductOrderOuterRelation order = new ProductOrderOuterRelation(contract);
        try {
            order.start();
            fail("Should throw LifecycleException");
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, order, ContractLifecycle.States.Active.class);
        }
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        order.start();
        assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.OrderCreated.class, order);
        contract.cancel();
        assertState(ContractLifecycle.States.Canceled.class, contract);
        try {
            order.doProduce();
            fail("Should throw LifecycleException");
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, order, ContractLifecycle.States.Active.class);
            throw e;
        }
    }

    @Test(expected = LifecycleException.class)
    public void should_throw_002_9001_if_violates_validwhile_constraint_reference_inner_relation() {
        final Contract contract = new Contract();
        assertState(ContractLifecycle.States.Draft.class, contract);
        final ProductOrderInnerValidWhile order = new ProductOrderInnerValidWhile(contract);
        try {
            order.start();
        } catch (LifecycleException e) {
            fail("Should not throw LifecycleException");
        }
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        order.start();
        assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.OrderCreated.class, order);
        contract.cancel();
        assertState(ContractLifecycle.States.Canceled.class, contract);
        try {
            order.doProduce();
            fail("Should throw LifecycleException");
        } catch (LifecycleException e) {
            assertInvalidStateErrorByValidWhile(e, contract, order, ContractLifecycle.States.Active.class);
            throw e;
        }
    }
}
