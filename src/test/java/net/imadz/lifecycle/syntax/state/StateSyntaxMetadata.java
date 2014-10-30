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

import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Function;
import net.imadz.lifecycle.annotations.Functions;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.action.ConditionSet;
import net.imadz.lifecycle.annotations.action.Conditional;
import net.imadz.lifecycle.annotations.action.ConditionalEvent;
import net.imadz.lifecycle.annotations.state.End;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.States.NCS2_B.CEvents.NCS2_CX;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.Events.NCS2_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS2.Events.NCS2_Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.States.NCS3_B.CEvents.NCS3_CX;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.Events.NCS3_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS3.Events.NCS3_Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.States.NCS4_B.CEvents.NCS4_CX;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.Events.NCS4_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.NCS4.Events.NCS4_Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.States.PCS1_B.CEvents.PCS1_CX;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.Events.PCS1_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.PCS1.Events.PCS1_Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S2.States.D;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S3.Events.Y;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Conditions.CompareWithZero;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S4.States.I;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S4.States.J;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Events.Z;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S4.Utils.ConcreteCondition;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S5.Events.S5_Start;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S5_Super.Events.S5_Super_Start;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S6.Events.S6_Start;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S7.Events.S7_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterEvent.States.SC1_C;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterEvent.States.SC1_B.CEvents.SC1_CX;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterEvent.Events.SC1_X;
import net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.StateMachineWithFunctionInCompositeStateReferencingOuterEvent.Events.SC1_Y;

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
        @EventSet
        static interface Events {

            static interface X {}
        }
    }
    @StateMachine
    static interface S2 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = net.imadz.lifecycle.syntax.state.StateSyntaxMetadata.S1.Events.X.class, value = { D.class })
            static interface C {}
            @End
            static interface D {}
        }
        @EventSet
        static interface Events {

            static interface X {}
        }
    }
    @StateMachine
    static interface S3 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Y.class, value = { F.class, G.class })
            static interface E {}
            @Function(event = Y.class, value = { G.class })
            static interface F {}
            @End
            static interface G {}
        }
        @EventSet
        static interface Events {

            static interface Y {}
        }
    }
    @StateMachine
    static interface S4 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Z.class, value = { I.class, J.class })
            static interface H {}
            @Function(event = Z.class, value = { I.class, J.class })
            static interface I {}
            @End
            static interface J {}
        }
        @EventSet
        static interface Events {

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

            public static class ConcreteCondition implements ConditionalEvent<CompareWithZero> {

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
            @Function(event = S5_Super_Start.class, value = { S5_Super_B.class })
            static interface S5_Super_A {}
            @End
            static interface S5_Super_B {}
        }
        @EventSet
        static interface Events {

            static interface S5_Super_Start {}
        }
    }
    @StateMachine
    static interface S5 extends S5_Super {

        @StateSet
        static interface states extends S5_Super.States {

            @Function(event = S5_Start.class, value = { D.class })
            static interface S5_A {}
        }
        @EventSet
        static interface Events {

            static interface S5_Start {}
        }
    }
    @StateMachine
    static interface S6 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = S6_Start.class, value = { S6_B.class })
            static interface S6_A {}
            @End
            static interface S6_B {}
        }
        @EventSet
        static interface Events {

            static interface S6_Start {}
        }
    }
    @StateMachine
    static interface S7 extends S6 {

        @StateSet
        static interface States extends S6.States {

            @Function(event = S7_X.class, value = { S6_B.class })
            static interface S7_A {}
        }
        @EventSet
        static interface Events extends S6.Events {

            static interface S7_X {}
        }
    }
    @StateMachine
    static interface PCS1 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = PCS1_X.class, value = PCS1_B.class)
            static interface PCS1_A {}
            @CompositeState
            @Function(event = PCS1_Y.class, value = PCS1_C.class)
            static interface PCS1_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(event = PCS1_CX.class, value = PCS1_CB.class)
                    static interface PCS1_CA {}
                    @Function(event = PCS1_CX.class, value = PCS1_CC.class)
                    static interface PCS1_CB {}
                    @End
                    @ShortCut(PCS1_C.class)
                    static interface PCS1_CC {}
                }
                @EventSet
                static interface CEvents {

                    static interface PCS1_CX {}
                }
            }
            @End
            static interface PCS1_C {}
        }
        @EventSet
        static interface Events {

            static interface PCS1_X {}
            static interface PCS1_Y {}
        }
    }
    @StateMachine
    static interface StateMachineWithFunctionInCompositeStateReferencingOuterEvent {

        @StateSet
        static interface States {

            @Initial
            @Function(event = SC1_X.class, value = SC1_B.class)
            static interface SC1_A {}
            @CompositeState
            @Function(event = SC1_Y.class, value = SC1_C.class)
            static interface SC1_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(event = SC1_CX.class, value = SC1_CB.class)
                    static interface SC1_CA {}
                    @Function(event = SC1_X.class, value = SC1_CC.class)
                    static interface SC1_CB {}
                    @End
                    @ShortCut(SC1_C.class)
                    static interface SC1_CC {}
                }
                @EventSet
                static interface CEvents {

                    static interface SC1_CX {}
                }
            }
            @End
            static interface SC1_C {}
        }
        @EventSet
        static interface Events {

            static interface SC1_X {}
            static interface SC1_Y {}
        }
    }
    @StateMachine
    static interface NCS2 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = NCS2_X.class, value = NCS2_B.class)
            static interface NCS2_A {}
            @CompositeState
            @Function(event = NCS2_Y.class, value = NCS2_C.class)
            static interface NCS2_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(event = NCS2_CX.class, value = NCS2_CC.class)
                    static interface NCS2_CA {}
                    @End
                    @ShortCut(SC1_C.class)
                    static interface NCS2_CC {}
                }
                @EventSet
                static interface CEvents {

                    static interface NCS2_CX {}
                }
            }
            @End
            static interface NCS2_C {}
        }
        @EventSet
        static interface Events {

            static interface NCS2_X {}
            static interface NCS2_Y {}
        }
    }
    @StateMachine
    static interface NCS3 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = NCS3_X.class, value = NCS3_B.class)
            static interface NCS3_A {}
            @CompositeState
            @Function(event = NCS3_Y.class, value = NCS3_C.class)
            static interface NCS3_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(event = NCS3_CX.class, value = NCS3_CB.class)
                    static interface NCS3_CA {}
                    @Function(event = NCS3_X.class, value = NCS3_CC.class)
                    static interface NCS3_CB {}
                    @End
                    static interface NCS3_CC {}
                }
                @EventSet
                static interface CEvents {

                    static interface NCS3_CX {}
                }
            }
            @End
            static interface NCS3_C {}
        }
        @EventSet
        static interface Events {

            static interface NCS3_X {}
            static interface NCS3_Y {}
        }
    }
    @StateMachine
    static interface NCS4 {

        @StateSet
        static interface States {

            @Initial
            @Function(event = NCS4_X.class, value = NCS4_B.class)
            static interface NCS4_A {}
            @CompositeState
            @Function(event = NCS4_Y.class, value = NCS4_C.class)
            static interface NCS4_B {

                @StateSet
                static interface CStates {

                    @Initial
                    @Function(event = NCS4_CX.class, value = NCS4_CB.class)
                    static interface NCS4_CA {}
                    @Function(event = NCS4_X.class, value = NCS4_CC.class)
                    static interface NCS4_CB {}
                    @ShortCut(NCS4_C.class)
                    static interface NCS4_CC {}
                    @End
                    @ShortCut(NCS4_C.class)
                    static interface NCS4_CD {}
                }
                @EventSet
                static interface CEvents {

                    static interface NCS4_CX {}
                }
            }
            @End
            static interface NCS4_C {}
        }
        @EventSet
        static interface Events {

            static interface NCS4_X {}
            static interface NCS4_Y {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Event {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(event = Multiple_Function_Referring_Same_Event.Events.X.class, value = Ended.class),
                    @Function(event = Multiple_Function_Referring_Same_Event.Events.X.class, value = Ended.class) })
            static interface Created {}
            @End
            static interface Ended {}
        }
        @EventSet
        static interface Events {

            static interface X {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Event_Super {

        @StateSet
        static interface States {

            @Initial
            @Functions({ @Function(event = Multiple_Function_Referring_Same_Event_Super.Events.X.class, value = Ended.class) })
            static interface Created {}
            @End
            static interface Ended {}
        }
        @EventSet
        static interface Events {

            static interface X {}
        }
    }
    @StateMachine
    static interface Multiple_Function_Referring_Same_Event_Child extends Multiple_Function_Referring_Same_Event_Super {

        @StateSet
        static interface States extends Multiple_Function_Referring_Same_Event_Super.States {

            @Functions({ @Function(event = Multiple_Function_Referring_Same_Event_Super.Events.X.class, value = Ended.class) })
            static interface Created extends Multiple_Function_Referring_Same_Event_Super.States.Created {}
        }
    }
    @StateMachine
    static interface State_Overriding_Function_Referring_Same_Event_With_Super_State extends Multiple_Function_Referring_Same_Event_Super {

        @StateSet
        static interface States extends Multiple_Function_Referring_Same_Event_Super.States {

            @Initial
            @Functions({ @Function(event = Multiple_Function_Referring_Same_Event_Super.Events.X.class, value = Ended.class) })
            @LifecycleOverride
            static interface Created extends Multiple_Function_Referring_Same_Event_Super.States.Created {}
        }
    }
    @StateMachine
    static interface CorrectBase {

        @StateSet
        static interface States {

            @Initial
            @Function(event = CorrectBase.Events.X.class, value = CorrectBase.States.B.class)
            static interface A {}
            @End
            static interface B {}
        }
        @EventSet
        static interface Events {

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
            @Function(event = CorrectBase.Events.X.class, value = CorrectBase.States.B.class)
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
            @Function(event = Events.X.class, value = { States.Super_S2.class })
            static interface Super_S1 {}
            @Function(event = Events.Y.class, value = { States.Super_S3.class })
            @CompositeState
            static interface Super_S2 {

                @StateSet
                static interface CompositeStates {

                    @Initial
                    @Function(event = CompositeEvents.Super_S2_X.class, value = { Super_S2_S2.class })
                    static interface Super_S2_S1 {}
                    @Function(event = CompositeEvents.Super_S2_Y.class, value = { Super_S2_S3.class })
                    static interface Super_S2_S2 {}
                    @End
                    @ShortCut(value = Super_S3.class)
                    static interface Super_S2_S3 {}
                }
                @EventSet
                static interface CompositeEvents {

                    static interface Super_S2_X {}
                    static interface Super_S2_Y {}
                }
            }
            @Function(event = Events.Y.class, value = { Super_S4.class })
            static interface Super_S3 {}
            @End
            static interface Super_S4 {}
        }
        @EventSet
        static interface Events {

            static interface X {}
            static interface Y {}
            static interface Z {}
        }
    }
    @StateMachine
    static interface FunctionInCompositeStateReferencingEventInSuper extends SuperStateMachine {

        @StateSet
        static interface States extends SuperStateMachine.States {

            @CompositeState
            static interface Child_S2 extends SuperStateMachine.States.Super_S2 {

                @StateSet
                static interface Child_S2_States extends SuperStateMachine.States.Super_S2.CompositeStates {

                    @Function(event = SuperStateMachine.States.Super_S2.CompositeEvents.Super_S2_X.class, value = { Child_S2_S2.class })
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
                    @Functions({ @Function(event = SuperStateMachine.Events.Z.class, value = { Child_S3_S2.class }),
                            @Function(event = ChildEvents.Child_S3_X.class, value = { Child_S3_S3.class }) })
                    static interface Child_S3_S1 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Child_S3_S2 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Child_S3_S3 {}
                }
                @EventSet
                static interface ChildEvents {

                    static interface Child_S3_X {}
                }
            }
        }
    }
    @StateMachine
    static interface CompositeStateMachineEventReferencedFromAnotherCompositeStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(event = Events.X.class, value = { S2.class })
            static interface S1 {}
            @Function(event = Events.Y.class, value = { S3.class })
            @CompositeState
            static interface S2 {

                @StateSet
                static interface S2_States {

                    @Initial
                    @Function(event = S3.S3_Events.S3_X.class, value = { S2_B.class })
                    static interface S2_A {}
                    @End
                    @ShortCut(value = States.S3.class)
                    static interface S2_B {}
                }
                @EventSet
                static interface S2_Events {

                    static interface S2_X {}
                }
            }
            @CompositeState
            static interface S3 {

                @StateSet
                static interface S3_States {

                    @Initial
                    @Function(event = S3.S3_Events.S3_X.class, value = { S3_B.class })
                    static interface S3_A {}
                    @End
                    @ShortCut(value = States.S4.class)
                    static interface S3_B {}
                }
                @EventSet
                static interface S3_Events {

                    static interface S3_X {}
                }
            }
            @End
            static interface S4 {}
        }
        @EventSet
        static interface Events {

            static interface X {}
            static interface Y {}
        }
    }
    @StateMachine
    static interface CompositeStateMachineEventReferenceFromSuperNonExtendedCompositeStateMachine extends SuperStateMachine {

        @StateSet
        static interface States extends SuperStateMachine.States {

            @CompositeState
            static interface Composite_S1 extends SuperStateMachine.States.Super_S3 {

                @StateSet
                static interface InnerStates {

                    @Initial
                    @Function(event = InnerEvents.Inner_X.class, value = { Inner_S2.class })
                    static interface Inner_S1 {}
                    @Function(event = SuperStateMachine.States.Super_S2.CompositeEvents.Super_S2_X.class, value = { Inner_S3.class })
                    static interface Inner_S2 {}
                    @End
                    @ShortCut(value = SuperStateMachine.States.Super_S4.class)
                    static interface Inner_S3 {}
                }
                @EventSet
                static interface InnerEvents {

                    static interface Inner_X {}
                }
            }
        }
    }
}
