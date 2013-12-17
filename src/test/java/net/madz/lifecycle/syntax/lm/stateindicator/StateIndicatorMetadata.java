package net.madz.lifecycle.syntax.lm.stateindicator;

import net.madz.lifecycle.StateConverter;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.Converter;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.annotations.state.LifecycleOverride;
import net.madz.lifecycle.syntax.BaseMetaDataTest;
import net.madz.lifecycle.syntax.lm.stateindicator.StateIndicatorMetadata.PS1.Transitions.S1_X;

public class StateIndicatorMetadata extends BaseMetaDataTest {

    public static class StateConverterImpl implements StateConverter<Integer> {

        @Override
        public String toState(Integer t) {
            switch (t.intValue()) {
                case 1:
                    return PS1.States.S1_A.class.getSimpleName();
                case 2:
                    return PS1.States.S1_B.class.getSimpleName();
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public Integer fromState(String state) {
            switch (state) {
                case "S1_A":
                    return 1;
                case "S1_B":
                    return 2;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    @StateMachine
    static interface PS1 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.S1_X.class, value = { S1_B.class })
            static interface S1_A {}
            @End
            static interface S1_B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface S1_X {}
        }
    }
    // ///////////////////////////////////////////////////////////
    // Default State Indicator tests
    // ///////////////////////////////////////////////////////////
    @LifecycleMeta(PS1.class)
    static interface PDefaultStateIndicatorInterface {

        @Transition(S1_X.class)
        void doX();

        // This is the default state getter
        String getState();
    }
    @LifecycleMeta(PS1.class)
    static interface NNoDefaultStateIndicatorInterface {

        @Transition(S1_X.class)
        void doX();

        // This is not the default state getter
        String getStateX();
    }
    @LifecycleMeta(PS1.class)
    static interface NDefaultStateIndicatorInterface {

        @Transition(S1_X.class)
        void doX();

        String getState();

        // Should not have public stateSetter
        void setState(String state);
    }
    @LifecycleMeta(PS1.class)
    static class PDefaultPrivateStateSetterClass {

        private String state;

        @Transition(S1_X.class)
        public void doX(String x, int y, int z) {}

        // Defaulted @StateIndicator
        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(PS1.class)
    static class NDefaultPublicStateSetterClass {

        private String state;

        @Transition(S1_X.class)
        public void doX() {}

        // Defaulted @StateIndicator
        public String getState() {
            return state;
        }

        // Error Modifier
        public void setState(String state) {
            this.state = state;
        }
    }
    // ////////////////////////////////////////////////
    // Field Access State Indicator Tests
    // ////////////////////////////////////////////////
    @LifecycleMeta(PS1.class)
    static class PrivateStateFieldClass {

        @StateIndicator
        private String state = PS1.States.S1_A.class.getSimpleName();

        @Transition(S1_X.class)
        public void doX() {}
    }
    @LifecycleMeta(PS1.class)
    static class PrivateStateFieldConverterClass {

        @StateIndicator
        @Converter(StateConverterImpl.class)
        private Integer state = 1;

        @Transition(S1_X.class)
        public void doX() {}
    }
    @LifecycleMeta(PS1.class)
    static class NPublicStateFieldClass {

        @StateIndicator
        public String state;

        @Transition(S1_X.class)
        public void doX() {}
    }
    // ////////////////////////////////////////////////////
    // Property Access State Indicator
    // ////////////////////////////////////////////////////
    @LifecycleMeta(PS1.class)
    static interface PStateIndicatorInterface {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        String getState();
    }
    @LifecycleMeta(PS1.class)
    static interface PStateIndicatorConverterInterface {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        @Converter(StateConverterImpl.class)
        Integer getState();
    }
    @LifecycleMeta(PS1.class)
    static class PrivateStateSetterClass {

        private String state;

        @Transition(S1_X.class)
        public void doX() {}

        @StateIndicator
        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(PS1.class)
    static interface NPublicStateIndicatorInterface {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        String getState();

        // Should not have public stateSetter
        void setState(String state);
    }
    @LifecycleMeta(PS1.class)
    static class NPublicStateSetterClass {

        private String state;

        @Transition(S1_X.class)
        public void doX() {}

        @StateIndicator
        public String getState() {
            return state;
        }

        // Error Modifier
        public void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(PS1.class)
    static class NStateIndicatorSetterNotFound {

        private int state;

        @Transition(S1_X.class)
        public void doX() {}

        @StateIndicator
        @Converter(StateConverterImpl.class)
        public Integer getState() {
            return state;
        };

        @SuppressWarnings("unused")
        private void setState(int i) {
            this.state = i;
        }
    }
    // //////////////////////////////////////////////////////////////////
    // Invalid State Converter Tests
    // //////////////////////////////////////////////////////////////////
    @LifecycleMeta(PS1.class)
    static interface NNeedConverter {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        Integer getState();
    }
    @LifecycleMeta(PS1.class)
    static interface NStateIndicatorConverterInvalid {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        @Converter(StateConverterImpl.class)
        Object getState();
    }
    // //////////////////////////////////////////////////////////////////
    // Multiple State Indicator Error
    // //////////////////////////////////////////////////////////////////
    @LifecycleMeta(PS1.class)
    static interface PositiveMultipleStateIndicatorSuper {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        String getStateX();
    }
    @LifecycleMeta(PS1.class)
    static interface PositiveMultipleStateIndicatorChild extends PositiveMultipleStateIndicatorSuper {

        @StateIndicator
        @LifecycleOverride
        String getStateY();
    }
    @LifecycleMeta(PS1.class)
    static interface NegativeMultipleStateIndicator {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        @Converter(StateConverterImpl.class)
        Integer getState();

        @StateIndicator
        String getStateX();
    }
    @LifecycleMeta(PS1.class)
    static interface NegativeMultipleStateIndicatorSuper {

        @Transition(S1_X.class)
        void doX();

        @StateIndicator
        String getStateX();
    }
    @LifecycleMeta(PS1.class)
    static interface NegativeMultipleStateIndicatorChild extends NegativeMultipleStateIndicatorSuper {

        @StateIndicator
        String getStateY();
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Testing Main");
        new PDefaultPrivateStateSetterClass().doX("Tracy", 4545, 3343);
    }
}
