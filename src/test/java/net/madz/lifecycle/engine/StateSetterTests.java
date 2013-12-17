package net.madz.lifecycle.engine;

import static org.junit.Assert.*;

import org.junit.Test;

public class StateSetterTests extends StateSetterTestMetadata {

    @Test
    public void should_support_eager_and_lazy_state_setter() {
        final LazySetterBusinessImpl lazy = new LazySetterBusinessImpl();
        assertEquals(StateSetterTestMetadata.SetterTestStateMachine.States.New.class.getSimpleName(), lazy.getState());
        lazy.doIt();
        assertEquals(StateSetterTestMetadata.SetterTestStateMachine.States.Done.class.getSimpleName(), lazy.getState());
        final EagerSetterBusinessImpl eager = new EagerSetterBusinessImpl();
        assertEquals(StateSetterTestMetadata.SetterTestStateMachine.States.New.class.getSimpleName(), eager.getState());
        eager.doIt();
        assertEquals(StateSetterTestMetadata.SetterTestStateMachine.States.Done.class.getSimpleName(), eager.getState());
    }

    @Test
    public void should_support_boolean_type_state_indicator() {
        final BooleanTypeObject o = new BooleanTypeObject();
        assertEquals(false, o.isClosed());
        o.close();
        assertEquals(true, o.isClosed());
    }
}
