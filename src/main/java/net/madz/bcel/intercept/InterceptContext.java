package net.madz.bcel.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.madz.utils.ClassUtils;

public final class InterceptContext<V, R> {

    private static final Logger logger = Logger.getLogger("Lifecycle Framework");
    private static final Level FINE = Level.FINE;
    private final HashMap<Object, Object> data = new HashMap<>();
    private final long startTime;
    private final Annotation[] annotation;
    private final Class<?> klass;
    private final Method method;
    private final V target;
    private final Object[] arguments;
    private long endTime;
    private boolean success;
    private Throwable failureCause;

    public InterceptContext(final Class<?> klass, final V target, final String methodName, final Class<?>[] argsType, final Object[] arguments) {
        this.klass = klass;
        this.method = ClassUtils.findDeclaredMethod(klass, methodName, argsType);
        this.annotation = method.getAnnotations();
        this.target = target;
        this.startTime = System.currentTimeMillis();
        this.arguments = arguments;
        logInterceptPoint(klass, methodName);
    }

    public void doTerminate() {
        this.endTime = System.currentTimeMillis();
    }

    public Annotation[] getAnnotation() {
        return annotation;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public long getEndTime() {
        return endTime;
    }

    public Throwable getFailureCause() {
        return failureCause;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public Method getMethod() {
        return method;
    }

    public long getStartTime() {
        return startTime;
    }

    public V getTarget() {
        return target;
    }

    public boolean isSuccess() {
        return success;
    }

    private void logInterceptPoint(final Class<?> klass, final String methodName) {
        if ( logger.isLoggable(FINE) ) {
            final StringBuilder sb = new StringBuilder(" ");
            for ( Object o : this.arguments ) {
                sb.append(String.valueOf(o)).append(" ");
            }
            logger.fine("Found Intercept Point: " + klass + "." + methodName + "( " + sb.toString() + " )");
            logger.fine("Intercepting....instatiating InterceptContext ...");
        }
    }

    public void markFail(Throwable failureCause) {
        this.success = false;
        this.failureCause = failureCause;
        doTerminate();
    }

    public void markSuccess() {
        this.success = true;
        this.failureCause = null;
        doTerminate();
    }

    public void putObject(Object key, Object value) {
        data.put(key, value);
    }

    public Object getObject(Object key) {
        return data.get(key);
    }
}