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

import static org.junit.Assert.fail;
import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleException;

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
