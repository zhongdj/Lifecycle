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
package net.imadz.lifecycle.syntax.lm.relation;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_Relations.S5_B_R1;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_States.S5_B_A;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.Relations.S7_R;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.States.S7_B;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.Relations.S8_R;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.States.S8_A;
import net.imadz.verification.VerificationException;
import org.junit.Test;

public class LMSyntaxRelationNegativeTest extends LMSyntaxRelationMetadata {

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3223_if_inboundwhile_relation_not_coveraged() throws VerificationException {
    @LifecycleRegistry(NLM_1.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s7_X", NLM_1.class.getName(),
          S7_R.class.getSimpleName(), S7.class.getName() + ".StateSet." + S7_B.class.getSimpleName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3223_if_validwhile_relation_not_coveraged() throws VerificationException {
    @LifecycleRegistry(NLM_2.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s8_X", NLM_2.class.getName(),
          S8_R.class.getSimpleName(), S8.class.getName() + ".StateSet." + S8_A.class.getSimpleName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3223_if_relation_in_composite_stateMachine_not_coveraged() throws VerificationException {
    @LifecycleRegistry(NLM_3.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s5_B_X", NLM_3.class.getName(),
          S5_B_R1.class.getSimpleName(), S5.class.getName() + ".CompositeStateMachine.S5_B.StateSet." + S5_B_A.class.getSimpleName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3223_if_relation_in_super_stateMachine_not_coveraged() throws VerificationException {
    @LifecycleRegistry(NLM_4.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s5_B_X", NLM_4.class.getName(),
          S5_B_R1.class.getSimpleName(), S5.class.getName() + ".CompositeStateMachine.S5_B.StateSet." + S5_B_A.class.getSimpleName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3221_if_LM_reference_an_invalid_relation_on_field() throws VerificationException {
    @LifecycleRegistry(NLM_5.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REFERENCE_INVALID_RELATION_INSTANCE, NLM_5.class.getName(),
          S4.Relations.R1.class.getName(), S5.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3221_if_test_LM_reference_an_invalid_relation_on_property() throws VerificationException {
    @LifecycleRegistry(NLM_6.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REFERENCE_INVALID_RELATION_INSTANCE, NLM_6.class.getName(),
          S4.Relations.R1.class.getName(), R1_S.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3222_if_relation_defined_multi_times_in_class_level() throws VerificationException {
    @LifecycleRegistry(Negative_Same_Relation_Concreted_Duplicate_On_Fields_r2_r3.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_INSTANCE_MUST_BE_UNIQUE,
          Negative_Same_Relation_Concreted_Duplicate_On_Fields_r2_r3.class.getName(), S4.Relations.R3.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public final void should_throw_002_3222_if_relation_defined_multi_times_in_method_level() throws VerificationException {
    @LifecycleRegistry(Negative_Same_Relation_Concreted_Multiple_Times_In_Method_tM1.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_INSTANCE_MUST_BE_UNIQUE,
          Negative_Same_Relation_Concreted_Multiple_Times_In_Method_tM1.class.getName(), S4.Relations.R1.class.getName());
      throw e;
    }
  }
}
