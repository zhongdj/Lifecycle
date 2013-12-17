/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2013-2020 Madz. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://raw.github.com/zhongdj/Lifecycle/master/License.txt
 * . See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package net.madz.meta.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.madz.common.Dumpable;
import net.madz.common.Dumper;
import net.madz.meta.KeySet;
import net.madz.meta.MetaData;
import net.madz.meta.MetaDataFilter;
import net.madz.meta.MetaDataFilterable;
import net.madz.util.StringUtil;

/**
 * 
 * Collection of filterable meta-data (typically child elements), that supports
 * the concept of lazy filtering.
 * 
 */
public class MetaDataMap implements Iterable<MetaDataFilterable>, Dumpable {

    protected final MetaData owner;
    protected final MetaDataFilter filter;
    protected final List<Value> valueList;
    protected final Map<Object, List<Integer>> valueKeyMap;
    protected List<MetaDataFilterable> entryListCache;

    /**
     * Constructor
     * 
     * @param owner
     *            New MetaData owner for this map.
     * @param cloneValueList
     * @param valueKeyMap
     * @param lazyFilter
     *            Enabling will cause this map can be performed any time the
     *            keys of the entries do not change. Passing lazyFilter as false
     *            will cause the passed values to be actively filtered, and the
     *            key map to be recreated
     */
    private MetaDataMap(MetaData owner, List<Value> cloneValueList, Map<Object, List<Integer>> valueKeyMap, MetaDataFilter filter, boolean lazyFilter) {
        this.owner = owner;
        this.filter = filter;
        if ( null != cloneValueList ) {
            this.valueList = new ArrayList<Value>(cloneValueList.size());
            //
            // When lazy filtering, copy every Value from the cloneValueList
            // into an UnfilteredValue
            // object. These objects will be filtered when accessed.
            //
            if ( lazyFilter ) {
                this.valueKeyMap = valueKeyMap;
                this.entryListCache = null;
                for ( Value cloneValue : cloneValueList ) {
                    if ( null != cloneValue ) {
                        this.valueList.add(new UnfilteredValue(cloneValue));
                    } else {
                        this.valueList.add(null);
                    }
                }
            }
            //
            // Otherwise create a Value from each entry
            //
            else {
                Map<Object, List<Integer>> newKeyMap = new HashMap<Object, List<Integer>>(( valueKeyMap.size() * 3 ) / 2);
                this.entryListCache = new ArrayList<MetaDataFilterable>(cloneValueList.size());
                for ( Value cloneValue : cloneValueList ) {
                    if ( null != cloneValue ) {
                        final MetaDataFilterable value = cloneValue.getFilteredValue(owner, filter);
                        if ( null != value ) {
                            Value filteredValue = new Value(this.valueList.size(), value, value.getKeySet());
                            this.valueList.add(filteredValue);
                            // Create the newKeyMap
                            Integer id = filteredValue.position;
                            entryListCache.add(value);
                            KeySet keySet = filteredValue.value.getKeySet();
                            for ( Object key : keySet ) {
                                List<Integer> idList = newKeyMap.get(key);
                                if ( null == idList ) {
                                    idList = new LinkedList<Integer>();
                                    newKeyMap.put(key, idList);
                                }
                                idList.add(id);
                            }
                        }
                    } else {
                        this.valueList.add(null);
                    }
                }
                this.valueKeyMap = Collections.unmodifiableMap(newKeyMap);
            }
        } else {
            this.valueKeyMap = valueKeyMap;
            this.valueList = new LinkedList<Value>();
        }
    }

    public MetaDataMap filter(MetaData newOwner, MetaDataFilter filter, boolean lazyFilter) {
        return new MetaDataMap(newOwner, valueList, valueKeyMap, filter, lazyFilter);
    }

    /**
     * Filtered value
     */
    protected class Value {

        public final Integer position;
        protected final MetaDataFilterable value;
        protected final KeySet keySet;

        // public Value(MetaDataFilterable value) {
        // this.value= value;
        // this.position= valueList.size();
        // this.keySet= value.getKeySet();
        // }
        //
        public Value(int position, MetaDataFilterable value, KeySet keySet) {
            this.value = value;
            this.position = position;
            this.keySet = keySet;
        }

        public MetaDataFilterable getValue() {
            return value;
        }

        @Override
        public final int hashCode() {
            return this.position.hashCode();
        }

        @Override
        public final boolean equals(Object obj) {
            return ( obj instanceof MetaDataMap.Value ) && ( (MetaDataMap.Value) obj ).position.equals(this.position);
        }

        @Override
        public String toString() {
            return position + "=" + value;
        }

        protected final MetaDataFilterable getFilteredValue(MetaData newOwner, MetaDataFilter filter) {
            if ( null != value && filter.canInclude(this.value) ) {
                return value.filter(newOwner, filter, true);
            }
            return null;
        }
    }
    private class UnfilteredValue extends Value {

        public UnfilteredValue(Value cloneValue) {
            super(cloneValue.position, cloneValue.value, cloneValue.keySet);
        }

        public MetaDataFilterable getValue() {
            Value filteredValue = new Value(position, getFilteredValue(owner, filter), keySet);
            valueList.set(position, filteredValue);
            return filteredValue.value;
        }

