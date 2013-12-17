package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.madz.util.MethodScanCallback;

public final class MethodSignatureScanner implements MethodScanCallback {

    private Method targetMethod = null;
    private String targetMethodName = null;
    private Class<?>[] parameterTypes = null;

    public MethodSignatureScanner(String setterName, Class<?>[] classes) {
        this.targetMethodName = setterName;
        this.parameterTypes = classes;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if ( null == targetMethod && targetMethodName.equals(method.getName()) && Arrays.equals(method.getParameterTypes(), parameterTypes) ) {
            targetMethod = method;
            return true;
        }
        return false;
    }

    public Method getMethod() {
        return targetMethod;
    }
}