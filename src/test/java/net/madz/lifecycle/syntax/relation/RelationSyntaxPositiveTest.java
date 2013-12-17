package net.madz.lifecycle.syntax.relation;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class RelationSyntaxPositiveTest extends RelationSyntaxMetadata {

    @Test
    public void relation_syntax_positive_standalone() throws VerificationException {
        @LifecycleRegistry(PStandalone.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void relation_syntax_positive_inheritance() throws VerificationException {
        @LifecycleRegistry(PChild.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void parent_relation_syntax_positive_standalone() throws VerificationException {
        @LifecycleRegistry(PStandaloneParent.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void parent_relation_in_composite_state_machine_syntax_positive_standalone() throws VerificationException {
        @LifecycleRegistry(POwningStateMachine.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }

    @Test
    public void parent_relation_in_child_state_machine_syntax_positive_standalone() throws VerificationException {
        @LifecycleRegistry(PParentRelationChild.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        new Registry();
    }
}
