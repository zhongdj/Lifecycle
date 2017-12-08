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
package net.imadz.lifecycle.syntax.register;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailure;
import net.imadz.verification.VerificationFailureSet;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class RegisterSyntaxNegativeTest extends RegisterSyntaxTestMetaData {

  @Test(expected = VerificationException.class)
  public void should_throw_002_1000_if_registering_without_StateMachine_or_LifecycleMeta() throws VerificationException {
    @LifecycleRegistry(WithoutMetadataAnnotationErrorSyntax.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class IncorrectStateMachineRegistry extends AbsStateMachineRegistry {

      protected IncorrectStateMachineRegistry() throws VerificationException {
        super();
      }
    }
    try {
      new IncorrectStateMachineRegistry();
    } catch (VerificationException ex) {
      VerificationFailureSet failureSet = ex.getVerificationFailureSet();
      Iterator<VerificationFailure> iterator = failureSet.iterator();
      assertEquals(1, failureSet.size());
      VerificationFailure failure = iterator.next();
      final String expectedErrorMessage = getMessage(SyntaxErrors.REGISTERED_META_ERROR, WithoutMetadataAnnotationErrorSyntax.class);
      final String actualErrorMessage = failure.getErrorMessage(null);
      assertEquals(expectedErrorMessage, actualErrorMessage);
      assertEquals(SyntaxErrors.REGISTERED_META_ERROR, failure.getErrorCode());
      throw ex;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2100_if_registering_super_interface_without_StateMachine() throws VerificationException {
    @LifecycleRegistry(IncorrectStateMachineInheritanceChildSyntax.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class IncorrectStateMachineInheritanceRegistry extends AbsStateMachineRegistry {

      protected IncorrectStateMachineInheritanceRegistry() throws VerificationException {
        super();
      }
    }
    try {
      new IncorrectStateMachineInheritanceRegistry();
    } catch (VerificationException ex) {
      VerificationFailureSet failureSet = ex.getVerificationFailureSet();
      Iterator<VerificationFailure> iterator = failureSet.iterator();
      assertEquals(1, failureSet.size());
      VerificationFailure failure = iterator.next();
      final String expectedErrorMessage = getMessage(SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE,
          IncorrectStateMachineInheritanceSuperSyntax.class);
      final String actualErrorMessage = failure.getErrorMessage(null);
      assertEquals(expectedErrorMessage, actualErrorMessage);
      assertEquals(SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE, failure.getErrorCode());
      throw ex;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2100_if_registering_superclass_without_StateMachine() throws VerificationException {
    @LifecycleRegistry(IllegalExtendsNonStateMachineClass.class)
    @StateMachineBuilder
    class IncorrectStateMachineInheritanceRegistry extends AbsStateMachineRegistry {

      protected IncorrectStateMachineInheritanceRegistry() throws VerificationException {
        super();
      }
    }
    try {
      new IncorrectStateMachineInheritanceRegistry();
    } catch (VerificationException ex) {
      VerificationFailureSet failureSet = ex.getVerificationFailureSet();
      Iterator<VerificationFailure> iterator = failureSet.iterator();
      assertEquals(1, failureSet.size());
      VerificationFailure failure = iterator.next();
      final String expectedErrorMessage = getMessage(SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE, NonStateMachineClass.class);
      final String actualErrorMessage = failure.getErrorMessage(null);
      assertEquals(expectedErrorMessage, actualErrorMessage);
      assertEquals(SyntaxErrors.STATEMACHINE_SUPER_MUST_BE_STATEMACHINE, failure.getErrorCode());
      throw ex;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2101_if_registering_with_multi_super_interfaces() throws VerificationException {
    @LifecycleRegistry(IncorrectStateMachineInheritanceChildWithMultiSuperInterfacesSyntax.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class IncorrectStateMachineInheritanceWithMultiSuperInterfacesRegistry extends AbsStateMachineRegistry {

      protected IncorrectStateMachineInheritanceWithMultiSuperInterfacesRegistry() throws VerificationException {
        super();
      }
    }
    try {
      new IncorrectStateMachineInheritanceWithMultiSuperInterfacesRegistry();
    } catch (VerificationException ex) {
      VerificationFailureSet actualFailureSet = ex.getVerificationFailureSet();
      assertEquals(1, actualFailureSet.size());
      Iterator<VerificationFailure> iterator = actualFailureSet.iterator();
      VerificationFailure failure = iterator.next();
      final String expectedErrorMessage = getMessage(SyntaxErrors.STATEMACHINE_HAS_ONLY_ONE_SUPER_INTERFACE,
          IncorrectStateMachineInheritanceChildWithMultiSuperInterfacesSyntax.class);
      final String actualErrorMessage = failure.getErrorMessage(null);
      assertEquals(expectedErrorMessage, actualErrorMessage);
      assertEquals(SyntaxErrors.STATEMACHINE_HAS_ONLY_ONE_SUPER_INTERFACE, failure.getErrorCode());
      throw ex;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_002_2102_if_registering_lifecycleMeta_value_without_stateMachine_annotation() throws VerificationException {
    @LifecycleRegistry(value = {WrongLifecycleMetaSyntaxWithStateMachineWithoutAnnotation.class})
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATEMACHINE_CLASS_WITHOUT_ANNOTATION,
          WrongStateMachineSyntaxWithoutAnnotation.class);
      throw e;
    }
  }
}
