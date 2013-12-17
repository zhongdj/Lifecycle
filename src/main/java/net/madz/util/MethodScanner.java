package net.madz.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodScanner {

    private static final ArrayList<Class<?>> SHOULD_TERMINATE_SCAN = new ArrayList<>();

    private MethodScanner() {}

    public static void scanMethodsOnClasses(Class<?> klass, final MethodScanCallback scanner) {
        scanMethodsOnClasses(new Class<?>[] { klass }, scanner);
    }

    private static void scanMethodsOnClasses(Class<?>[] klasses, final MethodScanCallback scanner) {
        if ( 0 == klasses.length ) {
            return;
        }
        ArrayList<Class<?>> superclasses = null;
        for ( final Class<?> klass : klasses ) {
            superclasses = scanClass(scanner, klass);
            if ( 0 >= superclasses.size() ) {
                return;
            }
        }
        scanMethodsOnClasses(superclasses.toArray(new Class<?>[superclasses.size()]), scanner);
    }

    private static ArrayList<Class<?>> scanClass(final MethodScanCallback scanner, Class<?> klass) {
        if ( processDeclaredMethods(scanner, klass) ) {
            return SHOULD_TERMINATE_SCAN;
        } else {
            return populateSuperclasses(klass);
        }
    }

    private static boolean processDeclaredMethods(final MethodScanCallback scanner, Class<?> klass) {
        for ( Method method : klass.getDeclaredMethods() ) {
            if ( scanner.onMethodFound(method) ) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<Class<?>> populateSuperclasses(Class<?> klass) {
        final ArrayList<Class<?>> superclasses = new ArrayList<>();
        if ( hasSuperclass(klass) ) {
            superclasses.add(klass.getSuperclass());
        }
        for ( Class<?> interfaze : klass.getInterfaces() ) {
            superclasses.add(interfaze);
        }
        return superclasses;
    }

    private static boolean hasSuperclass(Class<?> klass) {
        return null != klass.getSuperclass() && Object.class != klass;
    }
}
