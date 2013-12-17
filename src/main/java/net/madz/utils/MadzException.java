package net.madz.utils;

public abstract class MadzException extends Exception {

    private static final long serialVersionUID = 163564734666783236L;
    protected final String errorCode;
    protected final String bundle;

    public MadzException(Class<?> cls, String bundle, String errorCode, Throwable cause) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode), cause);
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzException(Class<?> cls, String bundle, String errorCode) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode));
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzException(Class<?> cls, String bundle, String errorCode, Object[] messageVars, Throwable cause) {
        super(BundleUtils.getBundledMessage(cls, bundle, errorCode, messageVars), cause);
        this.errorCode = errorCode;
        this.bundle = bundle;
    }

    public MadzException(Class<?> cls, String bundle, String errorCode, Object... messageVars) {
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