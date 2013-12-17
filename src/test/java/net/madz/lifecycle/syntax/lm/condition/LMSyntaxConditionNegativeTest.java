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
package net.madz.lifecycle.syntax.lm.condition;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.syntax.lm.condition.LMSyntaxConditionMetadata.S1.Conditions.S1_Condition_A;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxConditionNegativeTest extends LMSyntaxConditionMetadata {

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3231_if_condition_reference_invalid() throws VerificationException, NoSuchMethodException, SecurityException {
        @LifecycleRegistry(NLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_REFERENCE_INVALID, NLM_1.class.getMethod("getConditionA"),
                    S1.Transitions.S1_Transition_X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3232_if_multiple_methods_reference_same_condition() throws VerificationException {
        @LifecycleRegistry(NLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_MULTIPLE_METHODS_REFERENCE_SAME_CONDITION, NLM_2.class,
                    S1.Conditions.S1_Condition_A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3230_if_condition_not_covered() throws VerificationException {
        @LifecycleRegistry(NLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_NOT_COVERED, NLM_3.class, S1.class.getName(),
                    S1.class.getName() + ".ConditionSet." + S1_Condition_A.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3233_if_condition_object_does_not_implement_condition_class() throws NoSuchMethodException, SecurityException,
            VerificationException {
        @LifecycleRegistry(ConditionObjectDoesNotImplementConditionClass.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_CONDITION_OBJECT_DOES_NOT_IMPLEMENT_CONDITION_INTERFACE,
                    ConditionObjectDoesNotImplementConditionClass.class.getMethod("getConditionA"), S1_Condition_A.class);
            throw e;
        }
    }
}
