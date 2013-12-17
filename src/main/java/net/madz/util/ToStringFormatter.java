package net.madz.util;

import net.madz.common.Formatter;

public class ToStringFormatter implements Formatter<Object> {

    private static final ToStringFormatter INSTANCE = new ToStringFormatter();

    public static ToStringFormatter getInstance() {
        return INSTANCE;
    }

    @Override
    public String format(Object value) {
        return value != null ? String.valueOf(value) : null;
    }
}
