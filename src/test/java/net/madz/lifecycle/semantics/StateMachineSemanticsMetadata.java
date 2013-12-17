package net.madz.lifecycle.semantics;

import net.madz.common.ConsoleLoggingTestBase;
import net.madz.lifecycle.annotations.CompositeState;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.annotations.state.ShortCut;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Transitions.S1_X;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Transitions.S1_Y;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S1.Transitions.S1_Z;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S2.States.S2_B.S2_B_Transitions.S2_B_Transitions_X;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S2.Transitions.S2_Z;
import net.madz.lifecycle.semantics.StateMachineSemanticsMetadata.S3.Transitions.S3_Z;

public class StateMachineSemanticsMetadata extends ConsoleLoggingTestBase {

    @StateMachine
    public static interface S1 {

        @StateSet
        public static interface States {

            @Initial
            @Function(transition = S1_X.class, value = { S1_B.class })
            public static interface S1_A {}
            @Function(transition = S1_Y.class, value = { S1_C.class })
            public static interface S1_B {}
            @Function(transition = S1_Z.class, value = { S1_D.class })
            public static interface S1_C {}
            @End
            public static interface S1_D {}
        }
        @TransitionSet
        public static interface Transitions {

            public static interface S1_X {}
            public static interface S1_Y {}
            public static interface S1_Z {}
        }
    }
    @StateMachine
    public static interface S2 extends S1 {

        @StateSet
        public static interface States extends S1.States {

            @Initial
            @LifecycleOverride
            @Function(transition = S2_Z.class, value = { S1_C.class })
            public static interface S2_A extends S1.States.S1_A {}
            @LifecycleOverride
            @CompositeState
            public static interface S2_B extends S1.States.S1_B {

                @StateSet
                public static interface S2_B_States {

                    @Initial
                    @Function(transition = S2_B_Transitions_X.class, value = { S2_B_States_B.class })
                    public static interface S2_B_States_A {}
                    @End
                    @ShortCut(S1_C.class)
                    public static interface S2_B_States_B {}
                }
                @TransitionSet
                public static interface S2_B_Transitions {

                    public static interface S2_B_Transitions_X {}
                }
            }
        }
        @TransitionSet
        public static interface Transitions extends S1.Transitions {

            public static interface S2_Z {}
        }
    }
    @StateMachine
    public static interface S3 extends S2 {

        @StateSet
        public static interface States extends S2.States {

            @Initial
            @LifecycleOverride
            @Function(transition = S3_Z.class, value = { S1_D.class })
            public static interface S3_A extends S2_A {}
            @CompositeState
            public static interface S3_E extends S2.States.S2_B {}
        }
        @TransitionSet
        public static interface Transitions extends S2.Transitions {

            public static interface S3_Z {}
        }
    }
}
