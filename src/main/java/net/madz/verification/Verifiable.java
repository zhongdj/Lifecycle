package net.madz.verification;

/**
 * Object that is able to perform verification.
 */
public interface Verifiable {

    /**
     * Verify that all parameters of the action are valid.
     * 
     * @param sessionCtx
     * @return VerificationFailureSet containing zero entries, or more if the
     *         object has verification errors.
     */
    public VerificationFailureSet verify();
}
