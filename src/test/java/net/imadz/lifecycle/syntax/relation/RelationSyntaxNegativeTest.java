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
package net.imadz.lifecycle.syntax.relation;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailure;
import org.junit.Test;

import java.util.Iterator;

public class RelationSyntaxNegativeTest extends RelationSyntaxMetadata {

  @Test(expected = VerificationException.class)
  public void should_throw_002_2931_if_relateTo_refers_to_invalid_statemachine() throws VerificationException {
    @LifecycleRegistry(NStandalone3.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_RELATED_TO_REFER_TO_NON_STATEMACHINE,
          NStandalone3.Relations.NR.class.getAnnotation(RelateTo.class));
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3500_if_defines_multiple_relationset() throws VerificationException {
    @LifecycleRegistry(NStandalone4.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATIONSET_MULTIPLE, NStandalone4.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2913_and_002_2914_if_relation_otherwise_refers_to_invalid_state() throws VerificationException {
    @LifecycleRegistry(NStandalone5.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      final Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
      assertFailure(iterator.next(), SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_INBOUNDWHILE_INVALID,
          NStandalone5.States.N5A.class.getAnnotation(InboundWhile.class), NStandalone5.States.N5A.class, RelatedSM.class.getName());
      assertFailure(iterator.next(), SyntaxErrors.RELATION_OTHERWISE_ATTRIBUTE_OF_VALIDWHILE_INVALID,
          NStandalone5.States.N5A.class.getAnnotation(ValidWhile.class), NStandalone5.States.N5A.class, RelatedSM.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2911_if_relation_not_defined_in_relation_set() throws VerificationException {
    @LifecycleRegistry(NStandalone.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET,
          NStandalone.States.NA.class.getAnnotation(InboundWhile.class).relation(), NStandalone.States.NA.class, NStandalone.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2912_if_relation_on_state_not_defined_in_relation() throws VerificationException {
    @LifecycleRegistry(NStandalone2.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION,
          NStandalone2.States.NA.class.getAnnotation(InboundWhile.class), NStandalone2.States.NA.class, RelatedSM.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2911_if_inheritance_relation_not_defined_in_relation_set() throws VerificationException {
    @LifecycleRegistry(NChild.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_INBOUNDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET,
          NChild.States.NCC.class.getAnnotation(InboundWhile.class).relation(), NChild.States.NCC.class, NChild.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2912_if_inheritance_relation_on_state_not_defined_in_relation() throws VerificationException {
    @LifecycleRegistry(NChild2.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_INBOUNDWHILE_NOT_MATCHING_RELATION,
          NChild2.States.NC2C.class.getAnnotation(InboundWhile.class), NChild2.States.NC2C.class, RelatedSM.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2921_if_inheritance_validWhile_relation_not_defined_in_relation_set() throws VerificationException {
    @LifecycleRegistry(NChild3.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_VALIDWHILE_RELATION_NOT_DEFINED_IN_RELATIONSET,
          NChild3.States.NC3C.class.getAnnotation(ValidWhile.class).relation(), NChild3.States.NC3C.class, NChild3.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2922_if_inheritance_validWhile_relation_on_not_matching_relation() throws VerificationException {
    @LifecycleRegistry(NChild4.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_ON_ATTRIBUTE_OF_VALIDWHILE_NOT_MACHING_RELATION,
          NChild4.States.NC4C.class.getAnnotation(InboundWhile.class), NChild4.States.NC4C.class, RelatedSM.class.getName());
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3501_if_multiple_parent_relation_defined_in_standalone() throws VerificationException {
    @LifecycleRegistry(NStandaloneParent.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_MULTIPLE_PARENT_RELATION, NStandaloneParent.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3502_if_forget_to_override_super_state_machine_parent_relation() throws VerificationException {
    @LifecycleRegistry(NParentRelationChild.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors
              .RELATION_NEED_OVERRIDES_TO_OVERRIDE_SUPER_STATEMACHINE_PARENT_RELATION,
          NParentRelationChild.class, PParentRelationSuper.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3503_if_overrides_owning_state_machine_parent_relation_with_overrides() throws VerificationException {
    @LifecycleRegistry(NOwningStateMachine.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(),
          SyntaxErrors.RELATION_COMPOSITE_STATE_MACHINE_CANNOT_OVERRIDE_OWNING_PARENT_RELATION, NOwningStateMachine.class.getName()
              + ".CompositeStateMachine." + NOwningStateMachine.States.NOwningB.class.getSimpleName(),
          NOwningStateMachine.States.NOwningB.CRelations.NCR.class, NOwningStateMachine.class, NOwningStateMachine.Relations.NR.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3503_if_overrides_owning_state_machine_parent_relation_without_overrides() throws VerificationException {
    @LifecycleRegistry(N2OwningStateMachine.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(),
          SyntaxErrors.RELATION_COMPOSITE_STATE_MACHINE_CANNOT_OVERRIDE_OWNING_PARENT_RELATION, N2OwningStateMachine.class.getName()
              + ".CompositeStateMachine." + N2OwningStateMachine.States.N2OwningB.class.getSimpleName(),
          N2OwningStateMachine.States.N2OwningB.CRelations.N2CR.class, N2OwningStateMachine.class, N2OwningStateMachine.Relations.N2R.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_3504_if_relation_without_relate_to() throws VerificationException {
    @LifecycleRegistry(NoRelateTo.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.RELATION_NO_RELATED_TO_DEFINED, NoRelateTo.Relations.Relative
          .class);
      throw e;
    }
  }
}
