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

import net.imadz.common.DottedPath;
import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.meta.builder.AnnotationMetaBuilder;
import net.imadz.lifecycle.meta.type.LifecycleMetaRegistry;
import net.imadz.meta.MetaData;
import net.imadz.meta.impl.MetaDataBuilderBase;
import net.imadz.utils.BundleUtils;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailure;

public abstract class AnnotationMetaBuilderBase<SELF extends MetaData, PARENT extends MetaData> extends MetaDataBuilderBase<SELF, PARENT> implements
        AnnotationMetaBuilder<SELF, PARENT> {

    protected LifecycleMetaRegistry registry;
    private Object primaryKey;

    public AnnotationMetaBuilderBase(PARENT parent, String name) {
        super(parent, name);
    }

    public LifecycleMetaRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(LifecycleMetaRegistry registry) {
        this.registry = registry;
    }

    public VerificationException newVerificationException(String dottedPathName, String errorCode, Object... args) {
        return new VerificationException(newVerificationFailure(dottedPathName, errorCode, args));
    }

    public VerificationException newVerificationException(DottedPath dottedPath, String errorCode, Object... args) {
        return new VerificationException(newVerificationFailure(dottedPath.getAbsoluteName(), errorCode, args));
    }

    public VerificationFailure newVerificationFailure(DottedPath dottedPath, String errorCode, Object... args) {
        return newVerificationFailure(dottedPath.getAbsoluteName(), errorCode, args);
    }

    public VerificationFailure newVerificationFailure(String dottedPathName, String errorCode, Object... args) {
        return new VerificationFailure(this, dottedPathName, errorCode, BundleUtils.getBundledMessage(getClass(), SyntaxErrors.SYNTAX_ERROR_BUNDLE, errorCode,
                args));
    }

    protected void addKeys(Class<?> clazz) {
        addKey(getDottedPath());
        addKey(getDottedPath().getAbsoluteName());
        addKey(clazz);
        addKey(clazz.getName());
        addKey(clazz.getSimpleName());
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    protected void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public AnnotationMetaBuilder<SELF, PARENT> build(Class<?> klass, PARENT parent) throws VerificationException {
        setPrimaryKey(klass);
        addKeys(klass);
        return this;
    }
}