package net.madz.utils;

public class BusinessModuleException extends MadzException {

    private static final long serialVersionUID = -3563350052341747701L;
    private final String moduleName;

    public BusinessModuleException(Class<?> cls, String bundle, String errorCode, Object[] messageVars) {
        super(cls, bundle, errorCode, messageVars);
        this.moduleName = bundle;
    }

    public BusinessModuleException(Class<?> cls, String bundle, String errorCode, Object[] messageVars, Throwable cause) {
        super(cls, bundle, errorCode, messageVars, cause);
        this.moduleName = bundle;
    }

    public BusinessModuleException(Class<?> cls, String bundle, String errorCode, Throwable cause) {
        super(cls, bundle, errorCode, cause);
        this.moduleName = bundle;
    }

    public BusinessModuleException(Class<?> cls, String bundle, String errorCode) {
        super(cls, bundle, errorCode);
        this.moduleName = bundle;
    }

    @Override
    public String getCategory() {
        return "BUSINESS_MODULE";
    }

    public String getBusinessModuleName() {
        return this.moduleName;
    }
}
