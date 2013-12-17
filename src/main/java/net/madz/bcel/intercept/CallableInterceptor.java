package net.madz.bcel.intercept;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallableInterceptor<V, R> extends Interceptor<V, R> {

    private static final Logger logger = Logger.getLogger("Lifecycle Framework");

    @Override
    public R aroundInvoke(InterceptContext<V, R> context, Callable<R> callable) throws Exception {
        try {
            if ( logger.isLoggable(Level.FINE) ) {
                logger.fine("intercepting with: " + getClass().getName() + " @intercept");
            }
            return callable.call();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
}