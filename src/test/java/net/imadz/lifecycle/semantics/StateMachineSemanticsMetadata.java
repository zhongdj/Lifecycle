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
package net.imadz.lifecycle.semantics;

import net.imadz.common.ConsoleLoggingTestBase;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Events.S1_X;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Events.S1_Y;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Events.S1_Z;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S2.Events.S2_Z;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S2.States.S2_B.S2_B_Events.S2_B_Events_X;
import net.imadz.lifecycle.semantics.StateMachineSemanticsMetadata.S3.Events.S3_Z;

public class StateMachineSemanticsMetadata extends ConsoleLoggingTestBase {

  @StateMachine
  public static interface S1 {

    @StateSet
    public static interface States {

      @Initial
      @Transition(event = S1_X.class, value = {S1_B.class})
      public static interface S1_A {}

      @Transition(event = S1_Y.class, value = {S1_C.class})
      public static interface S1_B {}

      @Transition(event = S1_Z.class, value = {S1_D.class})
      public static interface S1_C {}

      @Final
      public static interface S1_D {}
    }

    @EventSet
    public static interface Events {

      public static interface S1_X {}

      public static interface S1_Y {}

      public static interface S1_Z {}
    }
  }

  @StateMachine
  public static interface S2 extends S1 {

    @StateSet
    public static interface States extends S1.States {

      @Initial
      @LifecycleOverride
      @Transition(event = S2_Z.class, value = {S1_C.class})
      public static interface S2_A extends S1.States.S1_A {}

      @LifecycleOverride
      @CompositeState
      public static interface S2_B extends S1.States.S1_B {

        @StateSet
        public static interface S2_B_States {

          @Initial
          @Transition(event = S2_B_Events_X.class, value = {S2_B_States_B.class})
          public static interface S2_B_States_A {}

          @Final
          @ShortCut(S1_C.class)
          public static interface S2_B_States_B {}
        }

        @EventSet
        public static interface S2_B_Events {

          public static interface S2_B_Events_X {}
        }
      }
    }

    @EventSet
    public static interface Events extends S1.Events {

      public static interface S2_Z {}
    }
  }

  @StateMachine
  public static interface S3 extends S2 {

    @StateSet
    public static interface States extends S2.States {

      @Initial
      @LifecycleOverride
      @Transition(event = S3_Z.class, value = {S1_D.class})
      public static interface S3_A extends S2_A {}

      @CompositeState
      public static interface S3_E extends S2.States.S2_B {}
    }

    @EventSet
    public static interface Events extends S2.Events {

      public static interface S3_Z {}
    }
  }
}
