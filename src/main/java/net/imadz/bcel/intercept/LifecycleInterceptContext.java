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
package net.imadz.bcel.intercept;

import java.lang.reflect.Method;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.imadz.lifecycle.annotations.Event;
import net.imadz.lifecycle.meta.type.EventMetadata.EventTypeEnum;
import net.imadz.util.StringUtil;
import net.imadz.utils.ClassUtils;

public class LifecycleInterceptContext implements UnlockableStack {

    private final Stack<Unlockable> lockedRelatedObjectStack = new Stack<Unlockable>();
    private static final Logger logger = Logger.getLogger("Lifecycle Framework");
    private static final Level FINE = Level.FINE;
    private final InterceptContext<?, ?> innerContext;
    private String fromState;
    private Object transitionKey;
    private EventTypeEnum transitionType;
    private String transitionName;
    private String toState;
    private long lifecycleEndTime;
    private long lifecycleStartTime;

    public LifecycleInterceptContext(InterceptContext<?, ?> context) {
        this.innerContext = context;
        init();
        context.putObject(LifecycleInterceptContext.class, this);
    }

    private void init() {
        final Class<?> keyClass = innerContext.getMethod().getAnnotation(Event.class).value();
        if ( ClassUtils.isDefaultStyle(keyClass) ) {
            transitionKey = StringUtil.toUppercaseFirstCharacter(innerContext.getMethod().getName());
        } else {
            transitionKey = keyClass;
        }
    }

    public String getFromState() {
        return fromState;
    }

    public Object getTarget() {
        return this.innerContext.getTarget();
    }

    public void setFromState(String fromState) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("intercepting  [" + getTarget() + "]" + "\n\tfrom state: [" + fromState + "] ");
        }
        this.fromState = fromState;
    }

    public Object getTransitionKey() {
        return transitionKey;
    }

    public void setTransitionType(EventTypeEnum type) {
        this.transitionType = type;
    }

    public void setTransitionName(String name) {
        this.transitionName = name;
    }

    public void logResultFromContext() {
        if ( !innerContext.isSuccess() ) {
            String toStateString = null == this.getToState() ? "(Had Not Been Evaluated)" : this.getToState();
            logger.severe("ReactiveObject: [" + this.getTarget() + "] was failed to transit from state: [" + this.getFromState() + "] to state: ["
                    + toStateString + "] with following error: ");
            logger.severe(innerContext.getFailureCause().getLocalizedMessage());
        }
    }

    public String getToState() {
        return toState;
    }

    public void logStep1ValidateCurrentState() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 1. start validating State [" + this.getFromState() + "]");
        }
    }

    public void logStep2validateTransition() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 2. start validating transition: [" + this.getTransitionKey() + "] on state: [" + this.getFromState() + "]");
        }
    }

    public void logStep3ValidateInboundConstrantBeforeMethodInvocation() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 3. start validating inbound relation constraint is next state is predictable before method invocation.");
        }
    }

    public void logStep4PreStateChangeCallback() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 4. start callback before state change from : " + this.getFromState() + " => to : " + this.getToState());
        }
    }

    public void logStep5ValiatingInbound() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 5. start validating inbound relation constraint is next state after method invocation.");
        }
    }

    public void logStep6_1SetupNextStateFinsihed() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 6. ReactiveObject is tranisited to state: [" + this.getToState() + "]");
        }
    }

    public void logStep6_2SetupNextStateStart() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 6. Set next state to reactiveObject.");
        }
    }

    public void logStep7Callback() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 7. Start Callback after state change from : " + this.getFromState() + " => to : " + this.getToState());
        }
    }

    public void logStep8FireLifecycleEvents() {
        if ( logger.isLoggable(FINE) ) {
            logger.fine("\tStep 8. Start fire state change event.");
        }
    }

    public String getTransition() {
        return transitionName;
    }

    public EventTypeEnum getTransitionType() {
        return transitionType;
    }

    public void setNextState(String nextState) {
        this.toState = nextState;
    }

    public Unlockable popUnlockable() {
        return lockedRelatedObjectStack.pop();
    }

    @Override
    public void pushUnlockable(Unlockable unlockable) {
        this.lockedRelatedObjectStack.push(unlockable);
    }

    @Override
    public boolean isEmpty() {
        return this.lockedRelatedObjectStack.isEmpty();
    }

    public void unlockRelatedReactiveObjects() {
        while ( !isEmpty() ) {
            popUnlockable().unlock();
        }
    }

    public Method getMethod() {
        return innerContext.getMethod();
    }

    public Object[] getArguments() {
        return innerContext.getArguments();
    }

    public long getStartTime() {
        return lifecycleStartTime;
    }

    public long getEndTime() {
        return lifecycleEndTime;
    }
}
