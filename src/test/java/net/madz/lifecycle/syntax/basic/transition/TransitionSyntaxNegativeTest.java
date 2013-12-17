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
package net.madz.lifecycle.syntax.basic.transition;

import java.io.Serializable;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class TransitionSyntaxNegativeTest extends TransitionSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public final void should_throw_exception_002_2503_if_conditional_condition_class_does_not_match_judger_class_type_parameter() throws VerificationException {
        @LifecycleRegistry(S1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.TRANSITION_CONDITIONAL_CONDITION_NOT_MATCH_JUDGER,
                    TransitionSyntaxMetadata.S1.Transitions.S1_Transition_X.class, TransitionSyntaxMetadata.S1.Conditions.S1_Condition_B.class,
                    TransitionSyntaxMetadata.S1.VolumeMeasurableTransition.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_exception_002_2504_if_transition_extends_interface_but_parent_state_machine_has_no_super() throws VerificationException {
        @LifecycleRegistry(NegativeOrder.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.TRANSITION_ILLEGAL_EXTENTION, NegativeOrder.Transitions.Pay.class,
                    Serializable.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_exception_002_2505_if_extended_transition_can_not_be_found_in_super_statemachine() throws VerificationException {
        @LifecycleRegistry(NegativeBigProductOrder.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.TRANSITION_EXTENED_TRANSITION_CAN_NOT_FOUND_IN_SUPER_STATEMACHINE,
                    NegativeBigProductOrder.Transitions.Pay.class, NegativeOrder.Transitions.Pay.class, Order.class);
            throw e;
        }
    }
}
