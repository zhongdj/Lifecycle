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
package net.imadz.lifecycle.syntax.basic.transition;

import java.io.Serializable;

import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.action.ConditionSet;
import net.imadz.lifecycle.annotations.action.Conditional;
import net.imadz.lifecycle.annotations.action.ConditionalTransition;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.lifecycle.syntax.basic.transition.TransitionSyntaxMetadata.S1.Conditions.S1_Condition_A;
import net.imadz.lifecycle.syntax.basic.transition.TransitionSyntaxMetadata.S1.Conditions.S1_Condition_B;
import net.imadz.lifecycle.syntax.basic.transition.TransitionSyntaxMetadata.S1.States.S1_State_B;
import net.imadz.lifecycle.syntax.basic.transition.TransitionSyntaxMetadata.S1.States.S1_State_C;

public class TransitionSyntaxMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface S1 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.S1_Transition_X.class, value = { S1_State_B.class, S1_State_C.class })
            static interface S1_State_A {}
            @End
            static interface S1_State_B {}
            @End
            static interface S1_State_C {}
        }
        @EventSet
        static interface Transitions {

            @Conditional(condition = S1_Condition_B.class, judger = VolumeMeasurableTransition.class)
            static interface S1_Transition_X {}
        }
        @ConditionSet
        static interface Conditions {

            static interface S1_Condition_A {

                boolean isVolumeLeft();
            }
            static interface S1_Condition_B {}
        }
        public static class VolumeMeasurableTransition implements ConditionalTransition<S1_Condition_A> {

            @Override
            public Class<?> doConditionJudge(S1_Condition_A t) {
                if ( t.isVolumeLeft() ) {
                    return S1_State_B.class;
                } else {
                    return S1_State_C.class;
                }
            }
        }
    }
    @StateMachine
    public static interface NegativeOrder {

        @StateSet
        public static interface States {

            @Initial
            @Function(transition = NegativeOrder.Transitions.Pay.class, value = { Paid.class })
            public static interface New {}
            @Function(transition = NegativeOrder.Transitions.Deliver.class, value = { Delivered.class })
            public static interface Paid {}
            @End
            public static interface Delivered {}
        }
        @EventSet
        public static interface Transitions {

            public static interface Pay extends Serializable {}
            public static interface Deliver {}
        }
    }
    @StateMachine
    public static interface Order {

        @StateSet
        public static interface States {

            @Initial
            @Function(transition = Order.Transitions.Pay.class, value = { Paid.class })
            public static interface New {}
            @Function(transition = Order.Transitions.Deliver.class, value = { Delivered.class })
            public static interface Paid {}
            @End
            public static interface Delivered {}
        }
        @EventSet
        public static interface Transitions {

            public static interface Pay {}
            public static interface Deliver {}
        }
    }
    @StateMachine
    public static interface NegativeBigProductOrder extends Order {

        @StateSet
        public static interface States extends Order.States {

            @Initial
            @Function(transition = NegativeBigProductOrder.Transitions.Pay.class, value = { States.Paid.class })
            public static interface New {}
            @Function(transition = NegativeBigProductOrder.Transitions.Deliver.class, value = { States.Delivered.class })
            public static interface Paid {}
            @End
            public static interface Delivered {}
        }
        @EventSet
        public static interface Transitions extends Order.Transitions {

            public static interface Pay extends NegativeOrder.Transitions.Pay {}
        }
    }
}
