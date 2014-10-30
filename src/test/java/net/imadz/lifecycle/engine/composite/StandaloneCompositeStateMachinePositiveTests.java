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
package net.imadz.lifecycle.engine.composite;

import org.junit.Test;

public class StandaloneCompositeStateMachinePositiveTests extends EngineCoreCompositeStateMachineMetadata {

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Part I: Stand alone composite state machine (composite state machine
    // without inheritance)
    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void should_support_non_relational_composite_state_machine_complete_process() {
        final ProductOrder product = new ProductOrder();
        // Outer State + Outer Event => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(OrderLifecycle.States.Created.class, product);
            // Outer Event
            product.start();
            assertState(OrderLifecycle.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Event => Composite State
            product.doProduce();
            assertState(OrderLifecycle.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(OrderLifecycle.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Event => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(OrderLifecycle.States.Finished.class, product);
        }
    }

    @Test
    public void should_support_non_relational_composite_state_machine_cancel_process_with_outer_transition() {
        final ProductOrder product = new ProductOrder();
        {// Outer State + Outer Event => Composite State => Composite
         // Initial State
            assertState(OrderLifecycle.States.Created.class, product);
            product.start();
            assertState(OrderLifecycle.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Event => Composite State
            product.doProduce();
            assertState(OrderLifecycle.States.Started.SubStates.Producing.class, product);
        }
        {
            // Composite State + Outer Event => Outer State
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
        // Outer State + Outer Event => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Created.class, product);
            // Outer Event
            product.start();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Event => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleSharingValidWhile.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Event => Composite Final State
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
        // Outer State + Outer Event => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Created.class, product);
            // Outer Event
            product.start();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Event => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleReferencingOuterRelation.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Event => Composite Final State
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
        // Outer State + Outer Event => Composite State => Composite
        // Initial State
        {
            // Outer Initial State
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Created.class, product);
            // Outer Event
            product.start();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.OrderCreated.class, product);
        }
        {
            // Composite State + Composite Event => Composite State
            product.doProduce();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.Producing.class, product);
            product.doDeliver();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Started.SubStates.Delivering.class, product);
        }
        {
            // Composite State + Composite Event => Composite Final State
            // => Outer State
            product.confirmComplete();
            assertState(RelationalOrderLifecycleReferencingInnerValidWhile.States.Finished.class, product);
        }
    }
}
