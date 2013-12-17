package net.madz.lifecycle.semantics;

import static org.junit.Assert.fail;

import java.util.HashMap;

import net.madz.common.DottedPath;
import net.madz.lifecycle.AbsStateMachineRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.LifecycleRegistry;
import net.madz.lifecycle.AbsStateMachineRegistry.StateMachineBuilder;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.verification.VerificationException;

import org.junit.Assert;
import org.junit.Test;

public class StateMachineSemanticsTest extends StateMachineSemanticsMetadata {

    @Test
    public final void testGetAllStates() {
        @LifecycleRegistry(S3.class)
        @StateMachineBuilder
        class Registry extends AbsStateMachineRegistry {

            protected Registry() throws VerificationException {}
        }
        try {
            final Registry registry = new Registry();
            final StateMetadata[] allStates = registry.getStateMachineMeta(S3.class).getAllStates();
            final HashMap<DottedPath, StateMetadata> statesMap = new HashMap<DottedPath, StateMetadata>();
            for ( StateMetadata state : allStates ) {
                System.out.println("Testing=====" + state.getDottedPath());
                statesMap.put(state.getDottedPath(), state);
            }
            Assert.assertEquals("Expected 6 States in StateMachine", 6, allStates.length);
            Assert.assertNotNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S3.StateSet.S3_E", statesMap));
            Assert.assertNotNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S3.StateSet.S3_A", statesMap));
            Assert.assertNotNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S1.StateSet.S1_C", statesMap));
            Assert.assertNotNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S1.StateSet.S1_D", statesMap));
            Assert.assertNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S1.StateSet.S1_A", statesMap));
            Assert.assertNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S1.StateSet.S1_B", statesMap));
            Assert.assertNull(findStateInStatesMap("net.madz.lifecycle.semantics.StateMachineSemanticsMetadata$S2.StateSet.S2_A", statesMap));
        } catch (VerificationException e) {
            e.printStackTrace();
            fail("No exception expeced");
        }
    }

    private Object findStateInStatesMap(final String path, final HashMap<DottedPath, StateMetadata> statesMap) {
        return statesMap.get(new DottedPath(path));
    }
}
