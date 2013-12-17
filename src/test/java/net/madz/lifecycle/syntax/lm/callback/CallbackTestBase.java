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
package net.madz.lifecycle.syntax.lm.callback;

import net.madz.lifecycle.LifecycleContext;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateIndicator;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.action.Condition;
import net.madz.lifecycle.annotations.action.ConditionSet;
import net.madz.lifecycle.annotations.action.Conditional;
import net.madz.lifecycle.annotations.action.ConditionalTransition;
import net.madz.lifecycle.annotations.callback.Callbacks;
import net.madz.lifecycle.annotations.callback.PostStateChange;
import net.madz.lifecycle.annotations.callback.PreStateChange;
import net.madz.lifecycle.annotations.relation.InboundWhile;
import net.madz.lifecycle.annotations.relation.RelateTo;
import net.madz.lifecycle.annotations.relation.Relation;
import net.madz.lifecycle.annotations.relation.RelationSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.syntax.BaseMetaDataTest;

public abstract class CallbackTestBase extends BaseMetaDataTest {

    @StateMachine
    static interface S1 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = Transitions.S1_Transition_X.class, value = { S1_State_B.class, S1_State_C.class })
            @Functions({ @Function(transition = Transitions.S1_Transition_X.class, value = { S1_State_B.class, S1_State_C.class }),
                    @Function(transition = Transitions.S1_Transition_Y.class, value = { S1_State_D.class }) })
            static interface S1_State_A {}
            @End
            static interface S1_State_B {}
            @End
            static interface S1_State_C {}
            @End
            static interface S1_State_D {}
        }
        @TransitionSet
        static interface Transitions {

            @Conditional(condition = S1.Conditions.S1_Condition_A.class, judger = VolumeMeasurableTransition.class, postEval = true)
            static interface S1_Transition_X {}
            static interface S1_Transition_Y {}
        }
        @ConditionSet
        static interface Conditions {

            static interface S1_Condition_A {

                boolean isVolumeLeft();
            }
        }
        public static class VolumeMeasurableTransition implements ConditionalTransition<S1.Conditions.S1_Condition_A> {

            @Override
            public Class<?> doConditionJudge(S1.Conditions.S1_Condition_A t) {
                if ( t.isVolumeLeft() ) {
                    return S1.States.S1_State_B.class;
                } else {
                    return S1.States.S1_State_C.class;
                }
            }
        }
    }
    @StateMachine
    static interface S2 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = S2.Transitions.Move.class, value = { S2_State_B.class })
            static interface S2_State_A {}
            @InboundWhile(on = { S1.States.S1_State_B.class }, relation = S2.Relations.S1Relation.class)
            @End
            static interface S2_State_B {}
        }
        @TransitionSet
        static interface Transitions {

            static interface Move {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(S1.class)
            static interface S1Relation {}
        }
    }
    @LifecycleMeta(S1.class)
    static class S1BaseLM {

        private String state;

        @Transition
        public void s1_Transition_X() {}

        @Transition
        public void s1_Transition_Y() {}

        @Condition(S1.Conditions.S1_Condition_A.class)
        public S1.Conditions.S1_Condition_A getConditionA() {
            return null;
        }

        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_With_PreStateChange_To_State_With_Post_Evaluate_Non_Relational extends S1BaseLM {

        /***
         * prestatechange
         * conditional
         * pre-evaluate
         * from
         * ok
         * to
         * ok
         * post-evaluate
         * from
         * ok
         * to
         * ???
         */
        @PreStateChange(to = S1.States.S1_State_C.class)
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_To_State_With_Post_Evaluate_Non_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_prestatechange_from_state_not_found_in_stateMachine_non_relational extends S1BaseLM {

        @PreStateChange(from = S2.States.S2_State_A.class)
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_from_state_not_found_in_stateMachine_non_relational, String> context) {
            System.out.println("The from state is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_prestatechange_to_state_invalid_non_relational extends S1BaseLM {

        @PreStateChange(to = S2.States.S2_State_A.class)
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The to state is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_poststatechange_from_state_invalid_non_relational extends S1BaseLM {

        @PostStateChange(from = S2.States.S2_State_A.class)
        public void interceptStateChange(LifecycleContext<NLM_poststatechange_from_state_invalid_non_relational, String> context) {
            System.out.println("The from state is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_poststatechange_to_state_invalid_non_relational extends S1BaseLM {

        @PostStateChange(to = S2.States.S2_State_A.class)
        public void interceptStateChange(LifecycleContext<NLM_poststatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The to state is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_prestatechange_observable_object_not_found extends S1BaseLM {

        @PreStateChange(to = S1.States.S1_State_A.class, observableName = "s1", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The relation is invalid.");
        }
    }
    @LifecycleMeta(S2.class)
    static class NLM_prestatechange_mappedby_invalid {

        @Relation(S2.Relations.S1Relation.class)
        private S1BaseLM s1;
        @StateIndicator
        private String state;

        public NLM_prestatechange_mappedby_invalid() {
            state = S2.States.S2_State_A.class.getSimpleName();
        }

        @Transition
        public void move() {}

        public String getState() {
            return state;
        }

        @PreStateChange(to = S1.States.S1_State_D.class, observableName = "s1", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The mappedBy is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_poststatechange_observable_name_invalid extends S1BaseLM {

        @PostStateChange(to = S1.States.S1_State_A.class, observableName = "s1", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The relation is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class NLM_poststatechange_mappedby_invalid extends S1BaseLM {

        @SuppressWarnings("unused")
        private S1BaseLM s1;

        @PostStateChange(to = S2.States.S2_State_A.class, observableName = "s1", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The mappedBy is invalid.");
        }
    }
    @LifecycleMeta(S1.class)
    static class PLM_With_CallBacksWithDefaultValues extends S1BaseLM {

        @Callbacks
        public void interceptStates(LifecycleContext<PLM_With_CallBacksWithDefaultValues, String> context) {}
    }
    @LifecycleMeta(S1.class)
    static class NLM_With_CallBacksWithInvalidStates extends S1BaseLM {

        @Callbacks(preStateChange = { @PreStateChange(from = S2.States.S2_State_A.class) }, postStateChange = { @PostStateChange(
                to = S2.States.S2_State_A.class) })
        public void interceptStates(LifecycleContext<NLM_With_CallBacksWithInvalidStates, String> context) {}
    }
    @StateMachine
    static interface S3 {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = S3.Transitions.Move.class, value = { S3_State_B.class, S3_State_C.class })
            static interface S3_State_A {}
            @InboundWhile(on = { S1.States.S1_State_B.class }, relation = S3.Relations.S1Relation.class)
            @End
            static interface S3_State_B {}
            @End
            static interface S3_State_C {}
        }
        @TransitionSet
        static interface Transitions {

            @Conditional(condition = S3.Conditions.S3_Condition_A.class, judger = S3.S3_VolumeMeasurableTransition.class, postEval = true)
            static interface Move {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(S1.class)
            static interface S1Relation {}
        }
        @ConditionSet
        static interface Conditions {

            static interface S3_Condition_A {

                boolean isVolumeLeft();
            }
        }
        public static class S3_VolumeMeasurableTransition implements ConditionalTransition<S3.Conditions.S3_Condition_A> {

            @Override
            public Class<?> doConditionJudge(S3.Conditions.S3_Condition_A t) {
                if ( t.isVolumeLeft() ) {
                    return S3.States.S3_State_B.class;
                } else {
                    return S3.States.S3_State_C.class;
                }
            }
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_To_State_With_Post_Evaluate_Relational extends S1BaseLM {

        private NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable s3;

        public NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable getS3() {
            return s3;
        }

        /***
         * prestatechange
         * conditional
         * pre-evaluate
         * from
         * ok
         * to
         * ok
         * post-evaluate
         * from
         * ok
         * to
         * ???
         */
        @PreStateChange(to = S3.States.S3_State_C.class, observableName = "s3", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_To_State_With_Post_Evaluate_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S3.class)
    public static class NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable implements S3.Conditions.S3_Condition_A {

        @Relation(S3.Relations.S1Relation.class)
        private NLM_With_PreStateChange_To_State_With_Post_Evaluate_Relational s1;

        @Transition
        public void move() {}

        @StateIndicator
        private String state = S3.States.S3_State_A.class.getSimpleName();

        public String getState() {
            return state;
        }

        @Override
        public boolean isVolumeLeft() {
            return false;
        }

        @Condition(S3.Conditions.S3_Condition_A.class)
        public S3.Conditions.S3_Condition_A getS3Condition_A() {
            return this;
        }
    }
    // **********************Relational: ObservableName************************
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_From_State_Invalid_Relational extends S1BaseLM {

        private NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable s3;

        public NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable getS3() {
            return s3;
        }

        @PreStateChange(from = S1.States.S1_State_A.class, observableName = "s3", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_From_State_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_To_State_Invalid_Relational extends S1BaseLM {

        private NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable s3;

        public NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable getS3() {
            return s3;
        }

        @PreStateChange(to = S1.States.S1_State_D.class, observableName = "s3", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_To_State_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_From_State_Invalid_Relational extends S1BaseLM {

        private NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable s3;

        public NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable getS3() {
            return s3;
        }

        @PostStateChange(from = S1.States.S1_State_A.class, observableName = "s3", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_From_State_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_To_State_Invalid_Relational extends S1BaseLM {

        private NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable s3;

        public NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable getS3() {
            return s3;
        }

        @PostStateChange(to = S1.States.S1_State_D.class, observableName = "s3", mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_To_State_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    // **********************Relational: ObservableClass************************
    // PostStateChange
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_ObservableName_Mistmatch_ObservableClass_Relational extends S1BaseLM {

        private S1BaseLM s3;

        public S1BaseLM getS3() {
            return s3;
        }

        @PostStateChange(observableName = "s3", observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class, mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_To_State_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    public static class NonLifecycleClass {}
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_ObservableClass_Invalid_Relational extends S1BaseLM {

        @PostStateChange(observableClass = NonLifecycleClass.class, mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_ObservableClass_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_From_State_Invalid_Specified_ObservableClass_Relational extends S1BaseLM {

        @PostStateChange(from = S1.States.S1_State_A.class, observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_From_State_Invalid_Specified_ObservableClass_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PostStateChange_To_State_Invalid_Specified_ObservableClass_Relational extends S1BaseLM {

        @PostStateChange(to = S1.States.S1_State_D.class, observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PostStateChange_To_State_Invalid_Specified_ObservableClass_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    // PreStateChange
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_ObservableName_Mistmatch_ObservableClass_Relational extends S1BaseLM {

        private S1BaseLM s3;

        public S1BaseLM getS3() {
            return s3;
        }

        @PreStateChange(observableName = "s3", observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class, mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_ObservableName_Mistmatch_ObservableClass_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_ObservableClass_Invalid_Relational extends S1BaseLM {

        @PreStateChange(observableClass = NonLifecycleClass.class, mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_ObservableClass_Invalid_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_From_State_Invalid_Specified_ObservableClass_Relational extends S1BaseLM {

        @PreStateChange(from = S1.States.S1_State_A.class, observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_From_State_Invalid_Specified_ObservableClass_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S1.class)
    public static class NLM_With_PreStateChange_To_State_Invalid_Specified_ObservableClass_Relational extends S1BaseLM {

        @PreStateChange(to = S1.States.S1_State_D.class, observableClass = NLM_With_PreStateChange_To_Possible_Next_State_Relational_Observable.class,
                mappedBy = "s1")
        public void interceptStateChange(LifecycleContext<NLM_With_PreStateChange_To_State_Invalid_Specified_ObservableClass_Relational, String> context) {
            System.out.println("The callback method will not be invoked.");
        }
    }
    @LifecycleMeta(S2.class)
    static class NLM_prestatechange_mappedby_is_null {

        @Relation(S2.Relations.S1Relation.class)
        private S1BaseLM s1;
        @StateIndicator
        private String state;

        public NLM_prestatechange_mappedby_is_null() {
            state = S2.States.S2_State_A.class.getSimpleName();
        }

        @Transition
        public void move() {}

        public String getState() {
            return state;
        }

        @PreStateChange(to = S1.States.S1_State_D.class, observableName = "s1", mappedBy = "")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The mappedBy is invalid.");
        }
    }
    @LifecycleMeta(S2.class)
    static class NLM_poststatechange_mappedby_is_null {

        @Relation(S2.Relations.S1Relation.class)
        private S1BaseLM s1;
        @StateIndicator
        private String state;

        public NLM_poststatechange_mappedby_is_null() {
            state = S2.States.S2_State_A.class.getSimpleName();
        }

        @Transition
        public void move() {}

        public String getState() {
            return state;
        }

        @PostStateChange(to = S1.States.S1_State_D.class, observableName = "s1", mappedBy = "")
        public void interceptStateChange(LifecycleContext<NLM_prestatechange_to_state_invalid_non_relational, String> context) {
            System.out.println("The mappedBy is invalid.");
        }
    }
}
