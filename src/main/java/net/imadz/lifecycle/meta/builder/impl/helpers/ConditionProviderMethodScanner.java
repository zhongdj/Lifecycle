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

import java.lang.reflect.Method;
import java.util.HashSet;

import net.imadz.lifecycle.SyntaxErrors;
import net.imadz.lifecycle.annotations.action.Condition;
import net.imadz.lifecycle.meta.builder.impl.StateMachineObjectBuilderImpl;
import net.imadz.lifecycle.meta.type.StateMachineMetadata;
import net.imadz.util.MethodScanCallback;
import net.imadz.verification.VerificationFailureSet;

public final class ConditionProviderMethodScanner implements MethodScanCallback {

    private final StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl;
    private final VerificationFailureSet failureSet;
    private HashSet<Class<?>> conditions = new HashSet<Class<?>>();
    private StateMachineMetadata template;
    private Class<?> klass;

    public ConditionProviderMethodScanner(StateMachineObjectBuilderImpl<?> stateMachineObjectBuilderImpl, Class<?> klass, StateMachineMetadata template,
            VerificationFailureSet failureSet) {
        this.stateMachineObjectBuilderImpl = stateMachineObjectBuilderImpl;
        this.template = template;
        this.klass = klass;
        this.failureSet = failureSet;
    }

    @Override
    public boolean onMethodFound(Method method) {
        if (method.isBridge()) return false;
        final Condition condition = method.getAnnotation(Condition.class);
        if ( null != condition ) {
            if ( template.hasCondition(condition.value()) ) {
                if ( conditions.contains(condition.value()) ) {
                    failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(),
                            SyntaxErrors.LM_CONDITION_MULTIPLE_METHODS_REFERENCE_SAME_CONDITION, klass, condition.value()));
                } else {
                    if ( !condition.value().isAssignableFrom(method.getReturnType()) ) {
                        failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(),
                                SyntaxErrors.LM_CONDITION_OBJECT_DOES_NOT_IMPLEMENT_CONDITION_INTERFACE, method, condition.value()));
                    }
                    conditions.add(condition.value());
                }
            } else {
                failureSet.add(this.stateMachineObjectBuilderImpl.newVerificationException(klass.getName(), SyntaxErrors.LM_CONDITION_REFERENCE_INVALID,
                        method, condition.value()));
            }
        }
        return false;
    }
}