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
package net.madz.lifecycle.meta.builder.impl.helpers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.madz.lifecycle.meta.builder.impl.RelationObjectBuilderImpl;
import net.madz.lifecycle.meta.object.RelationObject;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.verification.VerificationException;

public class RelationObjectConfigure {

    private final ArrayList<RelationMetadata> extendedRelationMetadata;
    private final Class<?> klass;
    private final StateMachineObject<?> parent;

    public RelationObjectConfigure(final Class<?> klass, final ArrayList<RelationMetadata> extendedRelationMetadata, final StateMachineObject<?> parent) {
        super();
        this.extendedRelationMetadata = extendedRelationMetadata;
        this.klass = klass;
        this.parent = parent;
    }

    public RelationObject configure(final StateMachineMetadata relatedStateMachine, final Object relationKey, final AccessibleObject obj)
            throws VerificationException {
        final RelationMetadata relationMetadata = relatedStateMachine.getRelationMetadata(relationKey);
        if ( extendedRelationMetadata.contains(relationMetadata) ) return null;
        if ( relationMetadata.hasSuper() ) {
            markExtendedRelationMetadata(extendedRelationMetadata, relationMetadata);
        }
        return createRelationObject(obj, relationMetadata);
    }

    private RelationObject createRelationObject(final AccessibleObject obj, final RelationMetadata relationMetadata) throws VerificationException {
        if ( obj instanceof Field ) {
            return new RelationObjectBuilderImpl(parent, (Field) obj, relationMetadata).build(klass, parent).getMetaData();
        } else if ( obj instanceof Method ) {
            return new RelationObjectBuilderImpl(parent, (Method) obj, relationMetadata).build(klass, parent).getMetaData();
        } else {
            throw new UnsupportedOperationException("For now, we only support Field and Method when configuring relation object in class " + getClass());
        }
    }

    private void markExtendedRelationMetadata(final ArrayList<RelationMetadata> extendedRelationMetadata, final RelationMetadata relationMetadata) {
        extendedRelationMetadata.add(relationMetadata);
        if ( relationMetadata.hasSuper() ) {
            markExtendedRelationMetadata(extendedRelationMetadata, relationMetadata.getSuper());
        }
    }
}
