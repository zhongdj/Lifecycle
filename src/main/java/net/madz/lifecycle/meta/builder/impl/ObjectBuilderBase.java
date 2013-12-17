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

import net.madz.lifecycle.meta.MetaObject;
import net.madz.lifecycle.meta.MetaType;
import net.madz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.madz.meta.MetaData;
import net.madz.verification.VerificationException;

public abstract class ObjectBuilderBase<SELF extends MetaObject<SELF, TYPE>, PARENT extends MetaData, TYPE extends MetaType<TYPE>> extends
        InheritableAnnotationMetaBuilderBase<SELF, PARENT> implements MetaObject<SELF, TYPE> {

    private TYPE metaType;

    public ObjectBuilderBase(PARENT parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean hasSuper() {
        return false;
    }

    @Override
    protected SELF findSuper(Class<?> metaClass) throws VerificationException {
        return null;
    }

    @Override
    public TYPE getMetaType() {
        return metaType;
    }

    protected void setMetaType(TYPE metaType) {
        this.metaType = metaType;
    }

    @Override
    public AnnotationMetaBuilder<SELF, PARENT> build(Class<?> klass, PARENT parent) throws VerificationException {
        setPrimaryKey(getMetaType().getPrimaryKey());
        addKey(getPrimaryKey());
        addKeys(getMetaType().getKeySet());
        return this;
    }
}