package net.madz.lifecycle.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.madz.lifecycle.annotations.Function;
import net.madz.lifecycle.annotations.Functions;
import net.madz.lifecycle.annotations.LifecycleMeta;
import net.madz.lifecycle.annotations.StateMachine;
import net.madz.lifecycle.annotations.StateSet;
import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.annotations.TransitionSet;
import net.madz.lifecycle.annotations.state.End;
import net.madz.lifecycle.annotations.state.Initial;
import net.madz.lifecycle.engine.ReturnTypeTests.ReturnTypeStateMachine.Transitions.FinishThis;
import net.madz.lifecycle.engine.ReturnTypeTests.ReturnTypeStateMachine.Transitions.JustDoIt;

import org.junit.Test;

public class ReturnTypeTests extends EngineTestBase {

    @StateMachine
    static interface ReturnTypeStateMachine {

        @StateSet
        static interface States {

            @Initial
            @Function(transition = JustDoIt.class, value = OnTheFly.class)
            static interface New {}
            @Functions({ @Function(transition = JustDoIt.class, value = OnTheFly.class), @Function(transition = FinishThis.class, value = Done.class) })
            static interface OnTheFly {}
            @End
            static interface Done {}
        }
        @TransitionSet
        static interface Transitions {

            static interface JustDoIt {}
            static interface FinishThis {}
        }
    }
    @LifecycleMeta(ReturnTypeStateMachine.class)
    public static class ReturnTypes extends ReactiveObject {

        public ReturnTypes() {
            initialState(ReturnTypeStateMachine.States.New.class.getSimpleName());
        }

        @Transition(JustDoIt.class)
        public boolean returnBoolean() {
            return false;
        }

        @Transition(JustDoIt.class)
        public byte returnByte() {
            return 1;
        }

        @Transition(JustDoIt.class)
        public short returnShort() {
            return 1;
        }

        @Transition(JustDoIt.class)
        public int returnInt() {
            return 1;
        }

        @Transition(JustDoIt.class)
        public long returnLong() {
            return 1L;
        }

        @Transition(JustDoIt.class)
        public float returnFloat() {
            return 1.0F;
        }

        @Transition(JustDoIt.class)
        public double returnDouble() {
            return 1.0D;
        }

        @Transition(JustDoIt.class)
        public char returnChar() {
            return 'a';
        }

        @Transition(JustDoIt.class)
        public Object returnObject() {
            return new Object();
        }

        @Transition(FinishThis.class)
        public void doFinish() {}
        // public Object doObject() {
        // InterceptorController<ReturnTypes, Object> controller = new
        // InterceptorController<>();
        // InterceptContext<ReturnTypes, Object> context = new
        // InterceptContext<ReturnTypeTests.ReturnTypes,
        // Object>(ReturnTypes.class, this, "doObject",
        // new Class[0], new Object[0]);
        // return controller.exec(context, new Callable<Object>() {
        //
        // @Override
        // public Object call() throws Exception {
        // return doObject$Impl();
        // }
        // });
        // }
        //
        // public Object doObject$Impl() {
        // return new Object();
        // }
    }

    @Test
    public void should_support_8_wrapper_classes_and_Object_as_return_type_with_bcel_bytecode_transform() {
        final ReturnTypes type = new ReturnTypes();
        assertFalse(type.returnBoolean());
        assertEquals((byte) 1, type.returnByte());
        assertEquals('a', type.returnChar());
        assertEquals(1.0D, type.returnDouble(), 0D);
        assertEquals(1.0F, type.returnFloat(), 0F);
        assertEquals(1, type.returnInt());
        assertEquals(1L, type.returnLong());
        assertEquals((short) 1, type.returnShort());
        type.returnObject();
        type.doFinish();
    }
}
