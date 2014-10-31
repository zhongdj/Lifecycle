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
package net.imadz.lifecycle.syntax.lm.relation;

import net.imadz.lifecycle.annotations.CompositeState;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateMachine;
import net.imadz.lifecycle.annotations.StateSet;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.annotations.EventSet;
import net.imadz.lifecycle.annotations.relation.InboundWhile;
import net.imadz.lifecycle.annotations.relation.RelateTo;
import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.annotations.relation.RelationSet;
import net.imadz.lifecycle.annotations.relation.ValidWhile;
import net.imadz.lifecycle.annotations.state.Final;
import net.imadz.lifecycle.annotations.state.Initial;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.annotations.state.ShortCut;
import net.imadz.lifecycle.engine.EngineTestBase.ReactiveObject;
import net.imadz.lifecycle.syntax.BaseMetaDataTest;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.R1_S.Events.R1_S_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.R2_S.Events.R2_S_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.R3_S.Events.R3_S_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S4.Relations.R1;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_Relations.S5_B_R1;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_Events.S5_B_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.Events.S5_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S6.Relations.S6_R1;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S6.Relations.S6_R2;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.Relations.S7_R;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.Events.S7_X;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.Relations.S8_R;
import net.imadz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.Events.S8_X;

public class LMSyntaxRelationMetadata extends BaseMetaDataTest {

    private static final class TestLifecycleInterceptor implements PLM_R1_S {

        private String state = R1_S.States.R1_S_A.class.getSimpleName();

        @Override
        public void tm() {}

        @Override
        public String getState() {
            return state;
        }

