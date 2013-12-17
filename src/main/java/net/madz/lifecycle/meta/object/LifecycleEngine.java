package net.madz.lifecycle.meta.object;

import net.madz.bcel.intercept.LifecycleInterceptContext;

public interface LifecycleEngine<S> {

    public abstract void doInterceptBefore(LifecycleInterceptContext context);

    public abstract void doInterceptAfter(LifecycleInterceptContext context);

    public abstract void doInterceptException(LifecycleInterceptContext lifecycleContext);
}