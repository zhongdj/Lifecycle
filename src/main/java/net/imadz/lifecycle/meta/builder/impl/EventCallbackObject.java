package net.imadz.lifecycle.meta.builder.impl;

import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.LifecycleException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventCallbackObject {

  private final Class<?> eventClass;
  private final String eventName;
  private final Method callbackMethod;

  public EventCallbackObject(Class<?> eventClass, Method method) {
    this.eventClass = eventClass;
    this.eventName = eventClass.getSimpleName();
    this.callbackMethod = method;
  }

  protected Object evaluateTarget(Object target) {
    return target;
  }

  public String getEventName() {
    return eventName;
  }

  public void doCallback(LifecycleContext<?, ?> callbackContext) {
    try {
      callbackMethod.invoke(evaluateTarget(callbackContext.getTarget()),
          new Object[] {callbackContext});
    } catch (Throwable e) {
      if (e instanceof InvocationTargetException) {
        final Throwable target = ((InvocationTargetException) e)
            .getTargetException();
        final LifecycleException lifecycleException = new LifecycleException(
            getClass(), LifecycleCommonErrors.BUNDLE,
            LifecycleCommonErrors.CALLBACK_EXCEPTION_OCCOURRED,
            callbackMethod, target);
        lifecycleException.initCause(target);
        throw lifecycleException;
      } else if (e instanceof IllegalAccessException
          | e instanceof IllegalArgumentException
          | e instanceof InvocationTargetException) {
        throw new LifecycleException(getClass(),
            LifecycleCommonErrors.BUNDLE,
            LifecycleCommonErrors.CALLBACK_EXCEPTION_OCCOURRED,
            callbackMethod, e);
      } else {
        throw new RuntimeException(e);
      }
    }
  }

}
