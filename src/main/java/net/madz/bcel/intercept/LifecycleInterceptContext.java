package net.madz.bcel.intercept;

import java.lang.reflect.Method;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.lifecycle.annotations.Transition;
import net.madz.lifecycle.meta.type.TransitionMetadata.TransitionTypeEnum;
import net.madz.util.StringUtil;
import net.madz.utils.ClassUtils;

public class LifecycleInterceptContext implements UnlockableStack {

    private final Stack<Unlockable> lockedRelatedObjectStack = new Stack<>();
    private static final Logger logger = Logger.getLogger("Lifecycle Framework");
    private static final Level FINE = Level.FINE;
    private final InterceptContext<?, ?> innerContext;
    private String fromState;
    private Object transitionKey;
    private TransitionTypeEnum transitionType;
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
        final Class<?> keyClass = innerContext.getMethod().getAnnotation(Transition.class).value();
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

    public void setTransitionType(TransitionTypeEnum type) {
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

    public TransitionTypeEnum getTransitionType() {
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
