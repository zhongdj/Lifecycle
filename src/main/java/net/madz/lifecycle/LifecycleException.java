package net.madz.lifecycle;

import net.madz.utils.MadzRuntimeException;

public class LifecycleException extends MadzRuntimeException {

    private static final long serialVersionUID = 7069144333982097517L;
    private static final String LIFECYCLE = "Lifecycle";

    public LifecycleException(Class<?> cls, String bundle, String errorCode, Object... messageVars) {
        super(cls, bundle, errorCode, messageVars);
    }

    public LifecycleException(Class<?> cls, String bundle, String errorCode, String[] messageVars, Throwable cause) {
        super(cls, bundle, errorCode, messageVars, cause);
    }

    public LifecycleException(Class<?> cls, String bundle, String errorCode, Throwable cause) {
        super(cls, bundle, errorCode, cause);
    }

    public LifecycleException(Class<?> cls, String bundle, String errorCode) {
        super(cls, bundle, errorCode);
    }

    @Override
    public String getCategory() {
        return LIFECYCLE;
    }
}
