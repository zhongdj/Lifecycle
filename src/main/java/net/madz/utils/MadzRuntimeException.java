package net.madz.utils;

public abstract class MadzRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 8163172301068340362L;
    protected final String errorCode;
    protected final String bundle;

    public MadzRuntimeException(Class<?> cls, String bundle, String errorCode, Throwable cause) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode), cause);
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzRuntimeException(Class<?> cls, String bundle, String errorCode) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode));
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzRuntimeException(Class<?> cls, String bundle, String errorCode, Object[] messageVars, Throwable cause) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode, messageVars), cause);
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzRuntimeException(Class<?> cls, String bundle, String errorCode, Object... messageVars) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode, messageVars));
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }

    @Override
    public String getMessage() {
        if ( null == getCause() ) {
            return super.getMessage();
        } else {
            return super.getMessage() + "\nCaused By:\n" + getCause().getStackTrace().toString();
        }
    }

    public abstract String getCategory();

    public String getErrorCode() {
        return errorCode;
    }
}
