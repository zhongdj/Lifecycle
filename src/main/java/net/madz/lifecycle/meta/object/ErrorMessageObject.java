package net.madz.lifecycle.meta.object;

import net.madz.lifecycle.meta.type.StateMetadata;

public class ErrorMessageObject {

    private String bundle;
    private Class<?> classLoaderClass;
    private String errorCode;
    private StateMetadata[] errorStates;

    public ErrorMessageObject(String bundle, Class<?> classLoaderClass, String errorCode, StateMetadata[] errorStates) {
        super();
        this.bundle = bundle;
        this.classLoaderClass = classLoaderClass;
        this.errorCode = errorCode;
        this.errorStates = errorStates;
    }

    public String getBundle() {
        return bundle;
    }

    public Class<?> getClassLoaderClass() {
        return classLoaderClass;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public StateMetadata[] getErrorStates() {
        return errorStates;
    }
}
