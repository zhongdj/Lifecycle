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
package net.imadz.lifecycle.syntax.basic;

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

public class StateSetAndEventSetSyntaxNegativeTest extends StateSetSyntaxMetadata {

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2107_if_stateMachine_defines_no_innerClasses() throws VerificationException {
    @LifecycleRegistry(Negative_No_InnerClasses.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertEquals(1, e.getVerificationFailureSet().size());
      final VerificationFailure failure = e.getVerificationFailureSet().iterator().next();
      assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES, failure.getErrorCode());
      final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_INNER_CLASSES_OR_INTERFACES,
          new Object[] {Negative_No_InnerClasses.class.getName()});
      assertEquals(expectedMessage, failure.getErrorMessage(null));
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2103_and_002_2105_if_stateMachine_defines_no_stateSet_and_eventSet() throws VerificationException {
    @LifecycleRegistry(Negative_No_StateSet_and_EventSet.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertEquals(2, e.getVerificationFailureSet().size());
      Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
      final VerificationFailure failureOne = iterator.next();
      final VerificationFailure failureTwo = iterator.next();
      {
        assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_STATESET, failureOne.getErrorCode());
        assertEquals(SyntaxErrors.STATEMACHINE_WITHOUT_EVENTSET, failureTwo.getErrorCode());
      }
      {
        final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_STATESET, Negative_No_StateSet_and_EventSet.class);
        assertEquals(expectedMessage, failureOne.getErrorMessage(null));
      }
      {
        final String expectedMessage = getMessage(SyntaxErrors.STATEMACHINE_WITHOUT_EVENTSET, Negative_No_StateSet_and_EventSet.class);
        assertEquals(expectedMessage, failureTwo.getErrorMessage(null));
      }
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2104_and_002_2106_if_stateMachine_defines_multi_stateSet_and_multi_eventSet() throws VerificationException {
    @LifecycleRegistry({Negative_Multi_StateSet_Multi_EventSet.class})
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException ex) {
      assertEquals(2, ex.getVerificationFailureSet().size());
      Iterator<VerificationFailure> iterator = ex.getVerificationFailureSet().iterator();
      final VerificationFailure failureOne = iterator.next();
      final VerificationFailure failureTwo = iterator.next();
      assertFailure(failureOne, SyntaxErrors.STATEMACHINE_MULTIPLE_STATESET, Negative_Multi_StateSet_Multi_EventSet.class);
      assertFailure(failureTwo, SyntaxErrors.STATEMACHINE_MULTIPLE_EVENTSET, Negative_Multi_StateSet_Multi_EventSet.class);
      throw ex;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2403_and_002_2501_if_stateSet_defines_no_states_and_eventSet_has_no_events() throws VerificationException {
    @LifecycleRegistry({Negative_No_State_No_Event.class})
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
      assertFailure(iterator.next(), SyntaxErrors.STATESET_WITHOUT_STATE, Negative_No_State_No_Event.States.class);
      assertFailure(iterator.next(), SyntaxErrors.EVENTSET_WITHOUT_EVENT, Negative_No_State_No_Event.Events.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2400_and_002_2402_if_stateSet_defines_no_initialState_and_endState() throws VerificationException {
    @LifecycleRegistry(Negative_StateSet_Without_InitalState_And_EndState.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      VerificationFailureSet failureSet = e.getVerificationFailureSet();
      assertEquals(2, failureSet.size());
      Iterator<VerificationFailure> iterator = failureSet.iterator();
      VerificationFailure failureOne = iterator.next();
      VerificationFailure failureTwo = iterator.next();
      assertFailure(failureOne, SyntaxErrors.STATESET_WITHOUT_INITIAL_STATE, Negative_StateSet_Without_InitalState_And_EndState.States.class);
      assertFailure(failureTwo, SyntaxErrors.STATESET_WITHOUT_FINAL_STATE, Negative_StateSet_Without_InitalState_And_EndState.States.class);
      throw e;
    }
  }

  @Test(expected = VerificationException.class)
  public void should_throw_exception_002_2401_if_stateSet_has_multi_initialState() throws VerificationException {
    @LifecycleRegistry(Negative_StateSet_With_Multi_InitalState.class)
    @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    try {
      new Registry();
    } catch (VerificationException e) {
      assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATESET_MULTIPLE_INITAL_STATES,
          Negative_StateSet_With_Multi_InitalState.States.class);
      throw e;
    }
  }
}
