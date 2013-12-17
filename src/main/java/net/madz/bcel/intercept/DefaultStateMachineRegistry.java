package net.madz.bcel.intercept;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.verification.VerificationException;

@LifecycleRegistry({})
@StateMachineBuilder
public class DefaultStateMachineRegistry extends AbsStateMachineRegistry {

    private static volatile DefaultStateMachineRegistry INSTANCE;

    public static AbsStateMachineRegistry getInstance() {
        if ( null == INSTANCE ) {
            synchronized (AbsStateMachineRegistry.class) {
                if ( null == INSTANCE ) {
                    try {
                        INSTANCE = new DefaultStateMachineRegistry();
                    } catch (VerificationException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return INSTANCE;
    }

    protected DefaultStateMachineRegistry() throws VerificationException {}
}
