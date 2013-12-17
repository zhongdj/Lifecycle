package net.madz.meta;

import java.util.Map;

import net.madz.meta.impl.KeyedEnumerationMap;

/**
 * MetaData flavor
 */
public abstract class FlavorFactoryMetaData<OWNER extends MetaData, E extends Enum<E> & Keyed<Class<?>>> implements FlavorMetaData<OWNER>, FlavorFactory {

    private final KeySet keySet;
    private final Map<Class<?>, E> flavorKeyMap;

    @SuppressWarnings("unchecked")
    protected FlavorFactoryMetaData(E... keyEnumeration) {
        flavorKeyMap = KeyedEnumerationMap.valueOf(keyEnumeration);
        keySet = new KeySet(flavorKeyMap.keySet().toArray(new Object[flavorKeyMap.size()]));
    }

    protected FlavorFactoryMetaData(Class<E> keyEnumeration) {
        flavorKeyMap = KeyedEnumerationMap.valueOf(keyEnumeration);
        keySet = new KeySet(flavorKeyMap.keySet().toArray(new Object[flavorKeyMap.size()]));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public FlavorFactoryMetaData<OWNER, E> filter(MetaData parent, MetaDataFilter filter, boolean lazyFilter) {
        return this;
    }

    @Override
    public final KeySet getKeySet() {
        return keySet;
    }

    protected abstract Object buildFlavor(E type, Object container) throws Exception;

    @Override
    public final <T> T getFlavor(Class<T> flavorInterface, Object container) throws FlavorNotSupportedException {
        E type = this.flavorKeyMap.get(flavorInterface);
        if ( null != type ) {
            try {
                Object value = buildFlavor(type, container);
                if ( null != value ) {
                    return flavorInterface.cast(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new FlavorNotSupportedException(this, flavorInterface);
            }
        }
        throw new FlavorNotSupportedException(this, flavorInterface);
    }
}
