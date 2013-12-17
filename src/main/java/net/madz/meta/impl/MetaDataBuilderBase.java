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

import java.util.HashMap;

import net.madz.common.DottedPath;
import net.madz.common.ParameterString;
import net.madz.meta.FlavorFactory;
import net.madz.meta.FlavorMetaData;
import net.madz.meta.FlavorNotSupportedException;
import net.madz.meta.Flavored;
import net.madz.meta.KeySet;
import net.madz.meta.MetaData;
import net.madz.meta.MetaDataBuilder;

public abstract class MetaDataBuilderBase<SELF extends MetaData, PARENT extends MetaData> implements MetaData, MetaDataBuilder<SELF, PARENT>, Flavored {

    protected Class<?> containerType;
    protected final KeySet.Builder keySet;
    protected final MetaDataMap.Builder flavorMap;
    protected final HashMap<Class<?>, Object> properties = new HashMap<Class<?>, Object>();
    protected final PARENT parent;
    protected final DottedPath path;

    protected MetaDataBuilderBase(PARENT parent, String name) {
        @SuppressWarnings("unchecked")
        final SELF self = (SELF) this;
        if ( null == parent ) {
            this.parent = null;
            this.path = DottedPath.parse(name);
        } else {
            this.parent = parent;
            this.path = DottedPath.parse(parent.getDottedPath().append(name).getAbsoluteName());
        }
        this.keySet = new KeySet.Builder(3);
        this.flavorMap = new MetaDataMap.Builder(self, 3);
        addKey(this.path.getName());
        addKey(this.path.getAbsoluteName());
    }

    @Override
    public DottedPath getDottedPath() {
        return this.path;
    }

    @Override
    public void addFlavor(FlavorMetaData<? super SELF> flavor) {
        if ( null != flavor ) {
            flavorMap.add(flavor);
        }
    }

    @Override
    public void removeFlavor(Object flavor) {
        this.flavorMap.deepRemove(flavor);
    }

    @Override
    public void addKey(Object key) {
        this.keySet.addKey(key);
    }

    @Override
    public void addKeys(KeySet keySet) {
        this.keySet.addKeys(keySet);
    }

    @Override
    public <F> F getFlavor(Class<F> flavorInterface, boolean assertExists) {
        final Object flavor = getFlavorMetaData(flavorInterface, assertExists);
        if ( flavor instanceof FlavorFactory ) {
            return flavorInterface.cast(( (FlavorFactory) flavor ).getFlavor(flavorInterface, this));
        } else if ( flavorInterface.isInstance(flavor) ) {
            return flavorInterface.cast(flavor);
        }
        if ( null != flavor || assertExists ) {
            throw new FlavorNotSupportedException(this, flavorInterface);
        }
        return null;
    }

    @Override
    public boolean hasFlavor(Class<?> flavorInterface) {
        return this.flavorMap.hasKey(flavorInterface);
    }

    /**
     * Record the error using the ErrorSet flavor of this object
     */
    @Override
    public void handleError(Throwable e) {
        MetaDataError<?> metaError = getFlavor(MetaDataError.class, false);
        if ( null == metaError ) {
            MetaDataError<SELF> newMetaError = new MetaDataError<SELF>();
            addFlavor(newMetaError);
            metaError = newMetaError;
        }
        metaError.addError(e);
    }

    @Override
    public PARENT getParent() {
        return this.parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF getMetaData() {
        return (SELF) this;
    }

    public Object getFlavorMetaData(Object key, boolean assertExists) {
        final Object flavorMetaData = this.flavorMap.get(key);
        if ( null == flavorMetaData && assertExists ) {
            throw new FlavorNotSupportedException(this, key);
        }
        return flavorMetaData;
    }

    /**
     * Set a build property.
     * 
     * <p>
     * Build properties are objects that assist in the initial build-out of
     * system's meta-data definition. These properties are only available during
     * build time and are not available to the meta-data objects themselves.
     * </p>
     */
    public <F> void setProperty(Class<F> propertyKey, F flavor) {
        properties.put(propertyKey, flavor);
    }

    /**
     * Get a build property.
     */
    public <F> F getProperty(Class<F> propertyKey) {
        return propertyKey.cast(properties.get(propertyKey));
    }

    @Override
    public KeySet.Builder getKeySet() {
        return this.keySet;
    }

    public boolean hasFlavorMetaData(Object key) {
        return this.flavorMap.hasKey(key);
    }

    public void setContainerType(Class<?> containerType) {
        this.containerType = containerType;
    }

    public String getName() {
        return path.getName();
    }

    public boolean hasKey(Object key) {
        return keySet.contains(key);
    }

    protected ParameterString toString(ParameterString sb) {
        sb.append("name", path.getName());
        sb.append("keys", keySet);
        return sb;
    }

    @Override
    public final String toString() {
        return toString(new ParameterString(getClass().getSimpleName())).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( path == null ) ? 0 : path.hashCode() );
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MetaDataBuilderBase other = (MetaDataBuilderBase) obj;
        if ( path == null ) {
            if ( other.path != null ) return false;
        } else if ( !path.equals(other.path) ) return false;
        return true;
    }
}
