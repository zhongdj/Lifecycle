package net.imadz.lifecycle.meta.builder.impl;

import net.imadz.util.Readable;

import java.lang.reflect.Method;

public class RelationalEventCallbackObject extends EventCallbackObject {

  private final Readable<?> readAccessor;

  public RelationalEventCallbackObject(Class<?> eventClass,
      Readable<?> readAccessor, Method method) {
    super(eventClass, method);
    this.readAccessor = readAccessor;
  }

  @Override
  protected Object evaluateTarget(Object target) {
    return this.readAccessor.read(target);
  }
}
