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
