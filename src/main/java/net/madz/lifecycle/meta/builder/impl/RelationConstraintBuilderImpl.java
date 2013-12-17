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

import java.util.LinkedList;
import java.util.List;

import net.madz.common.Dumper;
import net.madz.lifecycle.meta.builder.RelationConstraintBuilder;
import net.madz.lifecycle.meta.builder.StateMetaBuilder;
import net.madz.lifecycle.meta.object.ErrorMessageObject;
import net.madz.lifecycle.meta.type.RelationConstraintMetadata;
import net.madz.lifecycle.meta.type.RelationMetadata;
import net.madz.lifecycle.meta.type.StateMachineMetadata;
import net.madz.lifecycle.meta.type.StateMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class RelationConstraintBuilderImpl extends InheritableAnnotationMetaBuilderBase<RelationConstraintMetadata, StateMetadata> implements
        RelationConstraintBuilder {

    private StateMachineMetadata relatedStateMachine;
    private final LinkedList<StateMetadata> onStates = new LinkedList<>();
    private final LinkedList<ErrorMessageObject> errorMessageObjects = new LinkedList<>();
    private boolean nullable;
    private RelationMetadata relationMetadata;

    @Override
    public StateMachineMetadata getRelatedStateMachine() {
        return relatedStateMachine;
    }

    public RelationConstraintBuilderImpl(StateMetaBuilder parent, String name, List<StateMetadata> onStates, List<ErrorMessageObject> errorMessageObjects,
            StateMachineMetadata stateMachineMetadata, boolean nullable) {
        super(parent, name);
        this.onStates.addAll(onStates);
        this.errorMessageObjects.addAll(errorMessageObjects);
        this.relatedStateMachine = stateMachineMetadata;
        this.nullable = nullable;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

    @Override
    public RelationConstraintBuilder build(Class<?> klass, StateMetadata parent) throws VerificationException {
        super.build(klass, parent);
        this.relationMetadata = parent.getStateMachine().getRelationMetadata(klass);
        return this;
    }

    @Override
    public StateMetadata[] getOnStates() {
        return this.onStates.toArray(new StateMetadata[0]);
    }

    @Override
    public ErrorMessageObject[] getErrorMessageObjects() {
        return this.errorMessageObjects.toArray(new ErrorMessageObject[0]);
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void dump(Dumper dumper) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void verifySuper(Class<?> metaClass) throws VerificationException {}

    @Override
    protected RelationConstraintMetadata findSuper(Class<?> metaClass) throws VerificationException {
        return null;
    }

    @Override
    protected boolean extendsSuperKeySet() {
        return true;
    }

    @Override
    public RelationMetadata getRelationMetadata() {
        return relationMetadata;
    }
}
