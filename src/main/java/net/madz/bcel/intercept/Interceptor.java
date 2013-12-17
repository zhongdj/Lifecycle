package net.madz.bcel.intercept;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Interceptor<V, R> {

    private static final Logger logger = Logger.getLogger("Lifecycle Framework");
    private Interceptor<V, R> next;

    public Interceptor() {}

    public Interceptor(Interceptor<V, R> next) {
        super();
        this.next = next;
    }

    public R aroundInvoke(InterceptContext<V, R> context, Callable<R> callable) throws Exception {
        try {
            preExec(context);
            R result = next.aroundInvoke(context, callable);
            postExec(context);
            context.markSuccess();
            return result;
        } catch (Throwable e) {
            context.markFail(e);
            handleException(context, e);
            throw e;
        } finally {
            cleanup(context);
        }
    }

    protected void handleException(InterceptContext<V, R> context, Throwable e) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("intercepting with:" + getClass().getName() + " @handleException");
        }
        logger.log(Level.SEVERE, e.getMessage(), e);
    }

    protected void cleanup(InterceptContext<V, R> context) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("intercepting with :" + getClass().getName() + " @cleanup");
        }
    }

    protected void postExec(InterceptContext<V, R> context) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("intercepting with :" + getClass().getName() + " @postExec");
        }
    }

    protected void preExec(InterceptContext<V, R> context) {
        if ( logger.isLoggable(Level.FINE) ) {
            logger.fine("intercepting with :" + getClass().getName() + " @preExec");
        }
    }
}