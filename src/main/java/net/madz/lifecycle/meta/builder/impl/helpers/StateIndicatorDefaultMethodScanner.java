package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.Method;

import net.madz.lifecycle.annotations.state.Converter;
import net.madz.util.MethodScanCallback;

public final class StateIndicatorDefaultMethodScanner implements MethodScanCallback {

    private Method defaultStateGetterMethod = null;

    @Override
    public boolean onMethodFound(Method method) {
        if ( "getState".equals(method.getName()) ) {
            if ( String.class.equals(method.getReturnType()) && null == defaultStateGetterMethod ) {
                defaultStateGetterMethod = method;
                return true;
            } else if ( null != method.getAnnotation(Converter.class) && null == defaultStateGetterMethod ) {
                defaultStateGetterMethod = method;
                return true;
            }
        }
        return false;
    }

    public Method getDefaultMethod() {
        return defaultStateGetterMethod;
    }
}