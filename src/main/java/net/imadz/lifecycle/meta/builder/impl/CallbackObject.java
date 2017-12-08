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
package net.imadz.lifecycle.meta.builder.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.imadz.lifecycle.LifecycleCommonErrors;
import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.LifecycleException;
import net.imadz.lifecycle.meta.builder.impl.helpers.MethodWrapper;

import static net.imadz.lifecycle.meta.builder.impl.helpers.MethodOverridingUtils.overridesBy;

public class CallbackObject implements java.lang.Comparable<CallbackObject> {

	public static final int COMMON = 0;
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final int SPECIFIC = 3;

	private final String fromStateName;
	private final String toStateName;
	private int priority; // default 10
	private int inheritanceLevel;// defult 0, minus 1 from bottom to super class
	private int generalize; // set 0 for common, 1 for from/to, 2 for specific
	private Method method;

	public CallbackObject(String fromStateName, String toStateName,
			MethodWrapper wrappedCallbackMethod) {
		super();
		this.fromStateName = fromStateName;
		this.toStateName = toStateName;
		this.method = wrappedCallbackMethod.getMethod();
		this.priority = wrappedCallbackMethod.getPriority();
		this.inheritanceLevel = wrappedCallbackMethod.getInheritanceLevel();
		this.generalize = wrappedCallbackMethod.getGeneralize();
	}

	public boolean overrides(CallbackObject theOther) {
		return overridesBy(method, theOther.method) || overridesBy(theOther.method, method);
	}

	public boolean matches(final LifecycleContext<?, ?> callbackContext) {
		String fromState = callbackContext.getFromStateName();
		String toStateName = callbackContext.getToStateName();
		if (this.fromStateName.equals(fromState)
				&& this.toStateName.equals(toStateName)) {
			return true;
		}
		return false;
	}

	public void doCallback(final LifecycleContext<?, ?> callbackContext) {
		try {
			Object evaluateTarget = evaluateTarget(callbackContext.getTarget());
			method.invoke(evaluateTarget, callbackContext);
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				final Throwable target = ((InvocationTargetException) e)
						.getTargetException();
				final LifecycleException lifecycleException = new LifecycleException(
						getClass(), LifecycleCommonErrors.BUNDLE,
						LifecycleCommonErrors.CALLBACK_EXCEPTION_OCCOURRED,
						method, target);
				lifecycleException.initCause(target);
				throw lifecycleException;
			} else if (e instanceof IllegalAccessException
					| e instanceof IllegalArgumentException
					| e instanceof InvocationTargetException)
				throw new LifecycleException(getClass(),
						LifecycleCommonErrors.BUNDLE,
						LifecycleCommonErrors.CALLBACK_EXCEPTION_OCCOURRED,
						method, e);
			else
				throw new RuntimeException(e);
		}
	}

	protected Object evaluateTarget(Object target) {
		return target;
	}

	public int getPriority() {
		return priority;
	}

	public int getInheritanceLevel() {
		return inheritanceLevel;
	}

	@Override
	public int compareTo(CallbackObject other) {
		final int comparePriority = getPriority() - other.getPriority();
		if (comparePriority != 0) {
			return comparePriority;
		} else {
			final int compareInheritanceLevel = getInheritanceLevel()
					- other.getInheritanceLevel();
			if (compareInheritanceLevel != 0) {
				return compareInheritanceLevel;
			} else {
				final int compareGeneralize = getGeneralize()
						- other.getGeneralize();
				if (compareGeneralize != 0) {
					return compareGeneralize;
				} else
					return 0;
			}
		}
	}

	public int getGeneralize() {
		return this.generalize;
	}
}
