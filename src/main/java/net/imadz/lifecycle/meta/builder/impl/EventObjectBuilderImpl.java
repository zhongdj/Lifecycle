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

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.meta.builder.EventObjectBuilder;
import net.imadz.lifecycle.meta.builder.StateMachineObjectBuilder;
import net.imadz.lifecycle.meta.object.EventObject;
import net.imadz.lifecycle.meta.object.StateMachineObject;
import net.imadz.lifecycle.meta.type.EventMetadata;
import net.imadz.verification.VerificationException;
import net.imadz.verification.VerificationFailureSet;

public class EventObjectBuilderImpl extends ObjectBuilderBase<EventObject, StateMachineObject<?>, EventMetadata> implements
        EventObjectBuilder {

    private Method eventMethod;
    private final List<EventCallbackObject> eventCallbacks = new LinkedList<EventCallbackObject>();

    public EventObjectBuilderImpl(StateMachineObjectBuilder<?> parent, Method eventMethod, EventMetadata template) {
        super(parent, "EventSet." + template.getDottedPath().getName() + "." + eventMethod.getName());
        this.eventMethod = eventMethod;
        this.setMetaType(template);
    }

    @Override
    public EventObjectBuilder build(Class<?> klass, StateMachineObject<?> parent) throws VerificationException {
        super.build(klass, parent);
        return this;
    }

    @Override
    public Method getEventMethod() {
        return eventMethod;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
        // TODO Auto-generated method stub
    }

	@Override
	public void addSpecificOnEventCallbackObject(EventCallbackObject item) {
		this.eventCallbacks.add(item);
	}

	@Override
	public void invokeEventCallbacks(LifecycleContext<?, ?> callbackContext) {
		for (final EventCallbackObject callback : eventCallbacks) {
			callback.doCallback(callbackContext);
		}
	}
}
