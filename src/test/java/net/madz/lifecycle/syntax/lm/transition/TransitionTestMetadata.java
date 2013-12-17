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
package net.madz.lifecycle.syntax.lm.transition;

import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.action.Corrupt;
import net.madz.lifecycle.annotations.action.Recover;
import net.madz.lifecycle.annotations.action.Redo;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.syntax.BaseMetaDataTest;

public class TransitionTestMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface SpecialTranstionStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.Start.class, value = { Running.class })
            static interface Queued {}
            @Functions({ @Function(transition = Transitions.Inactivate.class, value = { InactiveRunning.class }),
                    @Function(transition = Transitions.Stop.class, value = { Stopped.class }) })
            static interface Running {}
            @Functions({ @Function(transition = Transitions.Activate.class, value = { Running.class }),
                    @Function(transition = Transitions.Restart.class, value = { Running.class }),
                    @Function(transition = Transitions.Stop.class, value = { Stopped.class }) })
            static interface InactiveRunning {}
            @End
            static interface Stopped {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Start {}
            @Corrupt
            static interface Inactivate {}
            @Recover
            static interface Activate {}
            @Redo
            static interface Restart {}
            static interface Stop {}
        }
    }
    @LifecycleMeta(SpecialTranstionStateMachine.class)
    static interface PositiveProcess {

        @Transition
        void start();

        @Transition
        void inactivate();

        @Transition
        void activate();

        @Transition
        void stop();

        @Transition
        void restart();

        @StateIndicator
        String getState();
    }
    @LifecycleMeta(SpecialTranstionStateMachine.class)
    static interface NegativeProcess {

        @Transition
        void start(int i);

        @Transition
        void inactivate(int i);

        @Transition
        void activate(int i);

        @Transition
        void stop(int i);

        @Transition
        void restart(int i);

        @StateIndicator
        String getState();
    }
}
