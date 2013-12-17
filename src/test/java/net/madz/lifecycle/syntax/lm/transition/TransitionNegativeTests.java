package net.madz.lifecycle.syntax.lm.transition;

import java.util.HashMap;
import java.util.Iterator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Activate;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Inactivate;
import net.madz.lifecycle.syntax.lm.transition.TransitionTestMetadata.SpecialTranstionStateMachine.Transitions.Restart;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailure;

import org.junit.Test;

public class TransitionNegativeTests extends TransitionTestMetadata {

    @Test(expected = VerificationException.class)
    public void should_throw_exception_if_transition_method_of_special_transition_type_has_parameter() throws VerificationException, Throwable {
        @LifecycleRegistry(NegativeProcess.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            new Registry();
        } catch (VerificationException e) {
            final Iterator<VerificationFailure> iterator = e.getVerificationFailureSet().iterator();
            final HashMap<String, VerificationFailure> errors = new HashMap<>();
            VerificationFailure next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            next = iterator.next();
            errors.put(next.getErrorKey().getName(), next);
            assertFailure(errors.get("Inactivate"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("inactivate", Integer.TYPE), Inactivate.class.getSimpleName(), TransitionTypeEnum.Corrupt);
            assertFailure(errors.get("Activate"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("activate", Integer.TYPE), Activate.class.getSimpleName(), TransitionTypeEnum.Recover);
            assertFailure(errors.get("Restart"), SyntaxErrors.TRANSITION_TYPE_CORRUPT_RECOVER_REDO_REQUIRES_ZERO_PARAMETER,
                    NegativeProcess.class.getDeclaredMethod("restart", Integer.TYPE), Restart.class.getSimpleName(), TransitionTypeEnum.Redo);
            throw e;
        }
    }
}
