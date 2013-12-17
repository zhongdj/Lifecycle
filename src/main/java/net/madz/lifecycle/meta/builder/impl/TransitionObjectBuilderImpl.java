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

import java.lang.reflect.Method;

import net.madz.lifecycle.meta.builder.StateMachineObjectBuilder;
import net.madz.lifecycle.meta.builder.TransitionObjectBuilder;
import net.madz.lifecycle.meta.object.StateMachineObject;
import net.madz.lifecycle.meta.object.TransitionObject;
import net.madz.lifecycle.meta.type.TransitionMetadata;
import net.madz.verification.VerificationException;
import net.madz.verification.VerificationFailureSet;

public class TransitionObjectBuilderImpl extends ObjectBuilderBase<TransitionObject, StateMachineObject<?>, TransitionMetadata> implements
        TransitionObjectBuilder {

    private Method transitionMethod;

    public TransitionObjectBuilderImpl(StateMachineObjectBuilder<?> parent, Method transitionMethod, TransitionMetadata template) {
        super(parent, "TransitionSet." + template.getDottedPath().getName() + "." + transitionMethod.getName());
        this.transitionMethod = transitionMethod;
        this.setMetaType(template);
    }

    @Override
    public TransitionObjectBuilder build(Class<?> klass, StateMachineObject<?> parent) throws VerificationException {
        super.build(klass, parent);
        return this;
    }

    @Override
    public Method getTransitionMethod() {
        return transitionMethod;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }
}
