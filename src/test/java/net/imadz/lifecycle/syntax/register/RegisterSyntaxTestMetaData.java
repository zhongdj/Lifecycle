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
package net.imadz.lifecycle.syntax.register;

import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.CorrectStateMachineInheritanceSuperSyntax.Transitions.TransitionTwo;
import net.imadz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.CorrectStateMachineSyntax.Transitions.TransitionOne;
import net.imadz.lifecycle.syntax.register.RegisterSyntaxTestMetaData.IncorrectStateMachineInheritanceSuperSyntax.Transitions.TransitionThree;

public class RegisterSyntaxTestMetaData extends BaseMetaDataTest {

    @StateMachine
    public static interface CorrectStateMachineSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionOne.class, value = StateB.class)
            static interface StateA {}
            @End
            static interface StateB {}
        }
        @EventSet
        static interface Transitions {

            static interface TransitionOne {}
        }
    }
    @LifecycleMeta(CorrectStateMachineSyntax.class)
    protected static class CorrectLifecycleMetaSyntax {

        private String state;

        @Event(TransitionOne.class)
        public void foo() {}

        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String stateName) {
            this.state = stateName;
        }
    }
    protected static class WithoutMetadataAnnotationErrorSyntax {}
    @StateMachine
    public static interface CorrectStateMachineInheritanceSuperSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionTwo.class, value = StateD.class)
            static interface StateC {}
            @End
            static interface StateD {}
        }
        @EventSet
        static interface Transitions {

            static interface TransitionTwo {}
        }
    }
    @StateMachine
    public static interface CorrectStateMachineInheritanceChildSyntax extends CorrectStateMachineInheritanceSuperSyntax {}
    public static interface IncorrectStateMachineInheritanceSuperSyntax {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionThree.class, value = StateF.class)
            static interface StateE {}
            @End
            static interface StateF {}
        }
        @EventSet
        static interface Transitions {

            static interface TransitionThree {}
        }
    }
    @StateMachine
    public static interface IncorrectStateMachineInheritanceChildSyntax extends IncorrectStateMachineInheritanceSuperSyntax {}
    @StateMachine
    public static interface IncorrectStateMachineInheritanceChildWithMultiSuperInterfacesSyntax extends CorrectStateMachineInheritanceSuperSyntax,
            IncorrectStateMachineInheritanceSuperSyntax {}

    public RegisterSyntaxTestMetaData() {
        super();
    }

    protected static interface WrongStateMachineSyntaxWithoutAnnotation {}
    @LifecycleMeta(WrongStateMachineSyntaxWithoutAnnotation.class)
    protected static class WrongLifecycleMetaSyntaxWithStateMachineWithoutAnnotation {}
    public static class NonStateMachineClass {}
    @StateMachine
    public static class IllegalExtendsNonStateMachineClass extends NonStateMachineClass {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = TransitionThree.class, value = StateF.class)
            static interface StateE {}
            @End
            static interface StateF {}
        }
        @EventSet
        static interface Transitions {

            static interface TransitionThree {}
        }
    }
}