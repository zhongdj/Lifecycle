package net.madz.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.madz.util.StringUtil;

/**
 * A Set of keys identifying a Keyed object.
 */
public class KeySet implements Iterable<Object> {

    public final static KeySet EMPTY_SET = new KeySet();
    protected final Set<Object> keySet;

    /**
     * Create a new KeySet which is an unmodifiable copy of the original keyset.
     * 
     * @param clone
     *            KeySet to copy
     */
    public KeySet(KeySet clone) {
        this.keySet = Collections.unmodifiableSet(new HashSet<Object>(clone.keySet));
    }

    /**
     * Constructor used by the Builder
     */
    private KeySet(Set<Object> keySet) {
        this.keySet = keySet;
    }

    /**
     * Construct a static KeySet composed of the specified keys
     * 
     * @param keys
     */
    public KeySet(Object... keys) {
        switch (keys.length) {
            case 0:
                this.keySet = Collections.emptySet();
                break;
            case 1:
                this.keySet = Collections.singleton(keys[0]);
                break;
            default:
                this.keySet = Collections.unmodifiableSet(new HashSet<Object>(Arrays.asList(keys)));
                break;
        }
    }

    public boolean contains(Object key) {
        return this.keySet.contains(key);
    }

    public int size() {
        return this.keySet.size();
    }

    @Override
    public boolean equals(Object obj) {
        return ( obj instanceof KeySet ) && ( (KeySet) obj ).keySet.equals(this.keySet);
    }

    @Override
    public int hashCode() {
        return this.keySet.hashCode();
    }

    @Override
    public String toString() {
        return StringUtil.toString(this.keySet, ", ");
    }

    @Override
    public Iterator<Object> iterator() {
        return this.keySet.iterator();
    }

    public static class Builder extends KeySet {

        public Builder(int size) {
            super(new HashSet<Object>(size));
        }

        public void addKeys(KeySet keySet) {
            if ( null != keySet ) {
                this.keySet.addAll(keySet.keySet);
            }
        }

        public void addKey(Object key) {
            if ( null != key ) {
                this.keySet.add(key);
            }
        }

        public KeySet filter() {
            return new KeySet(this);
        }
    }
}
