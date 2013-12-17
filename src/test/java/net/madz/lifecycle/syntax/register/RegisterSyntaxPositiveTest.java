package net.madz.lifecycle.syntax.register;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class RegisterSyntaxPositiveTest extends RegisterSyntaxTestMetaData {

    @Test
    public void correct_metadata_without_syntax_error() throws VerificationException {
        @LifecycleRegistry({ CorrectStateMachineSyntax.class, CorrectLifecycleMetaSyntax.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
    }

    @Test
    public void correct_inheritance_statemachine() throws VerificationException {
        @LifecycleRegistry({ CorrectStateMachineInheritanceChildSyntax.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectInheritanceRegistry extends AbsStateMachineRegistry {

            protected CorrectInheritanceRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectInheritanceRegistry();
    }
}
