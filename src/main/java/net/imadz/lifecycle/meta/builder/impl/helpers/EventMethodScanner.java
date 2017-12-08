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

import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.meta.type.EventMetadata;
import net.imadz.util.MethodScanCallback;
import net.imadz.util.StringUtil;
import net.imadz.utils.Null;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class EventMethodScanner implements MethodScanCallback {

  private final ArrayList<Method> eventMethodList = new ArrayList<Method>();
  private final EventMetadata event;

  public EventMethodScanner(final EventMetadata event) {
    this.event = event;
  }

  @Override
  public boolean onMethodFound(Method method) {
    if (method.isBridge()) {
      return false;
    }
    final Event eventAnno = method.getAnnotation(Event.class);
    if (null == eventAnno) {
      return false;
    }
    final Class<?> eventKey = eventAnno.value();
    if (matchedEventPrimaryKey(eventKey, event.getPrimaryKey())) {
      eventMethodList.add(method);
    } else if (matchedEventName(eventKey, method.getName(), event.getDottedPath().getName())) {
      eventMethodList.add(method);
    }
    return false;
  }

  private boolean matchedEventName(final Class<?> eventKey, String methodName, final String eventName) {
    return isDefaultStyle(eventKey) && StringUtil.toUppercaseFirstCharacter(methodName).equals(eventName);
  }

  private boolean matchedEventPrimaryKey(final Class<?> eventKey, Object primaryKey) {
    return !isDefaultStyle(eventKey) && eventKey.equals(primaryKey);
  }

  public Method[] getEventMethods() {
    return eventMethodList.toArray(new Method[0]);
  }

  private boolean isDefaultStyle(final Class<?> eventKey) {
    return Null.class == eventKey;
  }
}