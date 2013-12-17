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

import java.util.HashMap;
import java.util.Iterator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Activate;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Inactivate;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Restart;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;

import org.junit.Test;

public class TransitionNegativeTests extends TransitionTestMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_exception_if_transition_method_of_special_transition_type_has_parameter() throws VerificationException, Throwable {
        @LifecycleRegistry(NegativeProcess.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            final Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
            final HashMap<String, VerificationFailure> errors = new HashMap<>();
            VerificationFailure next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            assertFailure(errors.get("Inactivate"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("inactivate", Integer.TYPE), Inactivate.class.getSimpleName(), TransitionTypeEnum.Corrupt);
            assertFailure(errors.get("Activate"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("activate", Integer.TYPE), Activate.class.getSimpleName(), TransitionTypeEnum.Recover);
            assertFailure(errors.get("Restart"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("restart", Integer.TYPE), Restart.class.getSimpleName(), TransitionTypeEnum.Redo);
            throw e;
        }
    }
}
