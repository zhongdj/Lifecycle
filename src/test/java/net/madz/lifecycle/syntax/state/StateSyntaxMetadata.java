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
package net.madz.lifecycle.syntax.state;

import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.action.ConditionSet;
import net.madz.lifecycle.annotations.action.Conditional;
import net.madz.lifecycle.annotations.action.ConditionalTransition;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.States.NCS2_B.CTransitions.NCS2_CX;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.Transitions.NCS2_X;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.Transitions.NCS2_Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.States.NCS3_B.CTransitions.NCS3_CX;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.Transitions.NCS3_X;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.Transitions.NCS3_Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.States.NCS4_B.CTransitions.NCS4_CX;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.Transitions.NCS4_X;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.Transitions.NCS4_Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterTransition.States.SC1_B.CTransitions.SC1_CX;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterTransition.States.SC1_C;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterTransition.Transitions.SC1_X;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterTransition.Transitions.SC1_Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.States.PCS1_B.CTransitions.PCS1_CX;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.Transitions.PCS1_X;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.Transitions.PCS1_Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.D;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S3.Transitions.Y;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Conditions.CompareWithZero;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S4.States.I;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S4.States.J;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Transitions.Z;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Utils.ConcreteCondition;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S5.Transitions.S5_Start;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S5_Super.Transitions.S5_Super_Start;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S6.Transitions.S6_Start;
import net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S7.Transitions.S7_X;

public class StateSyntaxMetadata extends BaseMetaDataTest {

    @StateMachine
    static interface S1 {

        @StateSet
        static interface States {