        @Override
        public String toString() {
            return position + "=<" + value + ">";
        }
    }

    public Collection<MetaDataFilterable> getValues(Object key) {
        List<Integer> idList = this.valueKeyMap.get(key);
        if ( null != idList ) {
            ArrayList<MetaDataFilterable> results = new ArrayList<MetaDataFilterable>(idList.size());
            for ( Integer id : idList ) {
                Value valueEntry = this.valueList.get(id.intValue());
                if ( null != valueEntry ) {
                    MetaDataFilterable value = valueEntry.getValue();
                    if ( null != value ) {
                        results.add(value);
                    }
                }
            }
            return results;
        }
        return Collections.emptyList();
    }

    /**
     * Return a keyed value from the map
     * 
     * @param key
     *            Value's key
     * @return Value, or null if not found
     */
    public MetaDataFilterable get(Object key) {
        List<Integer> idList = this.valueKeyMap.get(key);
        if ( null != idList ) {
            for ( Integer id : idList ) {
                Value valueEntry = this.valueList.get(id.intValue());
                if ( null != valueEntry ) {
                    MetaDataFilterable value = valueEntry.getValue();
                    if ( null != value ) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Does the key belong to a MetaData object in this map?
     */
    public boolean hasKey(Object key) {
        return null != get(key);
    }

    /**
     * Convenience method that returns an object cast to the specified type.
     * 
     * @param type
     *            to cast to
     * @param key
     *            value's key
     * @return Value implementing the specified class/interface, or null if not
     *         found
     * 
     */
    public <T> T get(Class<T> type, Object key) {
        return type.cast(get(key));
    }

    /**
     * Number of entries in the map
     * 
     * @return getEntries().size()
     */
    public int size() {
        return getEntries().size();
    }

    /**
     * @return getEntries().iterator()
     */
    @Override
    public Iterator<MetaDataFilterable> iterator() {
        return getEntries().iterator();
    }

    /**
     * All entries in map
     */
    public <MD extends MetaDataFilterable> List<MD> getEntries() {
        if ( null == entryListCache ) {
            entryListCache = new ArrayList<MetaDataFilterable>(this.valueList.size());
            for ( Value valueEntry : this.valueList ) {
                if ( null != valueEntry ) {
                    MetaDataFilterable value = valueEntry.getValue();
                    if ( null != value ) {
                        entryListCache.add(value);
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        List<MD> list = (List<MD>) entryListCache;
        return list;
    }

    @Override
    public void dump(Dumper dumper) {
        for ( MetaDataFilterable value : getEntries() ) {
            dumper.dump(value);
        }
    }

    @Override
    public String toString() {
        return StringUtil.toString(getEntries(), ",", "{", "}");
    }

    static class Builder extends MetaDataMap {

        /**
         * Constructor
         * 
         * @param owner
         * @param mapSize
         */
        Builder(MetaData owner, int mapSize) {
            super(owner, null, new HashMap<Object, List<Integer>>(mapSize), MetaDataFilter.NULL_FILTER, false);
            this.entryListCache = new LinkedList<MetaDataFilterable>();
        }

        @Override
        public MetaDataMap filter(MetaData newOwner, MetaDataFilter filter, boolean lazyFilter) {
            Map<Object, List<Integer>> valueKeyMap = new HashMap<Object, List<Integer>>(this.valueKeyMap);
            for ( Map.Entry<Object, List<Integer>> mapEntry : valueKeyMap.entrySet() ) {
                mapEntry.setValue(Collections.unmodifiableList(new ArrayList<Integer>(mapEntry.getValue())));
            }
            return new MetaDataMap(newOwner, valueList, Collections.unmodifiableMap(valueKeyMap), filter, lazyFilter);
        }

        /**
         * Remove the specified key from the MetaDataMap, any other keys for the
         * same flavor(s) will still exist.
         * 
         * @param key
         *            Remove specified flavor key
         * @return int number of flavor(s) removed
         */
        public int shallowRemove(Object key) {
            List<Integer> idList = this.valueKeyMap.remove(key);
            if ( null != idList ) {
                return idList.size();
            }
            return 0;
        }

        public void deepRemove(Object key) {
            List<Integer> idList = this.valueKeyMap.get(key);
            if ( null != idList ) {
                for ( Integer id : idList ) {
                    Value valueEntry = valueList.set(id.intValue(), null);
                    KeySet keySet = valueEntry.value.getKeySet();
                    for ( Object valueKey : keySet ) {
                        this.valueKeyMap.remove(valueKey);
                    }
                    this.entryListCache.remove(valueEntry.value);
                }
            }
        }

        public <MD extends MetaDataFilterable> void add(MD metaData) {
            this.entryListCache.add(metaData);
            Value valueEntry = new Value(valueList.size(), metaData, metaData.getKeySet());
            this.valueList.add(valueEntry);
            for ( Object key : metaData.getKeySet() ) {
                List<Integer> idList = this.valueKeyMap.get(key);
                if ( null == idList ) {
                    idList = new LinkedList<Integer>();
                    this.valueKeyMap.put(key, idList);
                }
                idList.add(valueEntry.position);
            }
        }
    }
}
