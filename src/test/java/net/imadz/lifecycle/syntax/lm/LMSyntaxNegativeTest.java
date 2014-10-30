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
package net.imadz.lifecycle.syntax.lm;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S2.Events.NS1_Z;
import net.imadz.lifecycle.syntax.lm.LMSyntaxMetadata.S3.Events.S3_Y;
import net.imadz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxNegativeTest extends LMSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_002_3212_if_LM_partial_concreting_transitions_A() throws VerificationException {
        @LifecycleRegistry(NLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_NOT_CONCRETED_IN_LM, NS1_Z.class.getSimpleName(),
                    S2.class.getName(), NLM_1.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3212_if_LM_partial_concreting_transitions_B() throws VerificationException {
        @LifecycleRegistry(NLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_NOT_CONCRETED_IN_LM, NS1_Z.class.getSimpleName(),
                    S2.class.getName(), NLM_2.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3213_if_transition_method_cannot_bind_to_any_transition_via_method_name() throws VerificationException {
        @LifecycleRegistry(NLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_METHOD_NAME_INVALID, S2.class.getName(), "nS1_Xyz",
                    NLM_3.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3211_if_transition_method_reference_transition_beyond_stateMachine() throws VerificationException, NoSuchMethodException,
            SecurityException {
        @LifecycleRegistry(NLM_4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_TRANSITION_METHOD_WITH_INVALID_TRANSITION_REFERENCE, NLM_4.class
                    .getMethod("nS1_X").getAnnotation(Event.class), "nS1_X", NLM_4.class.getName(), S2.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3214_if_some_mandatory_transitions_have_multi_methods() throws VerificationException {
        @LifecycleRegistry(NLM_5.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REDO_CORRUPT_RECOVER_TRANSITION_HAS_ONLY_ONE_METHOD,
                    S3_Y.class.getSimpleName(), "@Redo", S3.class.getName(), NLM_5.class.getName());
            throw e;
        }
    }
}
