package net.madz.meta;

import net.madz.verification.VerificationRuntimeException;

/**
 * Exception thrown when a flavor is requested from an object that either does
 * not support that flavor.
 */
public class FlavorNotSupportedException extends VerificationRuntimeException {

    private final static long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param flavorMetaData
     *            Meta-data object of the flavor
     * @param flavorKey
     *            Flavor key (typically a name or class)
     */
    public FlavorNotSupportedException(Object flavorMetaData, Object flavorKey) {
        super(flavorMetaData, "flavorNotSupported." + toString(flavorKey), "Flavor %s not supported by %s", flavorKey, flavorMetaData);
    }

    /**
     * Convert a flavor key to a String
     */
    private final static String toString(Object flavorKey) {
        if ( flavorKey instanceof Class ) {
            return ( (Class<?>) flavorKey ).getSimpleName();
        }
        return flavorKey.toString();
    }
}
