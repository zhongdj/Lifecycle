package net.imadz.lifecycle.engine.callback;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.callback.PostStateChange;
import net.imadz.verification.VerificationException;
import org.junit.BeforeClass;

public class NegativeCallbackTestMetadata extends CallbackTestMetadata {

  @BeforeClass
  public static void setup() throws VerificationException {
    registerMetaFromClass(NegativeCallbackTestMetadata.class);
  }

  @LifecycleMeta(CallbackStateMachine.class)
  public static class PostCallbackWithPostStateChangeThrowException extends CallbackObjectBase {

    @PostStateChange
    public void interceptPostStateChange(LifecycleContext<PostCallbackFromAnyToAny, String> context) {
      this.callbackInvokeCounter++;
      throw new NullPointerException();
    }
  }
}
