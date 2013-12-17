package net.madz.meta;

/**
 * Object that supports the Flavor pattern.
 */
public interface Flavored {

    /**
     * Flavor handler.
     * 
     * <p>
     * Flavor handler provide domain specific extensions to generic meta data
     * types such as WebSerice support and Hibernate access.
     * </p>
     * 
     * @param handlerInterface
     *            Java class or Interface that provides the functionality of the
     *            handler
     * @param assertExists
     *            throws a FlavorNotSupportedException if the flavor does not
     *            exist
     * @return Handler if one of the specified type is registered, null
     *         otherwise
     * @throws FlavorNotSupportedException
     */
    public <T> T getFlavor(Class<T> flavorInterface, boolean assertExists);

    /**
     * Checks if the specified flavor is supported.
     * 
     * @param flavorInterface
     *            Flavor to check for
     * @return true if it is supported, false otherwise
     */
    public boolean hasFlavor(Class<?> flavorInterface);
}
