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
package net.madz.lifecycle.impl;

import net.madz.bcel.intercept.LifecycleInterceptContext;
import net.madz.lifecycle.LifecycleEvent;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;

public class LifecycleEventImpl implements LifecycleEvent {

    private final Object reactiveObject;
    private final String fromState;
    private final String toState;
    private final String transition;
    private final TransitionTypeEnum transitionType;
    private final long startTime;
    private final long endTime;

    public LifecycleEventImpl(LifecycleInterceptContext context) {
        this.reactiveObject = context.getTarget();
        this.fromState = context.getFromState();
        this.toState = context.getToState();
        this.transition = context.getTransition();
        this.transitionType = context.getTransitionType();
        this.startTime = context.getStartTime();
        this.endTime = context.getEndTime();
    }

    @Override
    public Object getReactiveObject() {
        return reactiveObject;
    }

    @Override
    public String fromState() {
        return fromState;
    }

    @Override
    public String toState() {
        return toState;
    }

    @Override
    public String transition() {
        return transition;
    }

    @Override
    public TransitionTypeEnum transitionType() {
        return transitionType;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public long endTime() {
        return endTime;
    }
}
