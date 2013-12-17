package net.madz.meta;

public interface MetaDataBuilder<SELF extends MetaData, PARENT extends MetaData> {

    /** Add a flavor to to the built meta data */
    public void addFlavor(FlavorMetaData<? super SELF> flavor);

    /** Remove all flavors that have the specified key */
    public void removeFlavor(Object key);

    /** Add a key to the built meta-data */
    public void addKey(Object key);

    /** Add all keys from the KeySet to the built meta-data */
    public void addKeys(KeySet keySet);

    /** Meta data object being created by this builder */
    public SELF getMetaData();

    /**
     * Indicates that the specified exception occurred while building the meta
     * definition
     */
    public void handleError(Throwable e);

    /**
     * Does this meta-data recognize itself by this key?
     * 
     * @param key
     * @return
     */
    public boolean hasKey(Object key);
}
