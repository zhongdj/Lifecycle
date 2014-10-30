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
package net.imadz.bcel.intercept.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.imadz.bcel.intercept.InterceptContext;
import net.imadz.lifecycle.AbsStateMachineRegistry;
import net.imadz.lifecycle.annotations.ReactiveObject;
import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.meta.object.StateMachineObject;
import net.imadz.util.StringUtil;
import net.imadz.utils.Null;
import net.imadz.verification.VerificationException;

public final class InterceptorHelper {

    public static synchronized StateMachineObject<?> lookupStateMachine(InterceptContext<?, ?> context) {
        try {
            return AbsStateMachineRegistry.getInstance().loadStateMachineObject(extractLifecycleMetaClass(context));
        } catch (VerificationException e) {
            throw new IllegalStateException("Should not encounter syntax verification exception at intercepting runtime", e);
        }
    }

    private static boolean doMatchMethod(final Method subClassMethod, final Class<?> klass) {
        if ( klass == Object.class ) return false;
        try {
            final Method tmpMethod = klass.getMethod(subClassMethod.getName(), subClassMethod.getParameterTypes());
            final Event transition = subClassMethod.getAnnotation(Event.class);
            final Event tmpTransition = tmpMethod.getAnnotation(Event.class);
            if ( hasSameTransitionKey(transition, tmpTransition) || hasSameTransitionName(subClassMethod, transition, tmpTransition) ) {
                return true;
            }
        } catch (NoSuchMethodException ignore) {}
        return false;
    }

    private static Class<? extends Object> extractLifecycleMetaClass(InterceptContext<?, ?> context) {
        if ( null != context.getTarget().getClass().getAnnotation(ReactiveObject.class) ) {
            Method method = context.getMethod();
            Object target = context.getTarget();
            Class<?> lifecycleMetaClass = findLifecycleMetaClass(target.getClass(), method);
            return lifecycleMetaClass;
        } else {
            return context.getTarget().getClass();
        }
    }

    private static Class<?> findLifecycleMetaClass(final Class<?> implClass, final Method method) {
        final Event transition = method.getAnnotation(Event.class);
        if ( Null.class == transition.value() ) {
            throw new IllegalStateException("With @ReactiveObject, transition.value has to be explicitly specified.");
        } else {
            return scanMethodsOnClasses(implClass.getInterfaces(), method);
        }
    }

    private static boolean hasSameTransitionKey(final Event transition, final Event tmpTransition) {
        return tmpTransition.value() != Null.class && transition.value() == tmpTransition.value();
    }

    private static boolean hasSameTransitionName(final Method subClassMethod, final Event transition, final Event tmpTransition) {
        return tmpTransition.value() == Null.class
                && transition.value().getSimpleName().equalsIgnoreCase(StringUtil.toUppercaseFirstCharacter(subClassMethod.getName()));
    }

    private static void populateSuperclasses(final ArrayList<Class<?>> superclasses, final Class<?> klass) {
        if ( null != klass.getSuperclass() && Object.class != klass ) {
            superclasses.add(klass.getSuperclass());
        }
        for ( Class<?> interfaze : klass.getInterfaces() ) {
            superclasses.add(interfaze);
        }
    }

    private static Class<?> scanMethodsOnClasses(Class<?>[] klasses, final Method subClassMethod) {
        if ( 0 == klasses.length ) throw new IllegalArgumentException();
        final ArrayList<Class<?>> superclasses = new ArrayList<Class<?>>();
        for ( final Class<?> klass : klasses ) {
            if ( doMatchMethod(subClassMethod, klass) ) {
                return klass;
            }
            populateSuperclasses(superclasses, klass);
        }
        return scanMethodsOnClasses(superclasses.toArray(new Class<?>[superclasses.size()]), subClassMethod);
    }
}
