package net.imadz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

public class MethodOverridingUtils {

  private MethodOverridingUtils() {
  }

  public static boolean overridesBy(Method subclassMethod, Method potentialOverridenMethod) {
    if (!subclassMethod.getName().equals(potentialOverridenMethod.getName())) {
      return false;
    }
    if (!subclassMethod.getReturnType().equals(potentialOverridenMethod.getReturnType())) {
      return false;
    }
    final Class<?>[] subParamTypes = subclassMethod.getParameterTypes();
    final Class<?>[] superParameterTypes = potentialOverridenMethod.getParameterTypes();
    if (subParamTypes.length != superParameterTypes.length) {
      return false;
    }
    for (int i = 0; i < subParamTypes.length; i++) {
      if (!superParameterTypes[i].isAssignableFrom(subParamTypes[i])) {
        return false;
      }
    }
    return potentialOverridenMethod.getDeclaringClass().isAssignableFrom(subclassMethod.getDeclaringClass());
  }
}
