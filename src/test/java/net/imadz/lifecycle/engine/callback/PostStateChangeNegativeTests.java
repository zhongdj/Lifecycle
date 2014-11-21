package net.imadz.lifecycle.engine.callback;

import static org.junit.Assert.assertEquals;
import net.imadz.lifecycle.LifecycleException;

import org.junit.Test;

public class PostStateChangeNegativeTests extends NegativeCallbackTestMetadata {

	@Test(expected = NullPointerException.class)
	public void should_catch_exception_thrown_in_postStateChange_method() throws Throwable {
		try {
			final PostCallbackWithPostStateChangeThrowException o = new PostCallbackWithPostStateChangeThrowException();
			assertEquals(0, o.getCallbackInvokeCounter());
			o.start();
		} catch (LifecycleException ex) {
			throw ex.getCause();
		}
	}
}
