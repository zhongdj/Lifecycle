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
package net.imadz.lifecycle.meta.object;

import net.imadz.bcel.intercept.UnlockableStack;
import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.meta.MetaObject;
import net.imadz.lifecycle.meta.MultiKeyed;
import net.imadz.lifecycle.meta.builder.impl.CallbackObject;
import net.imadz.lifecycle.meta.type.RelationConstraintMetadata;
import net.imadz.lifecycle.meta.type.StateMetadata;

public interface StateObject<S> extends MetaObject<StateObject<S>, StateMetadata>, MultiKeyed {

    void verifyValidWhile(Object target, RelationConstraintMetadata[] relation, Object relationInstance, UnlockableStack stack);

    void verifyInboundWhileAndLockRelatedObjects(Object eventKey, Object target, String nextState, RelationConstraintMetadata[] relation,
            Object relationInstance, UnlockableStack stack);

    void invokeFromPreStateChangeCallbacks(LifecycleContext<?, S> callbackContext);

    void invokeToPreStateChangeCallbacks(LifecycleContext<?, S> callbackContext);

    void invokeFromPostStateChangeCallbacks(LifecycleContext<?, S> callbackContext);

    void invokeToPostStateChangeCallbacks(LifecycleContext<?, S> callbackContext);

    void addPreToCallbackObject(Class<?> stateClass, CallbackObject callbackObject);

    void addPreFromCallbackObject(Class<?> from, CallbackObject callbackObject);

    void addPostToCallbackObject(Class<?> to, CallbackObject item);

    void addPostFromCallbackObject(Class<?> from, CallbackObject item);
}
