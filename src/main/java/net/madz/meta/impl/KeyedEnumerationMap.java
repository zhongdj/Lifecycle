package net.madz.meta.impl;

import java.util.HashMap;
import java.util.Map;

import net.madz.meta.Keyed;

public class KeyedEnumerationMap {

    public final static <K, E extends Enum<E> & Keyed<K>> Map<K, E> valueOf(Class<E> type) {
        return valueOf(type.getEnumConstants());
    }

    public final static <K, E extends Enum<E> & Keyed<K>> Map<K, E> valueOf(E[] values) {
        Map<K, E> map = new HashMap<K, E>(values.length);
        for ( E value : values ) {
            map.put(value.getKey(), value);
        }
        return map;
    }
}
