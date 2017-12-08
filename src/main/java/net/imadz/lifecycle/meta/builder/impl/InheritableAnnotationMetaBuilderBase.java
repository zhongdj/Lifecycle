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

import net.imadz.lifecycle.annotations.state.LifecycleOverride;
import net.imadz.lifecycle.meta.Inheritable;
import net.imadz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.imadz.meta.MetaData;
import net.imadz.verification.VerificationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class InheritableAnnotationMetaBuilderBase<SELF extends MetaData, PARENT extends MetaData> extends AnnotationMetaBuilderBase<SELF,
    PARENT>
    implements AnnotationMetaBuilder<SELF, PARENT>, Inheritable<SELF> {

  protected static Logger logger = Logger.getLogger("Lifecycle Framework");
  private SELF superMeta;
  private boolean overriding;

  protected InheritableAnnotationMetaBuilderBase(PARENT parent, String name) {
    super(parent, name);
    if (logger.isLoggable(Level.FINE)) {
      logger.fine(getDottedPath().getAbsoluteName());
    }
  }

  @Override
  public boolean hasSuper() {
    return null != superMeta;
  }

  @Override
  public SELF getSuper() {
    return superMeta;
  }

  @Override
  public boolean isOverriding() {
    return overriding;
  }

  protected void setOverriding(boolean overriding) {
    this.overriding = overriding;
  }

  protected void setSuper(SELF superMeta) {
    this.superMeta = superMeta;
  }

  protected void configureSuper(Class<?> metaClass) throws VerificationException {
    if (!hasSuper(metaClass)) {
      this.setOverriding(false);
      return;
    }
    verifySuper(metaClass);
    setSuper(findSuper(getSuperMetaClass(metaClass)));
    if (hasLifecycleOverrideAnnotation(metaClass)) {
      this.setOverriding(true);
    } else {
      this.setOverriding(false);
    }
    if (extendsSuperKeySet()) {
      final Iterator<Object> iterator = getSuper().getKeySet().iterator();
      while (iterator.hasNext()) {
        addKey(iterator.next());
      }
    }
  }

  protected boolean extendsSuperKeySet() {
    return false;
  }

  protected void verifySuper(Class<?> metaClass) throws VerificationException {
  }

  public boolean hasLifecycleOverrideAnnotation(AnnotatedElement metaClass) {
    return null != metaClass.getAnnotation(LifecycleOverride.class);
  }

  protected abstract SELF findSuper(Class<?> metaClass) throws VerificationException;

  protected Class<?> getSuperMetaClass(Class<?> clazz) {
    // if ( !hasSuper(clazz) ) {
    // throw new IllegalStateException("Class " + clazz +
    // " has no super class");
    // }
    if (null != clazz.getSuperclass() && !Object.class.equals(clazz.getSuperclass())) {
      return clazz.getSuperclass();
    } else {
      // if clazz is interface or clazz implements an interface.
      return clazz.getInterfaces()[0];
    }
  }

  protected boolean hasSuper(Class<?> metaClass) {
    return (null != metaClass.getSuperclass() && !Object.class.equals(metaClass.getSuperclass())) || (1 <= metaClass.getInterfaces().length);
  }

  protected boolean hasDeclaredAnnotation(Class<?> clazz, final Class<? extends Annotation> annotationClass) {
    for (Annotation anno : clazz.getDeclaredAnnotations()) {
      if (annotationClass == anno.annotationType()) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  protected <A extends Annotation> A getDeclaredAnnotation(Class<?> metaClass, Class<A> annotationClass) {
    for (Annotation annotation : metaClass.getDeclaredAnnotations()) {
      if (annotationClass == annotation.annotationType()) {
        return (A) annotation;
      }
    }
    return null;
  }
}