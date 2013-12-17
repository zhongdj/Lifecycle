package net.madz.utils;

import java.lang.reflect.Method;

public class ClassUtils {

    public static Method findDeclaredMethod(Class<?> klass, String methodName, Class<?>[] parameterTypes) {
        try {
            return klass.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isDefaultStyle(final Class<?> keyClass) {
        return Null.class.equals(keyClass);
    }
}
