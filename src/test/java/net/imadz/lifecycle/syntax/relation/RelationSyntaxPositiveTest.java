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
import net.imadz.verification.VerificationException;
import org.junit.Test;

public class RelationSyntaxPositiveTest extends RelationSyntaxMetadata {

  @Test
  public void relation_syntax_positive_standalone() throws VerificationException {
    @LifecycleRegistry(PStandalone.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    new Registry();
  }

  @Test
  public void relation_syntax_positive_inheritance() throws VerificationException {
    @LifecycleRegistry(PChild.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    new Registry();
  }

  @Test
  public void parent_relation_syntax_positive_standalone() throws VerificationException {
    @LifecycleRegistry(PStandaloneParent.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    new Registry();
  }

  @Test
  public void parent_relation_in_composite_state_machine_syntax_positive_standalone() throws VerificationException {
    @LifecycleRegistry(POwningStateMachine.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    new Registry();
  }

  @Test
  public void parent_relation_in_child_state_machine_syntax_positive_standalone() throws VerificationException {
    @LifecycleRegistry(PParentRelationChild.class)
    @StateMachineBuilder
    class Registry extends AbsStateMachineRegistry {

      protected Registry() throws VerificationException {
      }
    }
    new Registry();
  }
}
