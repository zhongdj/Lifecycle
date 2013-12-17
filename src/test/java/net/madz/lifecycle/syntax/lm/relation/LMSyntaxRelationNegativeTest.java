package net.madz.lifecycle.syntax.lm.relation;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_Relations.S5_B_R1;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S5.States.S5_B.S5_B_States.S5_B_A;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.Relations.S7_R;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S7.States.S7_B;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.Relations.S8_R;
import net.madz.lifecycle.syntax.lm.relation.LMSyntaxRelationMetadata.S8.States.S8_A;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class LMSyntaxRelationNegativeTest extends LMSyntaxRelationMetadata {

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3223_if_inboundwhile_relation_not_coveraged() throws VerificationException {
        @LifecycleRegistry(NLM_1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s7_X", NLM_1.class.getName(),
                    S7_R.class.getSimpleName(), S7.class.getName() + ".StateSet." + S7_B.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3223_if_validwhile_relation_not_coveraged() throws VerificationException {
        @LifecycleRegistry(NLM_2.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s8_X", NLM_2.class.getName(),
                    S8_R.class.getSimpleName(), S8.class.getName() + ".StateSet." + S8_A.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3223_if_relation_in_composite_stateMachine_not_coveraged() throws VerificationException {
        @LifecycleRegistry(NLM_3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s5_B_X", NLM_3.class.getName(),
                    S5_B_R1.class.getSimpleName(), S5.class.getName() + ".CompositeStateMachine.S5_B.StateSet." + S5_B_A.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3223_if_relation_in_super_stateMachine_not_coveraged() throws VerificationException {
        @LifecycleRegistry(NLM_4.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_NOT_BE_CONCRETED, "s5_B_X", NLM_4.class.getName(),
                    S5_B_R1.class.getSimpleName(), S5.class.getName() + ".CompositeStateMachine.S5_B.StateSet." + S5_B_A.class.getSimpleName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3221_if_LM_reference_an_invalid_relation_on_field() throws VerificationException {
        @LifecycleRegistry(NLM_5.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REFERENCE_INVALID_RELATION_INSTANCE, NLM_5.class.getName(),
                    S4.Relations.R1.class.getName(), S5.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3221_if_test_LM_reference_an_invalid_relation_on_property() throws VerificationException {
        @LifecycleRegistry(NLM_6.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_REFERENCE_INVALID_RELATION_INSTANCE, NLM_6.class.getName(),
                    S4.Relations.R1.class.getName(), R1_S.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3222_if_relation_defined_multi_times_in_class_level() throws VerificationException {
        @LifecycleRegistry(Negative_Same_Relation_Concreted_Duplicate_On_Fields_r2_r3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_INSTANCE_MUST_BE_UNIQUE, Negative_Same_Relation_Concreted_Duplicate_On_Fields_r2_r3.class.getName(),
                    S4.Relations.R3.class.getName());
            throw e;
        }
    }

    @Test(expected = VerificationException.class)
    public final void should_throw_002_3222_if_relation_defined_multi_times_in_method_level() throws VerificationException {
        @LifecycleRegistry(Negative_Same_Relation_Concreted_Multiple_Times_In_Method_tM1.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            assertFailure(e.getVerificationFailureSet().iterator().next(), SyntaxErrors.LM_RELATION_INSTANCE_MUST_BE_UNIQUE, Negative_Same_Relation_Concreted_Multiple_Times_In_Method_tM1.class.getName(),
                    S4.Relations.R1.class.getName());
            throw e;
        }
    }
}
