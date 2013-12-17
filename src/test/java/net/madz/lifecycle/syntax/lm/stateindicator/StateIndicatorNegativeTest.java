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
package net.madz.lifecycle.syntax.lm.stateindicator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class StateIndicatorNegativeTest extends StateIndicatorMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_002_3300_if_no_default_state_indicator() throws VerificationException {
        @LifecycleRegistry(StateIndicatorMetadata.NNoDefaultStateIndicatorInterface.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_FIND_DEFAULT_AND_SPECIFIED_STATE_INDICATOR,
                    NNoDefaultStateIndicatorInterface.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3301_if_default_state_indicator_with_public_state_setter_class_impl() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NDefaultPublicStateSetterClass.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_SETTER,
                    NDefaultPublicStateSetterClass.class.getDeclaredMethod("setState", String.class));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3301_if_default_state_indicator_with_public_state_setter_in_interface() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NDefaultStateIndicatorInterface.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_SETTER,
                    NDefaultStateIndicatorInterface.class.getDeclaredMethod("setState", String.class));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3302_if_field_access_expose_state_indicator() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NPublicStateFieldClass.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_FIELD,
                    NPublicStateFieldClass.class.getDeclaredField("state"));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3301_if_state_indicator_with_public_state_setter() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NPublicStateSetterClass.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_SETTER,
                    NPublicStateSetterClass.class.getDeclaredMethod("setState", String.class));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3301_if_state_indicator_with_public_state_setter_defined_in_interface() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NPublicStateIndicatorInterface.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CANNOT_EXPOSE_STATE_INDICATOR_SETTER,
                    NPublicStateIndicatorInterface.class.getDeclaredMethod("setState", String.class));
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3303_if_state_indicator_setter_not_found_class_impl() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NStateIndicatorSetterNotFound.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_SETTER_NOT_FOUND, NStateIndicatorSetterNotFound.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3304_if_state_indicator_converter_not_found_interface_impl() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NNeedConverter.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CONVERTER_NOT_FOUND, NNeedConverter.class,
                    Integer.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3305_if_state_indicator_converter_invalid_interface_impl() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NStateIndicatorConverterInvalid.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_CONVERTER_INVALID,
                    NStateIndicatorConverterInvalid.class, Object.class, StateConverterImpl.class, Integer.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3306_if_multiple_state_indicator_defined() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NegativeMultipleStateIndicator.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_MULTIPLE_STATE_INDICATOR_ERROR,
                    NegativeMultipleStateIndicator.class);
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public void should_throw_002_3306_if_multiple_state_indicator_defined_through_hierarchy() throws Exception {
        @LifecycleRegistry(StateIndicatorMetadata.NegativeMultipleStateIndicatorChild.class)
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {
                super();
            }
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.STATE_INDICATOR_MULTIPLE_STATE_INDICATOR_ERROR,
                    NegativeMultipleStateIndicatorChild.class);
            throw e;
        }
    }
}
