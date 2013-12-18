/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.imadz.util;

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
