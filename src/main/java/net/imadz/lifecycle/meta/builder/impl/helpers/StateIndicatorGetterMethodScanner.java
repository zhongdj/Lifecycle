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
package net.imadz.lifecycle.meta.builder.impl.helpers;

import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.imadz.util.MethodScanCallback;
import net.imadz.verification.VerificationFailureSet;

import java.lang.reflect.Method;

import static net.imadz.lifecycle.meta.builder.impl.helpers.MethodOverridingUtils.overridesBy;

public final class StateIndicatorGetterMethodScanner implements MethodScanCallback {

  private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
  private Method stateGetterMethod = null;
  private boolean overridingFound = false;
  private final Class<?> klass;
  private final VerificationFailureSet failureSet;

  public StateIndicatorGetterMethodScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, Class<?> klass,
      final VerificationFailureSet failureSet) {
    this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
    this.klass = klass;
    this.failureSet = failureSet;
  }

  @Override
  public boolean onMethodFound(Method method) {
    if (method.isBridge()) {
      return false;
    }
    if (null == stateGetterMethod && null != method.getAnnotation(StateIndicator.class)) {
      stateGetterMethod = method;
      overridingFound = null != method.getAnnotation(LifecycleOverride.class);
      return false;
    } else if (null != stateGetterMethod && null != method.getAnnotation(StateIndicator.class)) {
      if (!overridingFound) {
        if (!overridesBy(stateGetterMethod, method)) {
          failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(this.stateMachineObjectBuilderImpl.getDottedPath(),
              SyntaxErrors.STATE_INDICATOR_MULTIPLE_STATE_INDICATOR_ERROR, klass));
        }
        return true;
      }
    }
    return false;
  }

  public Method getStateGetterMethod() {
    return stateGetterMethod;
  }
}