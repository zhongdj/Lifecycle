package net.madz.lifecycle.syntax.lm.stateindicator;

import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.meta.builder.impl.StateMachineMetaBuilderImpl;
import net.madz.verification.VerificationException;

import org.junit.Test;

public class StateIndicatorPositiveTest extends StateIndicatorMetadata {

    @Test
    public void default_state_indicator_interface_impl() throws VerificationException {
        @LifecycleRegistry({ StateIndicatorMetadata.PDefaultStateIndicatorInterface.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
    }

    @Test
    public void default_state_indicator_class_impl() throws VerificationException {
        @LifecycleRegistry({ StateIndicatorMetadata.PDefaultPrivateStateSetterClass.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
    }

    @Test
    public void field_access_state_indicator_class_impl() throws VerificationException {
        @LifecycleRegistry({ StateIndicatorMetadata.PrivateStateFieldClass.class, StateIndicatorMetadata.PrivateStateFieldConverterClass.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
        
        PrivateStateFieldConverterClass o = new PrivateStateFieldConverterClass();
        o.doX();
    }

    @Test
    public void property_access_state_indicator_class_impl() throws VerificationException {
        @LifecycleRegistry({ StateIndicatorMetadata.PrivateStateSetterClass.class, StateIndicatorMetadata.PStateIndicatorInterface.class,
                PStateIndicatorConverterInterface.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
    }

    @Test
    public void state_indicator_overrides() throws VerificationException {
        @LifecycleRegistry({ PositiveMultipleStateIndicatorChild.class })
        @StateMachineBuilder(StateMachineMetaBuilderImpl.class)
        class CorrectRegistry extends AbsStateMachineRegistry {

            protected CorrectRegistry() throws VerificationException {
                super();
            }
        }
        new CorrectRegistry();
    }
}
