package net.madz.bcel.intercept;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterceptorController<V, R> {

    private static Logger logger = Logger.getLogger("Lifecycle Framework");
    final Interceptor<V, R> interceptorChain = new LifecycleInterceptor<V, R>(new CallableInterceptor<V, R>());

    public R exec(InterceptContext<V, R> context, Callable<R> callable) throws Exception {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("Intercepting....InterceptorController is doing exec ...");
        }
        try {
            return interceptorChain.aroundInvoke(context, callable);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
}