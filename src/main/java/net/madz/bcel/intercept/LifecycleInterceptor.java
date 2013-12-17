package net.madz.bcel.intercept;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.bcel.intercept.helper.InterceptorHelper;
import net.madz.lifecycle.meta.object.StateMachineObject;

public class LifecycleInterceptor<V, R> extends Interceptor<V, R> {

    private static final Logger logger = Logger.getLogger("Lifecycle Framework");

    public LifecycleInterceptor(Interceptor<V, R> next) {
        super(next);
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("Intercepting....instantiating LifecycleInterceptor");
        }
    }

    @Override
    protected void preExec(InterceptContext<V, R> context) {
        super.preExec(context);
        LifecycleInterceptContext lifecycleContext = new LifecycleInterceptContext(context);
        final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
        stateMachine.doInterceptBefore(lifecycleContext);
    }

    @Override
    protected void postExec(InterceptContext<V, R> context) {
        super.postExec(context);
        final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
        LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
        stateMachine.doInterceptAfter(lifecycleContext);
    }

    @Override
    protected void handleException(InterceptContext<V, R> context, Throwable e) {
        final StateMachineObject<?> stateMachine = InterceptorHelper.lookupStateMachine(context);
        LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
        stateMachine.doInterceptException(lifecycleContext);
        super.handleException(context, e);
    }

    @Override
    protected void cleanup(InterceptContext<V, R> context) {
        super.cleanup(context);
        logCleanup(context);
    }

    private void logCleanup(InterceptContext<V, R> context) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("Intercepting....LifecycleInterceptor is doing cleanup ...");
            LifecycleInterceptContext lifecycleContext = (LifecycleInterceptContext) context.getObject(LifecycleInterceptContext.class);
            lifecycleContext.logResultFromContext();
        }
    }
}