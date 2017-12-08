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

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Interceptor<V, R> {

  private static final Logger logger = Logger.getLogger("Lifecycle Framework");
  private Interceptor<V, R> next;

  public Interceptor() {
  }

  public Interceptor(Interceptor<V, R> next) {
    super();
    this.next = next;
  }

  public R aroundInvoke(InterceptContext<V, R> context, Callable<R> callable) throws Throwable {
    try {
      preExec(context);
      R result = next.aroundInvoke(context, callable);
      postExec(context);
      context.markSuccess();
      return result;
    } catch (Throwable e) {
      context.markFail(e);
      handleException(context, e);
      throw e;
    } finally {
      cleanup(context);
    }
  }

  protected void handleException(InterceptContext<V, R> context, Throwable e) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("intercepting with:" + getClass().getName() + " @handleException");
    }
    logger.log(Level.SEVERE, e.getMessage(), e);
  }

  protected void cleanup(InterceptContext<V, R> context) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("intercepting with :" + getClass().getName() + " @cleanup");
    }
  }

  protected void postExec(InterceptContext<V, R> context) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("intercepting with :" + getClass().getName() + " @postExec");
    }
  }

  protected void preExec(InterceptContext<V, R> context) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("intercepting with :" + getClass().getName() + " @preExec");
    }
  }
}