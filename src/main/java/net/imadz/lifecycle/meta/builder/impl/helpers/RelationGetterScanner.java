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

import net.imadz.lifecycle.annotations.relation.Relation;
import net.imadz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.imadz.lifecycle.meta.type.RelationConstraintMetadata;
import net.imadz.util.MethodScanCallback;
import net.imadz.utils.ClassUtils;

import java.lang.reflect.Method;

public final class RelationGetterScanner implements MethodScanCallback {

  private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
  private RelationConstraintMetadata relationMetadata;
  private boolean covered = false;

  public RelationGetterScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, RelationConstraintMetadata relation) {
    this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
    this.relationMetadata = relation;
  }

  @Override
  public boolean onMethodFound(Method method) {
    if (!Relation.Utils.isRelationMethod(method)) {
      return false;
    }
    if (matchRelationWithName(method.getAnnotation(Relation.class), method.getName())) {
      covered = true;
    } else if (matchRelationWithKeyClass(method.getAnnotation(Relation.class))) {
      covered = true;
    }
    return covered;
  }

  public boolean isCovered() {
    return covered;
  }

  private boolean matchRelationWithKeyClass(final Relation relation) {
    final Class<?> relationKeyClass = relation.value();
    return !ClassUtils.isDefaultStyle(relationKeyClass) && this.stateMachineObjectBuilderImpl.isKeyOfRelationMetadata(relationMetadata,
        relationKeyClass);
  }

  private boolean matchRelationWithName(final Relation relation, final String methodName) {
    final Class<?> relationKeyClass = relation.value();
    final String relationName = methodName.substring(3); // trim "get"
    return ClassUtils.isDefaultStyle(relationKeyClass) && this.stateMachineObjectBuilderImpl.isKeyOfRelationMetadata(relationMetadata, relationName);
  }
}