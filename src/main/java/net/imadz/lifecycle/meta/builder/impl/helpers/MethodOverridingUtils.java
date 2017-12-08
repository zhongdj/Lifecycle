package net.imadz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

public class MethodOverridingUtils {

  private MethodOverridingUtils(){}

  public static boolean overridesBy(Method subclassMethod, Method potentialOverridenMethod) {
    if (!subclassMethod.getName().equals(potentialOverridenMethod.getName())) return false;
    if (!subclassMethod.getReturnType().equals(potentialOverridenMethod.getReturnType())) return false;
    if (subclassMethod.getParameterTypes().length != potentialOverridenMethod.getParameterTypes().length) return false;
    return potentialOverridenMethod.getDeclaringClass().isAssignableFrom(subclassMethod.getDeclaringClass());
  }
}
