package net.madz.lifecycle.engine.composite;

import org.junit.Test;

public class StandaloneCompositeStateMachinePositiveTests extends EngineCoreCompositeStateMachineMetadata {

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Part I: Stand alone composite state machine (composite state machine
    // without inheritance)
    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void should_support_non_relational_composite_state_machine_complete_process() {
        final ProductOrder product = new ProductOrder();
        // Outer State + Outer Transition => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(OrderLifecycle.States.Created.class, product);
            // Outer Transition
            product.start();
            assertState(OrderLifecycle.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Transition => Composite State
            product.doProduce();
            assertState(OrderLifecycle.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(OrderLifecycle.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Transition => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(OrderLifecycle.States.Finished.class, product);
        }
    }

    @Test
    public void should_support_non_relational_composite_state_machine_cancel_process_with_outer_transition() {
        final ProductOrder product = new ProductOrder();
        {// Outer State + Outer Transition => Composite State => Composite
         // Initial State
            assertState(OrderLifecycle.States.Created.class, product);
            product.start();
            assertState(OrderLifecycle.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Transition => Composite State
            product.doProduce();
            assertState(OrderLifecycle.States.Started.SubStates.Producing.class, product);
        }
        {
            // Composite State + Outer Transition => Outer State
            product.cancel();
            assertState(OrderLifecycle.States.Canceled.class, product);
        }
    }

    @Test
    public void should_support_composite_state_machine_sharing_valid_while() {
        final Contract contract = new Contract();
        // Outer Initial State
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        final ProductOrderSharingValidWhile product = new ProductOrderSharingValidWhile(contract);
        // Outer State + Outer Transition => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Created.class, product);
            // Outer Transition
            product.start();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Transition => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Transition => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Finished.class, product);
        }
    }

    @Test
    public void should_support_relational_composite_state_machine_reference_outer_relations() {
        final Contract contract = new Contract();
        // Outer Initial State
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        final ProductOrderOuterRelation product = new ProductOrderOuterRelation(contract);
        // Outer State + Outer Transition => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Created.class, product);
            // Outer Transition
            product.start();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Transition => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Transition => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Finished.class, product);
        }
    }

    @Test
    public void should_support_composite_state_machine_has_inner_relations() {
        final Contract contract = new Contract();
        // Outer Initial State
        assertState(ContractLifecycle.States.Draft.class, contract);
        contract.activate();
        assertState(ContractLifecycle.States.Active.class, contract);
        final ProductOrderInnerValidWhile product = new ProductOrderInnerValidWhile(contract);
        // Outer State + Outer Transition => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Created.class, product);
            // Outer Transition
            product.start();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Transition => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Transition => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Finished.class, product);
        }
    }
}
