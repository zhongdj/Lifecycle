package net.madz.verification;

import net.madz.meta.MetaData;

/**
 * RuntimeException caused by server error.
 */
public class ServerErrorRuntimeException extends VerificationRuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerErrorRuntimeException(Throwable cause, MetaData metaData, String errorKey, String defaultErrorMessage, Object... details) {
        super(cause, new VerificationFailureSet().add(cause, metaData, errorKey, defaultErrorMessage, details));
    }

    public ServerErrorRuntimeException(Object source, String errorKey, String defaultErrorMessage, Object... details) {
        super(null, new VerificationFailureSet().add(source, errorKey, defaultErrorMessage, details));
    }

    public ServerErrorRuntimeException(VerificationFailureSet verificationSet) {
        super(null, verificationSet);
    }
}
