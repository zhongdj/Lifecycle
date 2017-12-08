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

public class NonOverridedInheritanceCompositeStateMachinePositiveTests extends EngineCoreCompositeStateMachineMetadata {

  // /////////////////////////////////////////////////////////////////////////////////////////////////////
  // Part II: composite state machine with inheritance) According to Image
  // File:
  // Composite State Machine Visibility Scope.png
  // /////////////////////////////////////////////////////////////////////////////////////////////////////
  @Test
  public void should_be_ok_if_satisfy_validwhile_constraint_on_owning_state_super_state_with_Active_contract() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate(); // Draft from Owning State, Active From Owning
    // State's super State, Expire from super state.
    assertState(ContractLifecycle.States.Active.class, contract);
    final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
    assertState(SM1_No_Overrides.States.S0.class, noOverride);
    noOverride.doActionT2();
    assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
    noOverride.doActionT1();// noOverride.doActionT4();
    assertState(SM1_No_Overrides.States.S2.CStates.CS2.class, noOverride);
    noOverride.doActionT3();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S3.class, noOverride);
  }

  @Test
  public void should_be_ok_if_satisfy_validwhile_constraint_on_owning_state_super_state_with_Active_contract_and_invoke_owning_super_T6() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate(); // Draft from Owning State, Active From Owning
    // State's super State, Expire from super state.
    assertState(ContractLifecycle.States.Active.class, contract);
    final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
    assertState(SM1_No_Overrides.States.S0.class, noOverride);
    noOverride.doActionT2();
    assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
    noOverride.doActionT6();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S2.CStates.CS2.class, noOverride);
    noOverride.doActionT6();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S3.class, noOverride);
  }

  @Test
  public void should_be_ok_if_satisfy_validwhile_constraint_on_owning_state_super_state_with_Active_contract_and_invoke_owning_T2() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate(); // Draft from Owning State, Active From Owning
    // State's super State, Expire from super state.
    assertState(ContractLifecycle.States.Active.class, contract);
    final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
    assertState(SM1_No_Overrides.States.S0.class, noOverride);
    noOverride.doActionT2();
    assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
    noOverride.doActionT2();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S2.CStates.CS2.class, noOverride);
    noOverride.doActionT2();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S3.class, noOverride);
  }

  @Test
  public void should_be_ok_if_satisfy_validwhile_constraint_on_owning_state_with_Draft_contract() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    // Draft from Owning State, Active From Owning
    // State's super State, Expire from super state.
    final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
    assertState(SM1_No_Overrides.States.S0.class, noOverride);
    noOverride.doActionT2();
    assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
    noOverride.doActionT4();// noOverride.doActionT4();
    assertState(SM1_No_Overrides.States.S2.CStates.CS2.class, noOverride);
    noOverride.doActionT5();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S3.class, noOverride);
  }

  @Test
  public void should_be_ok_if_satisfy_validwhile_constraint_on_super_state_with_Expired_contract() {
    final Contract contract = new Contract();
    assertState(ContractLifecycle.States.Draft.class, contract);
    contract.activate(); // Draft from Owning State, Active From Owning
    // State's super State, Expire from super state.
    assertState(ContractLifecycle.States.Active.class, contract);
    contract.expire();
    assertState(ContractLifecycle.States.Expired.class, contract);
    final NoOverrideComposite noOverride = new NoOverrideComposite(contract);
    assertState(SM1_No_Overrides.States.S0.class, noOverride);
    noOverride.doActionT2();
    assertState(SM1_No_Overrides.States.S1.CStates.CS0.class, noOverride);
    noOverride.doActionT1();// noOverride.doActionT4();
    assertState(SM1_No_Overrides.States.S2.CStates.CS2.class, noOverride);
    noOverride.doActionT3();// noOverride.doActionT5();
    assertState(SM1_No_Overrides.States.S3.class, noOverride);
  }
}