            @Initial
            static interface A {}
            @End
            static interface B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface S2 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = net.madz.lifecycle.syntax.state.StateSyntaxMetadata.S1.Transitions.X.class, value = { D.class })
            static interface C {}
            @End
            static interface D {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface S3 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Y.class, value = { F.class, G.class })
            static interface E {}
            @Function(transition = Y.class, value = { G.class })
            static interface F {}
            @End
            static interface G {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Y {}
        }
    }
    @StateMachine
    static interface S4 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Z.class, value = { I.class, J.class })
            static interface H {}
            @Function(transition = Z.class, value = { I.class, J.class })
            static interface I {}
            @End
            static interface J {}
        }
        @TransitionSet
        static interface Transitions {

            @Conditional(judger = ConcreteCondition.class, condition = CompareWithZero.class)
            static interface Z {}
        }
        @ConditionSet
        public static interface Conditions {

            public static interface CompareWithZero {

                int intValue();
            }
        }
        public static class Utils {

            public static class ConcreteCondition implements ConditionalTransition<CompareWithZero> {

                @Override
                public Class<?> doConditionJudge(CompareWithZero t) {
                    if ( t.intValue() > 0 ) {
                        return I.class;
                    } else {
                        return J.class;
                    }
                }
            }
        }
    }
    @StateMachine
    static interface S5_Super {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = S5_Super_Start.class, value = { S5_Super_B.class })
            static interface S5_Super_A {}
            @End
            static interface S5_Super_B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface S5_Super_Start {}
        }
    }
    @StateMachine
    static interface S5 extends S5_Super {

        @StateSet
        static interface states extends S5_Super.States {

            @Function(transition = S5_Start.class, value = { D.class })
            static interface S5_A {}
        }
        @TransitionSet
        static interface Transitions {

            static interface S5_Start {}
        }
    }
    @StateMachine
    static interface S6 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = S6_Start.class, value = { S6_B.class })
            static interface S6_A {}
            @End
            static interface S6_B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface S6_Start {}
        }
    }
    @StateMachine
    static interface S7 extends S6 {

        @StateSet
        static interface States extends S6.States {

            @Function(transition = S7_X.class, value = { S6_B.class })
            static interface S7_A {}
        }
        @TransitionSet
        static interface Transitions extends S6.Transitions {

            static interface S7_X {}
        }
    }
    @StateMachine
    static interface PCS1 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = PCS1_X.class, value = PCS1_B.class)
            static interface PCS1_A {}
            @CompositeState
            @Function(transition = PCS1_Y.class, value = PCS1_C.class)
            static interface PCS1_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = PCS1_CX.class, value = PCS1_CB.class)
                    static interface PCS1_CA {}
                    @Function(transition = PCS1_CX.class, value = PCS1_CC.class)
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
    @StateMachine
    static interface StateMachineWithFunctionInCompositeStateReferencingOuterTransition {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = SC1_X.class, value = SC1_B.class)
            static interface SC1_A {}
            @CompositeState
            @Function(transition = SC1_Y.class, value = SC1_C.class)
            static interface SC1_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = SC1_CX.class, value = SC1_CB.class)
                    static interface SC1_CA {}
                    @Function(transition = SC1_X.class, value = SC1_CC.class)
                    static interface SC1_CB {}
                    @End
                    @ShortCut(SC1_C.class)
                    static interface SC1_CC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface SC1_CX {}
                }
            }
            @End
            static interface SC1_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface SC1_X {}
            static interface SC1_Y {}
        }
    }
    @StateMachine
    static interface NCS2 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NCS2_X.class, value = NCS2_B.class)
            static interface NCS2_A {}
            @CompositeState
            @Function(transition = NCS2_Y.class, value = NCS2_C.class)
            static interface NCS2_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = NCS2_CX.class, value = NCS2_CC.class)
                    static interface NCS2_CA {}
                    @End
                    @ShortCut(SC1_C.class)
                    static interface NCS2_CC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface NCS2_CX {}
                }
            }
            @End
            static interface NCS2_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NCS2_X {}
            static interface NCS2_Y {}
        }
    }
    @StateMachine
    static interface NCS3 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NCS3_X.class, value = NCS3_B.class)
            static interface NCS3_A {}
            @CompositeState
            @Function(transition = NCS3_Y.class, value = NCS3_C.class)
            static interface NCS3_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = NCS3_CX.class, value = NCS3_CB.class)
                    static interface NCS3_CA {}
                    @Function(transition = NCS3_X.class, value = NCS3_CC.class)
                    static interface NCS3_CB {}
                    @End
                    static interface NCS3_CC {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface NCS3_CX {}
                }
            }
            @End
            static interface NCS3_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NCS3_X {}
            static interface NCS3_Y {}
        }
    }
    @StateMachine
    static interface NCS4 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = NCS4_X.class, value = NCS4_B.class)
            static interface NCS4_A {}
            @CompositeState
            @Function(transition = NCS4_Y.class, value = NCS4_C.class)
            static interface NCS4_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(transition = NCS4_CX.class, value = NCS4_CB.class)
                    static interface NCS4_CA {}
                    @Function(transition = NCS4_X.class, value = NCS4_CC.class)
                    static interface NCS4_CB {}
                    @ShortCut(NCS4_C.class)
                    static interface NCS4_CC {}
                    @End
                    @ShortCut(NCS4_C.class)
                    static interface NCS4_CD {}
                }
                @TransitionSet
                static interface CTransitions {

                    static interface NCS4_CX {}
                }
            }
            @End
            static interface NCS4_C {}
        }
        @TransitionSet
        static interface Transitions {

            static interface NCS4_X {}
            static interface NCS4_Y {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Transition {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(transition = Multiple_Function_Referring_Same_Transition.Transitions.X.class, value = Ended.class),
                    @Function(transition = Multiple_Function_Referring_Same_Transition.Transitions.X.class, value = Ended.class) })
            static interface Created {}
            @End
            static interface Ended {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Transition_Super {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(transition = Multiple_Function_Referring_Same_Transition_Super.Transitions.X.class, value = Ended.class) })
            static interface Created {}
            @End
            static interface Ended {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Transition_Child extends Multiple_Function_Referring_Same_Transition_Super {

        @StateSet
        static interface States extends Multiple_Function_Referring_Same_Transition_Super.States {

            @Functions({ @Function(transition = Multiple_Function_Referring_Same_Transition_Super.Transitions.X.class, value = Ended.class) })
            static interface Created extends Multiple_Function_Referring_Same_Transition_Super.States.Created {}
        }
    }
    @StateMachine
    static interface State_Overriding_Function_Referring_Same_Transition_With_Super_State extends Multiple_Function_Referring_Same_Transition_Super {

        @StateSet
        static interface States extends Multiple_Function_Referring_Same_Transition_Super.States {

            @Initial
            @Functions({ @Function(transition = Multiple_Function_Referring_Same_Transition_Super.Transitions.X.class, value = Ended.class) })
            @LifecycleOverride
            static interface Created extends Multiple_Function_Referring_Same_Transition_Super.States.Created {}
        }
    }
    @StateMachine
    static interface CorrectBase {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = CorrectBase.Transitions.X.class, value = CorrectBase.States.B.class)
            static interface A {}
            @End
            static interface B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
        }
    }
    @StateMachine
    static interface NegativeOverridesWithoutSuperClass extends CorrectBase {

        public static final String errorCode = SyntaxErrors.STATE_OVERRIDES_WITHOUT_SUPER_CLASS;

        @StateSet
        static interface States extends CorrectBase.States {

            @Initial
            @LifecycleOverride
            @Function(transition = CorrectBase.Transitions.X.class, value = CorrectBase.States.B.class)
            static interface A {}
        }
    }
    @StateMachine
    static interface NegativeOverridesMissingInitial extends CorrectBase {

        public static final String errorCode = SyntaxErrors.STATESET_MULTIPLE_INITAL_STATES;

        @StateSet
        static interface States extends CorrectBase.States {

            @LifecycleOverride
            // Should define another @Initial state or add @Initial to this
            // @Initial State
            static interface A extends CorrectBase.States.A {}
        }
    }
    @StateMachine
    static interface SuperStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.X.class, value = { States.Super_S2.class })
            static interface Super_S1 {}
            @Function(transition = Transitions.Y.class, value = { States.Super_S3.class })
            @CompositeState
            static interface Super_S2 {

                @StateSet
                static interface CompositeStates {

                    @Initial
                    @Function(transition = CompositeTransitions.Super_S2_X.class, value = { Super_S2_S2.class })
                    static interface Super_S2_S1 {}
                    @Function(transition = CompositeTransitions.Super_S2_Y.class, value = { Super_S2_S3.class })
                    static interface Super_S2_S2 {}
                    @End
                    @ShortCut(value = Super_S3.class)
                    static interface Super_S2_S3 {}
                }
                @TransitionSet
                static interface CompositeTransitions {

                    static interface Super_S2_X {}
                    static interface Super_S2_Y {}
                }
            }
            @Function(transition = Transitions.Y.class, value = { Super_S4.class })
            static interface Super_S3 {}
            @End
            static interface Super_S4 {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
            static interface Y {}
            static interface Z {}
        }
    }
    @StateMachine
    static interface FunctionInCompositeStateReferencingTransitionInSuper extends SuperStateMachine {

        @StateSet
        static interface States extends SuperStateMachine.States {

            @CompositeState
            static interface Child_S2 extends SuperStateMachine.States.Super_S2 {

                @StateSet
                static interface Child_S2_States extends SuperStateMachine.States.Super_S2.CompositeStates {

                    @Function(transition = SuperStateMachine.States.Super_S2.CompositeTransitions.Super_S2_X.class, value = { Child_S2_S2.class })
                    static interface Child_S2_S1 extends SuperStateMachine.States.Super_S2.CompositeStates.Super_S2_S2 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S3.class)
                    static interface Child_S2_S2 {}
                }
            }
            @CompositeState
            static interface Child_S3 extends SuperStateMachine.States.Super_S3 {

                @StateSet
                static interface ChildStates {

                    @Initial
                    @Functions({ @Function(transition = SuperStateMachine.Transitions.Z.class, value = { Child_S3_S2.class }),
                            @Function(transition = ChildTransitions.Child_S3_X.class, value = { Child_S3_S3.class }) })
                    static interface Child_S3_S1 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Child_S3_S2 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Child_S3_S3 {}
                }
                @TransitionSet
                static interface ChildTransitions {

                    static interface Child_S3_X {}
                }
            }
        }
    }
    @StateMachine
    static interface CompositeStateMachineTransitionReferencedFromAnotherCompositeStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.X.class, value = { S2.class })
            static interface S1 {}
            @Function(transition = Transitions.Y.class, value = { S3.class })
            @CompositeState
            static interface S2 {

                @StateSet
                static interface S2_States {

                    @Initial
                    @Function(transition = S3.S3_Transitions.S3_X.class, value = { S2_B.class })
                    static interface S2_A {}
                    @End
                    @ShortCut(value = States.S3.class)
                    static interface S2_B {}
                }
                @TransitionSet
                static interface S2_Transitions {

                    static interface S2_X {}
                }
            }
            @CompositeState
            static interface S3 {

                @StateSet
                static interface S3_States {

                    @Initial
                    @Function(transition = S3.S3_Transitions.S3_X.class, value = { S3_B.class })
                    static interface S3_A {}
                    @End
                    @ShortCut(value = States.S4.class)
                    static interface S3_B {}
                }
                @TransitionSet
                static interface S3_Transitions {

                    static interface S3_X {}
                }
            }
            @End
            static interface S4 {}
        }
        @TransitionSet
        static interface Transitions {

            static interface X {}
            static interface Y {}
        }
    }
    @StateMachine
    static interface CompositeStateMachineTransitionReferenceFromSuperNonExtendedCompositeStateMachine extends SuperStateMachine {

        @StateSet
        static interface States extends SuperStateMachine.States {

            @CompositeState
            static interface Composite_S1 extends SuperStateMachine.States.Super_S3 {

                @StateSet
                static interface InnerStates {

                    @Initial
                    @Function(transition = InnerTransitions.Inner_X.class, value = { Inner_S2.class })
                    static interface Inner_S1 {}
                    @Function(transition = SuperStateMachine.States.Super_S2.CompositeTransitions.Super_S2_X.class, value = { Inner_S3.class })
                    static interface Inner_S2 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Inner_S3 {}
                }
                @TransitionSet
                static interface InnerTransitions {

                    static interface Inner_X {}
                }
            }
        }
    }
}