        @Override
        public String toString() {
            return "TestLifecycleInterceptor [state=" + state + "]";
        }
    }
    // Positive LM: Concrete all relations in SM
    @StateMachine
    static interface R1_S {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = R1_S_X.class, value = { R1_S_B.class })
            static interface R1_S_A {}
            @Final
            static interface R1_S_B {}
        }
        @EventSet
        static interface Events {

            static interface R1_S_X {}
        }
    }
    @LifecycleMeta(R1_S.class)
    static interface PLM_R1_S {

        @Event(R1_S_X.class)
        void tm();

        String getState();
    }
    @StateMachine
    static interface R2_S {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = R2_S_X.class, value = { R2_S_B.class })
            static interface R2_S_A {}
            @Final
            static interface R2_S_B {}
        }
        @EventSet
        static interface Events {

            static interface R2_S_X {}
        }
    }
    @LifecycleMeta(R2_S.class)
    static interface PLM_R2_S {

        @Event(R2_S_X.class)
        void tm();

        String getState();
    }
    @StateMachine
    static interface R3_S {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = R3_S_X.class, value = { R3_S_B.class })
            static interface R3_S_A {}
            @Final
            static interface R3_S_B {}
        }
        @EventSet
        static interface Events {

            static interface R3_S_X {}
        }
    }
    @LifecycleMeta(R3_S.class)
    static interface PLM_R3_S {

        @Event(R3_S_X.class)
        void tm();

        String getState();
    }
    @StateMachine
    static interface S4 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S4.Events.X.class, value = { S4_B.class })
            static interface S4_A {}
            @InboundWhile(on = { R1_S.States.R1_S_A.class }, relation = S4.Relations.R1.class)
            @ValidWhile(on = { R2_S.States.R2_S_A.class }, relation = S4.Relations.R2.class)
            @Transition(event = S4.Events.Y.class, value = { S4_C.class })
            static interface S4_B {}
            @Transition(event = S4.Events.Z.class, value = { S4_D.class })
            @ValidWhile(relation = S4.Relations.R3.class, on = { R3_S.States.R3_S_A.class })
            static interface S4_C {}
            @Final
            static interface S4_D {}
        }
        @EventSet
        static interface Events {

            static interface X {}
            static interface Y {}
            static interface Z {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(R1_S.class)
            static interface R1 {}
            @RelateTo(R2_S.class)
            static interface R2 {}
            @RelateTo(R3_S.class)
            static interface R3 {}
        }
    }
    @LifecycleMeta(S4.class)
    static class PLM_5 {

        String state;

        @Event(S4.Events.X.class)
        void tM1(@Relation(S4.Relations.R1.class) PLM_R1_S x) {
            System.out.println(". print inside tM1 .");
        }

        @Event(S4.Events.Y.class)
        void tM2() {}

        @Event(S4.Events.Z.class)
        void tM3() {}

        @Relation(S4.Relations.R2.class)
        PLM_R2_S r2 = null;

        @Relation(S4.Relations.R3.class)
        PLM_R3_S getR3S() {
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
    // Positive LM: Concrete all relations in SM that contains
    // CompositeStateMachines
    @StateMachine
    static interface S5 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S5_X.class, value = { S5_B.class })
            static interface S5_A {}
            @CompositeState
            static interface S5_B {

                @StateSet
                static interface S5_B_States {

                    @Initial
                    @Transition(event = S5_B_X.class, value = { S5_B_B.class })
                    @ValidWhile(on = { R1_S.States.R1_S_A.class }, relation = S5_B_R1.class)
                    static interface S5_B_A {}
                    @Final
                    @ShortCut(value = S5_C.class)
                    static interface S5_B_B {}
                }
                @EventSet
                static interface S5_B_Events {

                    static interface S5_B_X {}
                }
                @RelationSet
                static interface S5_B_Relations {

                    @RelateTo(R1_S.class)
                    static interface S5_B_R1 {};
                }
            }
            @Final
            static interface S5_C {}
        }
        @EventSet
        static interface Events {

            static interface S5_X {}
            static interface S5_Y {}
        }
    }
    @LifecycleMeta(S5.class)
    static class PLM_6 {

        String state;

        public String getState() {
            return state;
        }

        @Event
        void s5_X() {}

        @Event
        void s5_Y() {}

        @Event
        void s5_B_X() {}

        @Relation(S5_B_R1.class)
        PLM_R1_S r1_S = null;

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    // Positive LM: Concrete all relations in SM that has super StateMachines.
    @StateMachine
    static interface S6 extends S5 {

        @StateSet
        static interface States extends S5.States {

            @ValidWhile(on = { R1_S.States.R1_S_A.class }, relation = S6_R1.class)
            static interface S6_A extends S5_A {}
            @InboundWhile(on = { R2_S.States.R2_S_A.class }, relation = S6_R2.class)
            @CompositeState
            static interface S6_B extends S5_B {}
        }
        @EventSet
        static interface Transitons extends S5.Events {}
        @RelationSet
        static interface Relations {

            @RelateTo(R1_S.class)
            static interface S6_R1 {}
            @RelateTo(R2_S.class)
            static interface S6_R2 {}
        }
    }
    @LifecycleMeta(S6.class)
    static class PLM_7 {

        String state;

        public String getState() {
            return state;
        }

        @Event
        void s5_X() {}

        @Event
        void s5_Y(@Relation(S6_R2.class) PLM_R2_S rs_S) {}

        @Event
        void s5_B_X() {}

        @Relation(S5_B_R1.class)
        PLM_R1_S r1_S = null;

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    // Relation Negative Test
    // Relation in InboundWhile or InboundWhiles not binded in LM.
    @StateMachine
    static interface S7 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S7_X.class, value = { S7_B.class })
            static interface S7_A {}
            @Final
            @InboundWhile(on = { R1_S.States.R1_S_A.class }, relation = S7_R.class)
            static interface S7_B {}
        }
        @EventSet
        static interface Events {

            static interface S7_X {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(R1_S.class)
            static interface S7_R {}
        }
    }
    @LifecycleMeta(S7.class)
    static interface NLM_1 {

        @Event
        void s7_X(PLM_R1_S r1_S);

        String getState();
    }
    // Relation Negative Test
    // Relation in ValidWhile or ValidWhiles not binded in LM.
    @StateMachine
    static interface S8 {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = S8_X.class, value = { S8_B.class })
            @ValidWhile(on = { R1_S.States.R1_S_A.class }, relation = S8_R.class)
            static interface S8_A {}
            @Final
            static interface S8_B {}
        }
        @EventSet
        static interface Events {

            static interface S8_X {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(R1_S.class)
            static interface S8_R {}
        }
    }
    @LifecycleMeta(S8.class)
    static class NLM_2 {

        String state;

        @Event
        void s8_X() {}

        public String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    // Relation Negative Test
    // Relation in Composite StateMachine not binded in LM.
    @LifecycleMeta(S5.class)
    static class NLM_3 {

        String state;

        @Event
        void s5_X() {}

        @Event
        void s5_Y() {}

        @Event
        void s5_B_X() {}

        String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    // Relation Negative Test
    // Relation in Super StateMachine not binded in LM.
    @LifecycleMeta(S6.class)
    static class NLM_4 {

        String state;

        @Event
        void s5_X() {}

        @Event
        void s5_Y(@Relation(S6_R2.class) PLM_R2_S r2_S) {}

        @Event
        void s5_B_X() {}

        @Relation(S6_R1.class)
        PLM_R1_S r1_S = null;

        String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    // Relation Negative Test
    // Binding an invalid Relation that does not exist in S5.
    @LifecycleMeta(S5.class)
    static class NLM_5 {

        String state;

        @Event
        void s5_X() {}

        @Event
        void s5_Y() {}

        @Event
        void s5_B_X() {}

        @Relation(S4.Relations.R1.class)
        PLM_R1_S r1_S = null;

        String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(R1_S.class)
    static class NLM_6 {

        String state;

        @Event
        void r1_S_X() {}

        @Relation(S4.Relations.R1.class)
        String getState() {
            return state;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(S4.class)
    static class Negative_Same_Relation_Concreted_Duplicate_On_Fields_r2_r3 {

        String state;

        @Event(S4.Events.X.class)
        void tM1(@Relation(R1.class) PLM_R1_S x) {}

        @Event(S4.Events.Y.class)
        void tM2() {}

        @Event(S4.Events.Z.class)
        void tM3() {}

        @Relation(S4.Relations.R2.class)
        PLM_R2_S r2 = null;
        @Relation(S4.Relations.R3.class)
        PLM_R2_S r3 = null;

        @Relation(S4.Relations.R3.class)
        public PLM_R3_S getR3S() {
            return null;
        }

        public String getState() {
            return null;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }
    @LifecycleMeta(S4.class)
    static class Negative_Same_Relation_Concreted_Multiple_Times_In_Method_tM1 {

        String state;

        @Event(S4.Events.X.class)
        void tM1(@Relation(R1.class) PLM_R1_S x, @Relation(R1.class) PLM_R1_S x2) {}

        @Event(S4.Events.Y.class)
        void tM2() {}

        @Event(S4.Events.Z.class)
        void tM3() {}

        @Relation(S4.Relations.R2.class)
        PLM_R2_S r2 = null;
        @Relation(S4.Relations.R3.class)
        PLM_R2_S r3 = null;

        public String getState() {
            return null;
        }

        @SuppressWarnings("unused")
        private void setState(String state) {
            this.state = state;
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Testing Main");
        PLM_5 plm_5 = new PLM_5();
        PLM_R1_S x = new TestLifecycleInterceptor();
        plm_5.tM1(x);
    }

    @StateMachine
    static interface LevelOneOrderLifecycle {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = Events.Pay.class, value = { States.Paid.class })
            @ValidWhile(on = { LevelOneCustomerLifecycle.States.Activated.class }, relation = Relations.CustomerRelation.class)
            static interface Draft {}
            @Transition(event = Events.Deliver.class, value = { States.Delivered.class })
            static interface Paid {}
            @Final
            static interface Delivered {}
        }
        @EventSet
        static interface Events {

            static interface Pay {}
            static interface Deliver {}
        }
        @RelationSet
        static interface Relations {

            @RelateTo(LevelOneCustomerLifecycle.class)
            static interface CustomerRelation {}
        }
    }
    @StateMachine
    static interface LevelTwoOrderLifecycle extends LevelOneOrderLifecycle {

        @StateSet
        static interface States extends LevelOneOrderLifecycle.States {

            @LifecycleOverride
            @ValidWhile(on = { LevelTwoCustomerLifecycle.States.CreditRated.class }, relation = Relations.CustomerRelation.class)
            @Transition(event = LevelOneOrderLifecycle.Events.Pay.class, value = { LevelOneOrderLifecycle.States.Paid.class })
            @Initial
            static interface Draft extends LevelOneOrderLifecycle.States.Draft {}
        }
        @RelationSet
        static interface Relations extends LevelOneOrderLifecycle.Relations {

            @RelateTo(LevelTwoCustomerLifecycle.class)
            static interface CustomerRelation extends LevelOneOrderLifecycle.Relations.CustomerRelation {}
        }
    }
    @StateMachine
    static interface LevelThreeOrderLifecycle extends LevelTwoOrderLifecycle {

        @StateSet
        static interface States extends LevelTwoOrderLifecycle.States {

            @LifecycleOverride
            @ValidWhile(on = { LevelThreeCustomerLifecycle.States.Prepaid.class }, relation = Relations.CustomerRelation.class)
            @Transition(event = LevelTwoOrderLifecycle.Events.Pay.class, value = { LevelTwoOrderLifecycle.States.Paid.class })
            @Initial
            static interface Draft extends LevelOneOrderLifecycle.States.Draft {}
        }
        @RelationSet
        static interface Relations extends LevelTwoOrderLifecycle.Relations {

            @RelateTo(LevelThreeCustomerLifecycle.class)
            static interface CustomerRelation extends LevelTwoOrderLifecycle.Relations.CustomerRelation {}
        }
    }
    @StateMachine
    static interface LevelOneCustomerLifecycle {

        @StateSet
        static interface States {

            @Initial
            @Transition(event = Events.Activate.class, value = { States.Activated.class })
            static interface Draft {}
            @Final
            static interface Activated {}
        }
        @EventSet
        static interface Events {

            static interface Activate {}
        }
    }
    @StateMachine
    static interface LevelTwoCustomerLifecycle extends LevelOneCustomerLifecycle {

        @StateSet
        static interface States extends LevelOneCustomerLifecycle.States {

            @LifecycleOverride
            @Transition(event = Events.CreditRate.class, value = { States.CreditRated.class })
            static interface Activated extends LevelOneCustomerLifecycle.States.Activated {}
            @Final
            static interface CreditRated {}
        }
        @EventSet
        static interface Events extends LevelOneCustomerLifecycle.Events {

            static interface CreditRate {}
        }
    }
    @StateMachine
    static interface LevelThreeCustomerLifecycle extends LevelTwoCustomerLifecycle {

        @StateSet
        static interface States extends LevelTwoCustomerLifecycle.States {

            @LifecycleOverride
            @Transition(event = Events.Prepay.class, value = { States.Prepaid.class })
            static interface CreditRated extends LevelTwoCustomerLifecycle.States.CreditRated {}
            @Final
            static interface Prepaid {}
        }
        @EventSet
        static interface Events extends LevelTwoCustomerLifecycle.Events {

            static interface Prepay {}
        }
    }
    @LifecycleMeta(LevelOneCustomerLifecycle.class)
    public static class LevelOneCustomer extends ReactiveObject {

        public LevelOneCustomer() {
            initialState(LevelOneCustomerLifecycle.States.Draft.class.getSimpleName());
        }

        @Event
        public void activate() {}
    }
    @LifecycleMeta(LevelTwoCustomerLifecycle.class)
    public static class LevelTwoCustomer extends LevelOneCustomer {

        @Event
        public void creditRate() {}
    }
    @LifecycleMeta(LevelThreeCustomerLifecycle.class)
    public static class LevelThreeCustomer extends LevelTwoCustomer {

        @Event
        public void prepay() {}
    }
    @LifecycleMeta(LevelOneOrderLifecycle.class)
    public static class LevelOneOrder extends ReactiveObject {

        public LevelOneOrder(LevelOneCustomer levelOneCustomer) {
            this.levelOneCustomer = levelOneCustomer;
            initialState(LevelOneOrderLifecycle.States.Draft.class.getSimpleName());
        }

        @Relation(LevelOneOrderLifecycle.Relations.CustomerRelation.class)
        private LevelOneCustomer levelOneCustomer;

        @Event
        public void pay() {}

        @Event
        public void deliver() {}
    }
    @LifecycleMeta(LevelTwoOrderLifecycle.class)
    public static class LevelTwoOrder extends LevelOneOrder {

        public LevelTwoOrder(LevelTwoCustomer customer) {
            super(customer);
            this.levelTwoCustomer = customer;
        }

        @Relation(LevelTwoOrderLifecycle.Relations.CustomerRelation.class)
        private LevelTwoCustomer levelTwoCustomer;
    }
    @LifecycleMeta(LevelThreeOrderLifecycle.class)
    public static class LevelThreeOrder extends LevelTwoOrder {

        public LevelThreeOrder(LevelThreeCustomer customer) {
            super(customer);
            this.customer = customer;
        }

        private LevelThreeCustomer customer;

        @Relation(LevelThreeOrderLifecycle.Relations.CustomerRelation.class)
        public LevelThreeCustomer getLevelThreeCustomer() {
            return customer;
        }
    }
}
