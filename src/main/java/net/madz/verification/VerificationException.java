package net.madz.verification;

import net.madz.common.Dumpable;
import net.madz.common.Dumper;

/**
 * Exception caused by a single VerificationFailure or a VerificationFailureSet.
 */
public class VerificationException extends Exception implements Dumpable {

    private final static long serialVersionUID = 1L;
    private final VerificationFailureSet verificationSet;

    /**
     * Convenience constructor
     */
    public VerificationException(VerificationFailureSet verificationSet) {
        this(null, verificationSet);
    }

    /**
     * Full constructor
     * 
     * @param cause
     *            Throwable that has triggered this exception
     * @param verificationSet
     *            Verification failures associated with the exception
     */
    public VerificationException(Throwable cause, VerificationFailureSet verificationSet) {
        super(verificationSet.getMessage(), cause);
        this.verificationSet = verificationSet;
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Throwable cause, VerificationFailure failure) {
        this(cause, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(VerificationFailure failure) {
        this(null, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Object metaData, String errorKey, String defaultErrorMessage, Object... details) {
        this(null, new VerificationFailureSet().add(metaData, errorKey, defaultErrorMessage, details));
    }

    /**
     * Convenience constructor
     */
    public VerificationException(Throwable cause, Object metaData, String errorKey, String defaultErrorMessage, Object... details) {
        this(cause, new VerificationFailureSet().add(cause, metaData, errorKey, defaultErrorMessage, details));
    }

    /**
     * VerificationFailureSet associated with this exception
     */
    public VerificationFailureSet getVerificationFailureSet() {
        return this.verificationSet;
    }

    @Override
    public void dump(Dumper dumper) {
        dumper.println("VerificationException");
        dumper.dump(verificationSet);
    }

    /**
     * Convert this VerificationException into a VerificationRuntimeException
     */
    public VerificationRuntimeException toRuntimeException() {
        VerificationRuntimeException newException = new VerificationRuntimeException(getCause(), this.verificationSet);
        newException.setStackTrace(this.getStackTrace());
        return newException;
    }
}
