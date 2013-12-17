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
package net.madz.lifecycle.impl;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.madz.bcel.intercept.LifecycleInterceptContext;
import net.madz.lifecycle.LifecycleContext;

public class LifecycleContextImpl<T, S> implements LifecycleContext<T, S> {

    private final T target;
    private final S fromState;
    private final String fromStateName;
    private final S toState;
    private final String toStateName;
    private final Method transitionMethod;
    private final Object[] arguments;

    @SuppressWarnings("unchecked")
    public LifecycleContextImpl(LifecycleInterceptContext context, S fromState, S toState) {
        this.target = (T) context.getTarget();
        this.fromStateName = context.getFromState();
        this.fromState = fromState;
        if ( null == context.getToState() ) {
            this.toState = null;
        } else {
            this.toState = toState;
        }
        this.toStateName = context.getToState();
        this.transitionMethod = context.getMethod();
        this.arguments = context.getArguments();
    }

    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public S getFromState() {
        return fromState;
    }

    @Override
    public S getToState() {
        return toState;
    }

    @Override
    public Method getTransitionMethod() {
        return transitionMethod;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "LifecycleContextImpl [target=" + target + ", fromState=" + fromState + ", toState=" + toState + ", transitionMethod=" + transitionMethod
                + ", arguments=" + Arrays.toString(arguments) + "]";
    }

    @Override
    public String getFromStateName() {
        return this.fromStateName;
    }

    @Override
    public String getToStateName() {
        return this.toStateName;
    }
}
