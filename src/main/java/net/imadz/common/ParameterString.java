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
package net.imadz.common;

import java.lang.reflect.Field;

import net.imadz.util.MetaDataUtil;

public class ParameterString {

    private final String name;
    private final String separator;
    private final StringBuilder sb = new StringBuilder();

    /**
     * Default constructor
     * 
     * @param name
     *            Name of the parameter string
     */
    public ParameterString(String name) {
        this(null, name);
    }

    /**
     * Advanced constructor which additionally specifies the separator to use to
     * separate values appended to this string.
     * 
     * @param separator
     *            String used to separate values (default constructor uses ", ")
     * @param name
     *            Name of this parameter string, or null for an unnamed string
     */
    public ParameterString(String separator, String name) {
        this.separator = MetaDataUtil.coalesce(separator, ", ");
        this.name = name;
    }

    /**
     * Append a value to the string if the value is true
     * 
     * @param key
     *            Value to append to the string
     * @param value
     *            Value will only be appended if this value is true
     * @return This ParameterString, for easy chaining
     */
    public ParameterString append(Object key, boolean value) {
        if ( value ) {
            append(key);
        }
        return this;
    }

    protected void separate() {
        if ( sb.length() > 0 ) {
            sb.append(separator);
        }
    }

    public <V extends Object> ParameterString appendAll(Object key, V[] values) {
        if ( null != values ) {
            boolean first = true;
            for ( V value : values ) {
                if ( null != value ) {
                    if ( first ) {
                        separate();
                        sb.append(key).append("=").append(value);
                        first = false;
                    } else {
                        sb.append("|").append(value);
                    }
                }
            }
        }
        return this;
    }

    public ParameterString appendAll(Object key, Iterable<?> values) {
        if ( null != values ) {
            boolean first = true;
            for ( Object value : values ) {
                if ( null != value ) {
                    if ( first ) {
                        separate();
                        sb.append(key).append("=").append(value);
                        first = false;
                    } else {
                        sb.append("|").append(value);
                    }
                }
            }
        }
        return this;
    }

    public ParameterString append(Object key, Object value) {
        if ( null != value ) {
            separate();
            sb.append(key).append("=").append(value);
        }
        return this;
    }

    public ParameterString append(Object value) {
        if ( null != value ) {
            separate();
            sb.append(value);
        }
        return this;
    }

    public ParameterString appendFields(Object obj) {
        for ( Class<?> type = obj.getClass(); type != null && type != Object.class; type = type.getSuperclass() ) {
            for ( Field f : type.getDeclaredFields() ) {
                try {
                    if ( !f.isAccessible() ) {
                        f.setAccessible(true);
                    }
                    append(f.getName(), f.get(obj));
                } catch (Exception e) {
                    // oh well
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        if ( null != name ) {
            return name + "[" + sb.toString() + "]";
        }
        return sb.toString();
    }
}
