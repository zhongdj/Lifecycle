package net.madz.verification;

import net.madz.common.Dumpable;
import net.madz.common.Dumper;
import net.madz.meta.MetaData;

/**
 * RuntimeException caused by a single VerificationFailure or a
 * VerificationFailureSet.
 */
public class VerificationRuntimeException extends RuntimeException implements Dumpable {

    private final static long serialVersionUID = 1L;
    private final VerificationFailureSet verificationSet;

    /**
     * Convenience constructor
     */
    public VerificationRuntimeException(VerificationFailureSet verificationSet) {
        this(null, verificationSet);
    }

    /**
     * Construct a new VerificationRuntimeException from a
     * VerificationFailureSet and a cause.
     */
    public VerificationRuntimeException(Throwable cause, VerificationFailureSet verificationSet) {
        super(verificationSet.getMessage(), cause);
        this.verificationSet = verificationSet;
    }

    /**
     * Convenience constructor
     */
    public VerificationRuntimeException(VerificationFailure failure) {
        this(null, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationRuntimeException(Throwable cause, VerificationFailure failure) {
        this(cause, new VerificationFailureSet().add(failure));
    }

    /**
     * Convenience constructor
     */
    public VerificationRuntimeException(Object source, String errorKey, String defaultErrorMessage, Object... details) {
        this(null, new VerificationFailureSet().add(source, errorKey, defaultErrorMessage, details));
    }

    /**
     * Convenience constructor
     */
    public VerificationRuntimeException(Throwable cause, MetaData metaData, String errorKey, String defaultErrorMessage, Object... details) {
        this(cause, new VerificationFailureSet().add(cause, metaData, errorKey, defaultErrorMessage, details));
    }

    /**
     * Convert this VerificationRuntimeException into a VerificationException
     */
    public VerificationException toVerificationException() {
        VerificationException newException = new VerificationException(getCause(), this.verificationSet);
        newException.setStackTrace(this.getStackTrace());
        return newException;
    }

    /**
     * VerificationFailureSet associated with this exception
     */
    public VerificationFailureSet getVerificationFailureSet() {
        return this.verificationSet;
    }

    @Override
    public void dump(Dumper dumper) {
        dumper.dump(verificationSet);
    }
}
