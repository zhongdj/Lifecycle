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
package net.madz.lifecycle.meta.builder.impl;

import net.madz.common.Dumper;
import net.madz.lifecycle.SyntaxErrors;
import net.madz.lifecycle.annotations.relation.Parent;
import net.madz.lifecycle.annotations.relation.RelateTo;
import net.madz.lifecycle.meta.builder.RelationMetaBuilder;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class RelationMetaBuilderImpl extends InheritableAnnotationMetaBuilderBase<RelationMetadata, StateMachineMetadata> implements RelationMetaBuilder {

    private StateMachineMetadata relatedStateMachine;
    private boolean parentRelation;

    public RelationMetaBuilderImpl(StateMachineMetadata parent, String name) {
        super(parent, name);
    }

    @Override
    public StateMachineMetadata getRelateToStateMachine() {
        return relatedStateMachine;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

    @Override
    public void dump(Dumper dumper) {
        // TODO Auto-generated method stub
    }

    @Override
    public RelationMetaBuilder build(Class<?> klass, StateMachineMetadata parent) throws VerificationException {
        super.build(klass, parent);
        configureSuper(klass);
        configureParent(klass);
        configureRelatedStateMachine(klass, parent);
        return this;
    }

    private void configureParent(Class<?> klass) {
        if ( !hasSuper(klass) ) {
            this.parentRelation = null != klass.getAnnotation(Parent.class);
        } else if ( hasLifecycleOverrideAnnotation(klass) ) {
            this.parentRelation = hasDeclaredAnnotation(klass, Parent.class);
        } else {
            if ( hasDeclaredAnnotation(klass, Parent.class) ) {
                this.parentRelation = true;
            } else {
                this.parentRelation = parent.getRelationMetadata(getSuperMetaClass(klass)).isParent();
            }
        }
    }

    @Override
    protected RelationMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return parent.getSuper().getDeclaredRelationMetadata(metaClass);
    }

    protected void verifyRelateTo(Class<?> clazz) throws VerificationException {
        if ( !hasSuper(clazz) ) {
            if ( null == clazz.getAnnotation(RelateTo.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.RELATION_NO_RELATED_TO_DEFINED, clazz);
            }
        } else if ( hasLifecycleOverrideAnnotation(clazz) ) {
            if ( !hasDeclaredAnnotation(clazz, RelateTo.class) ) {
                throw newVerificationException(clazz.getName(), SyntaxErrors.RELATION_NO_RELATED_TO_DEFINED, clazz);
            }
        } else {
            // TODO It is not necessary ..
            if ( null != clazz.getAnnotation(RelateTo.class) ) {
                verifyRelateTo(getSuperMetaClass(clazz));
            }
        }
    }

    protected void configureRelatedStateMachine(Class<?> klass, StateMachineMetadata parent) throws VerificationException {
        verifyRelateTo(klass);
        if ( hasSuper() && isOverriding() ) {
            final RelateTo relateTo = getDeclaredAnnotation(klass, RelateTo.class);
            if ( null != relateTo ) {
                relatedStateMachine = parent.getRegistry().loadStateMachineMetadata(relateTo.value(), null);
            }
        } else {
            final Class<?> relateStateMachineClass = klass.getAnnotation(RelateTo.class).value();
            relatedStateMachine = parent.getRegistry().loadStateMachineMetadata(relateStateMachineClass, null);
        }
    }

    @Override
    public boolean isParent() {
        return parentRelation;
    }

    @Override
    protected boolean extendsSuperKeySet() {
        return true;
    }
}
