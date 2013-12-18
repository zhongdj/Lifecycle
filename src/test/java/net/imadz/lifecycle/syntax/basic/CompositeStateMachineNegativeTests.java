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
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.TransitionSet;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.verification.VerificationException;

import org.junit.Ignore;
import org.junit.Test;

public class CompositeStateMachineNegativeTests extends BaseMetaDataTest {

    @StateMachine
    static interface CompositeExtendsOwningStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = CompositeExtendsOwningStateMachine.Transitions.PCS1_X.class, value = PCS1_B.class)
            static interface PCS1_A {}
            @CompositeState
            @Function(transition = CompositeExtendsOwningStateMachine.Transitions.PCS1_Y.class, value = PCS1_C.class)
            static interface PCS1_B extends CompositeExtendsOwningStateMachine {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = CompositeExtendsOwningStateMachine.States.PCS1_B.CTransitions.PCS1_CX.class, value = PCS1_CB.class)
                    static interface PCS1_CA {}
                    @Function(transition = CompositeExtendsOwningStateMachine.States.PCS1_B.CTransitions.PCS1_CX.class, value = PCS1_CC.class)
                    static interface PCS1_CB {}
                    @End
                    @ShortCut(PCS1_C.class)
                    static interface PCS1_CC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface PCS1_CX {}
                }
            }
            @End
            static interface PCS1_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface PCS1_X {}
            static interface PCS1_Y {}
        }
    }

    @Test(expected = VerificationException.class)
    @Ignore
    public void should_throw_exception_002_2804_if_composite_state_extends_owning_stateMachine() throws VerificationException {
        @LifecycleRegistry(CompositeExtendsOwningStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_CANNOT_EXTENDS_OWNING_STATEMACHINE,
                    CompositeExtendsOwningStateMachine.States.PCS1_B.class);
            throw e;
        }
    }
}
