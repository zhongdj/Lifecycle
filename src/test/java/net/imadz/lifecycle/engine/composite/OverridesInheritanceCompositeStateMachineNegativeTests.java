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

import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleException;
import org.junit.Test;

public class OverridesInheritanceCompositeStateMachineNegativeTests extends EngineCoreCompositeStateMachineMetadata {

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9001_if_invoke_T1_while_contract_Active() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    contract.activate();
    assertState(ContractLifecycle.States.Active.class, contract);
    try {
      object.doActionT1();
    } catch (LifecycleException e) {
      assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
      throw e;
    }
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9001_if_invoke_T2_while_contract_Active() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    contract.activate();
    assertState(ContractLifecycle.States.Active.class, contract);
    try {
      object.doActionT2();
    } catch (LifecycleException e) {
      assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
      throw e;
    }
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9001_if_invoke_T6_while_contract_Active() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate();
    assertState(ContractLifecycle.States.Active.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    try {
      object.doActionT6();
    } catch (LifecycleException e) {
      assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
      throw e;
    }
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9001_if_invoke_T6_while_contract_Canceled() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate();
    assertState(ContractLifecycle.States.Active.class, contract);
    contract.cancel();
    assertState(ContractLifecycle.States.Canceled.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    try {
      object.doActionT6();
    } catch (LifecycleException e) {
      assertInvalidStateErrorByValidWhile(e, contract, object, ContractLifecycle.States.Expired.class, ContractLifecycle.States.Draft.class);
      throw e;
    }
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9000_if_invoke_T6_while_contract_Expired() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate();
    assertState(ContractLifecycle.States.Active.class, contract);
    contract.expire();
    assertState(ContractLifecycle.States.Expired.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    try {
      object.doActionT6();
    } catch (LifecycleException e) {
      assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_EVENT_ON_STATE, SM1_Overrides.Events.T6.class,
          SM1_Overrides.States.S1.CStates.CS0.class.getSimpleName(), object);
    }
  }

  @Test(expected = LifecycleException.class)
  public void should_throw_002_9000_if_invoke_T6_while_contract_Draft() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    final OverridesComposite object = new OverridesComposite(contract);
    assertState(SM1_Overrides.States.S0.class, object);
    object.doActionT2();
    assertState(SM1_Overrides.States.S1.CStates.CS0.class, object);
    try {
      object.doActionT6();
    } catch (LifecycleException e) {
      assertLifecycleError(e, LifecycleCommonErrors.ILLEGAL_EVENT_ON_STATE, SM1_Overrides.Events.T6.class,
          SM1_Overrides.States.S1.CStates.CS0.class.getSimpleName(), object);
    }
  }
}
