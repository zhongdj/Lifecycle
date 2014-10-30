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
package net.imadz.lifecycle.syntax.state;

import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.imadz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.States.NCS2_B.CStates.NCS2_CC;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.States.NCS3_B.CStates.NCS3_CC;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.States.NCS4_B.CStates.NCS4_CC;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S1.States.A;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.C;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.D;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S3.States.E;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S3.Events.Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S5.states.S5_A;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterEvent.States.SC1_C;
import net.imadz.verification.VerificationException;

import org.junit.Test;

public class StateSyntaxNegativeTest extends StateSyntaxMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_002_2615_if_non_final_state_without_functions() throws VerificationException {
        @LifecycleRegistry(S1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_NON_FINAL_WITHOUT_FUNCTIONS, A.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_state_function__with_invalid_event() throws VerificationException {
        @LifecycleRegistry(S2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    C.class.getAnnotation(Function.class), C.class, net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S1.Events.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2611_if_state_function_with_invalid_conditional_event_without_conditional_annotation() throws VerificationException {
        @LifecycleRegistry(S3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_CONDITIONAL_TRANSITION_WITHOUT_CONDITION,
                    E.class.getAnnotation(Function.class), E.class.getName(), Y.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2700_if_state_function_with_invalid_next_state() throws VerificationException {
        @LifecycleRegistry(S5.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_NEXT_STATESET_OF_FUNCTION_INVALID,
                    S5_A.class.getAnnotation(Function.class), S5_A.class.getName(), S5.class.getName(), D.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2803_if_shortcut_referencing_state_beyond_scope() throws VerificationException {
        @LifecycleRegistry(NCS2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_STATE_INVALID,
                    NCS2_CC.class.getAnnotation(ShortCut.class), NCS2_CC.class, SC1_C.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2802_if_composite_final_without_shortcut() throws VerificationException {
        @LifecycleRegistry(NCS3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_FINAL_STATE_WITHOUT_SHORTCUT, NCS3_CC.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2801_if_shortcut_without_end_annotation() throws VerificationException {
        @LifecycleRegistry(NCS4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.COMPOSITE_STATEMACHINE_SHORTCUT_WITHOUT_END, NCS4_CC.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2616_if_multiple_functions_referring_same_event() throws VerificationException {
        @LifecycleRegistry(Multiple_Function_Referring_Same_Event.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_TRANSITION,
                    Multiple_Function_Referring_Same_Event.States.Created.class, Multiple_Function_Referring_Same_Event.Events.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2616_if_inheritance_multiple_functions_referring_same_event() throws VerificationException {
        @LifecycleRegistry(Multiple_Function_Referring_Same_Event_Child.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_DEFINED_MULTIPLE_FUNCTION_REFERRING_SAME_TRANSITION,
                    Multiple_Function_Referring_Same_Event_Child.States.Created.class,
                    Multiple_Function_Referring_Same_Event_Super.Events.X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2617_if_state_overrides_without_super_class() throws VerificationException {
        @LifecycleRegistry(NegativeOverridesWithoutSuperClass.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_OVERRIDES_WITHOUT_SUPER_CLASS,
                    NegativeOverridesWithoutSuperClass.States.A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2618_if_missing_initial_state_when_intial_state_overrided() throws VerificationException {
        @LifecycleRegistry(NegativeOverridesMissingInitial.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATESET_WITHOUT_INITAL_STATE_AFTER_OVERRIDING_SUPER_INITIAL_STATE,
                    NegativeOverridesMissingInitial.States.A.class, CorrectBase.States.A.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_composite_statemachine_event_referenced_from_another_composite_state_machine() throws VerificationException {
        @LifecycleRegistry(CompositeStateMachineEventReferencedFromAnotherCompositeStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    CompositeStateMachineEventReferencedFromAnotherCompositeStateMachine.States.S2.S2_States.S2_A.class.getAnnotation(Function.class),
                    CompositeStateMachineEventReferencedFromAnotherCompositeStateMachine.States.S2.S2_States.S2_A.class,
                    CompositeStateMachineEventReferencedFromAnotherCompositeStateMachine.States.S3.S3_Events.S3_X.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_2610_if_composite_statemachine_event_referenced_from_super_non_extended_composite_state_machine()
            throws VerificationException {
        @LifecycleRegistry(CompositeStateMachineEventReferenceFromSuperNonExtendedCompositeStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.FUNCTION_INVALID_TRANSITION_REFERENCE,
                    CompositeStateMachineEventReferenceFromSuperNonExtendedCompositeStateMachine.States.Composite_S1.InnerStates.Inner_S2.class
                            .getAnnotation(Function.class),
                    CompositeStateMachineEventReferenceFromSuperNonExtendedCompositeStateMachine.States.Composite_S1.InnerStates.Inner_S2.class,
                    SuperStateMachine.States.Super_S2.CompositeEvents.Super_S2_X.class);
            throw e;
        }
    }
}
