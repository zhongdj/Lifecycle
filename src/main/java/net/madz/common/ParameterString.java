package net.madz.common;

import java.lang.reflect.Field;

import net.madz.util.MetaDataUtil;

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
