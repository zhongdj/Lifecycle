package net.imadz.lifecycle.engine.callback.priority;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PostStateChangeByPriorityPositiveTests extends PriorityTestMetadata{

	/**Use priority first to invoke callback methods when priority set**/
	@Test
    public void should_invoke_by_priority_when_priority_set_without_hierarchy() {
        final InvokeByPriorityWithoutHierarchy o = new InvokeByPriorityWithoutHierarchy();
        assertEquals("", o.getCallbackInvokeCounter());
        o.start();
        assertEquals("1,2,3,4,", o.getCallbackInvokeCounter());
        o.finish();
        assertEquals("1,2,3,4,1,", o.getCallbackInvokeCounter());
    }
	
	/**When priority same, use inheritance level from top to down to invoke callback methods **/
	@Test
	public void should_invoke_by_inheritance_level_when_priority_same() {
		final Son s = new Son();
		assertEquals("", s.callbackInvokeAcc);
		s.start();
		assertEquals("GrandFather,Father,Son,", s.callbackInvokeAcc);
		s.finish();
		assertEquals("GrandFather,Father,Son,GrandFather,Father,Son,", s.callbackInvokeAcc);
	}
	/**When priority same, inheritance level same, use generalize for ordering: common -> from/to -> specific**/
	@Test
	public void should_invoke_by_generalize_when_priority_and_inheritance_same() {
		final InvokeByGeneralizeDimensionWhenPrioritySame o = new InvokeByGeneralizeDimensionWhenPrioritySame();
		assertEquals("", o.getCallbackInvokeCounter());
		o.start();
		assertEquals("common,from,to,specific,", o.getCallbackInvokeCounter());
		o.finish();
		assertEquals("common,from,to,specific,common,", o.getCallbackInvokeCounter());
	}
    
	
}
