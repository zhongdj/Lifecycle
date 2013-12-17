package net.madz.util;

import java.lang.reflect.Method;

public interface MethodScanCallback {

    boolean onMethodFound(Method method);
}