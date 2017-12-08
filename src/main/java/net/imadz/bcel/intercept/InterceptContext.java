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

import net.imadz.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InterceptContext<V, R> {

  private static final Logger logger = Logger.getLogger("Lifecycle Framework");
  private static final Level FINE = Level.FINE;
  private final HashMap<Object, Object> data = new HashMap<Object, Object>();
  private final long startTime;
  private final Annotation[] annotation;
  private final Class<?> klass;
  private final Method method;
  private final V target;
  private final Object[] arguments;
  private long endTime;
  private boolean success;
  private Throwable failureCause;

  public InterceptContext(final Class<?> klass, final V target, final String methodName, final Class<?>[] argsType, final Object[] arguments) {
    this.klass = klass;
    this.method = ClassUtils.findDeclaredMethod(klass, methodName, argsType);
    this.annotation = method.getAnnotations();
    this.target = target;
    this.startTime = System.currentTimeMillis();
    this.arguments = arguments;
    logInterceptPoint(klass, methodName);
  }

  public void doTerminate() {
    this.endTime = System.currentTimeMillis();
  }

  public Annotation[] getAnnotation() {
    return annotation;
  }

  public Object[] getArguments() {
    return this.arguments;
  }

  public long getEndTime() {
    return endTime;
  }

  public Throwable getFailureCause() {
    return failureCause;
  }

  public Class<?> getKlass() {
    return klass;
  }

  public Method getMethod() {
    return method;
  }

  public long getStartTime() {
    return startTime;
  }

  public V getTarget() {
    return target;
  }

  public boolean isSuccess() {
    return success;
  }

  private void logInterceptPoint(final Class<?> klass, final String methodName) {
    if (logger.isLoggable(FINE)) {
      final StringBuilder sb = new StringBuilder(" ");
      for (Object o : this.arguments) {
        sb.append(String.valueOf(o)).append(" ");
      }
      logger.fine("Found Intercept Point: " + klass + "." + methodName + "( " + sb.toString() + " )");
      logger.fine("Intercepting....instatiating InterceptContext ...");
    }
  }

  public void markFail(Throwable failureCause) {
    this.success = false;
    this.failureCause = failureCause;
    doTerminate();
  }

  public void markSuccess() {
    this.success = true;
    this.failureCause = null;
    doTerminate();
  }

  public void putObject(Object key, Object value) {
    data.put(key, value);
  }

  public Object getObject(Object key) {
    return data.get(key);
  }
}