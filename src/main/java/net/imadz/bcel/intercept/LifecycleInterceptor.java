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
package net.imadz.bcel.intercept;

import net.imadz.bcel.intercept.helper.InterceptorHelper;
import net.imadz.lifecycle.meta.object.StateMachineObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LifecycleInterceptor<V, R> extends Interceptor<V, R> {

  private static final Logger logger = Logger.getLogger("Lifecycle Framework");

  public LifecycleInterceptor(Interceptor<V, R> next) {
    super(next);
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Intercepting....instantiating LifecycleInterceptor");
    }
  }

  @Override
  protected void preExec(InterceptContext<V, R> context) {
    super.preExec(context);
    LifecycleInterceptContext lifecycleContext = new LifecycleInterceptContext(context);
    final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
    stateMachine.doInterceptBefore(lifecycleContext);
  }

  @Override
  protected void postExec(InterceptContext<V, R> context) {
    super.postExec(context);
    final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
    LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
    stateMachine.doInterceptAfter(lifecycleContext);
  }

  @Override
  protected void handleException(InterceptContext<V, R> context, Throwable e) {
    final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
    LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
    stateMachine.doInterceptException(lifecycleContext);
    super.handleException(context, e);
  }

  @Override
  protected void cleanup(InterceptContext<V, R> context) {
    super.cleanup(context);
    logCleanup(context);
  }

  private void logCleanup(InterceptContext<V, R> context) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Intercepting....LifecycleInterceptor is doing cleanup ...");
      LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
      lifecycleContext.logResultFromContext();
    }
  }
}