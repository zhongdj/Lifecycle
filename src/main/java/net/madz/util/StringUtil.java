package net.madz.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.madz.common.Formatter;

public class StringUtil {

    public static final Charset UTF8 = Charset.forName("UTF-8");
    private static final CharsetEncoder UTF8_ENCODER = UTF8.newEncoder(); // used

    /**
     * only for escapeXml - no actual encoding
     */
    public static String escapeXml(String string) {
        return escapeXml(string, UTF8);
    }

    public static String escapeXml(String string, Charset charset) {
        CharsetEncoder encoder = charset == UTF8 ? UTF8_ENCODER : charset.newEncoder();
        StringBuilder sb = new StringBuilder();
        for ( char c : string.toCharArray() ) {
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    if ( encoder.canEncode(c) ) {
                        sb.append(c);
                    } else {
                        String replacement = "&#" + ( (int) c ) + ";";
                        sb.append(replacement);
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public static String toUppercaseFirstCharacter(String s) {
        if ( s == null || s.length() == 0 ) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + ( s.length() > 1 ? s.substring(1) : "" );
    }

    public static String toLowercaseFirstCharacter(String s) {
        if ( s == null || s.length() == 0 ) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + ( s.length() > 1 ? s.substring(1) : "" );
    }

    public static <E> String toString(Collection<E> c, String delimiter) {
        return toString(c, delimiter, null, null, ToStringFormatter.getInstance());
    }

    public static <E> String toString(Collection<E> c, String delimiter, Formatter<? super E> formatter) {
        return toString(c, delimiter, null, null, formatter);
    }

    public static <E> String toString(Collection<E> c, String delimiter, String before, String after) {
        return toString(c, delimiter, before, after, ToStringFormatter.getInstance());
    }

    public static <E> String toString(Collection<E> c, String delimiter, String before, String after, Formatter<? super E> formatter) {
        if ( c == null ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        append(sb, c, delimiter, before, after, formatter);
        return sb.toString();
    }

    public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter) {
        append(sb, c, delimiter, null, null, ToStringFormatter.getInstance());
    }

    public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, Formatter<E> formatter) {
        append(sb, c, delimiter, null, null, formatter);
    }

    public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, String before, String after) {
        append(sb, c, delimiter, before, after, ToStringFormatter.getInstance());
    }

    public static <E> void append(StringBuilder sb, Collection<E> c, String delimiter, String before, String after, Formatter<? super E> formatter) {
        if ( c.isEmpty() ) {
            return;
        }
        if ( before != null ) {
            sb.append(before);
        }
        boolean first = true;
        for ( E e : c ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(formatter.format(e));
        }
        if ( after != null ) {
            sb.append(after);
        }
    }

    public static <K, V> String toString(Map<K, V> m, String delimiter) {
        return toString(m, delimiter, "=", true, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
    }

    public static <K, V> String toString(Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue) {
        return toString(m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, null, null, ToStringFormatter.getInstance(),
                ToStringFormatter.getInstance());
    }

    public static <K, V> String toString(Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before,
            String after) {
        return toString(m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, ToStringFormatter.getInstance(),
                ToStringFormatter.getInstance());
    }

    public static <K, V> String toString(Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue, String before,
            String after, Formatter<? super K> keyFormatter, Formatter<? super V> valueFormatter) {
        if ( m == null ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, keyFormatter, valueFormatter);
        return sb.toString();
    }

    public static <K, V> void append(StringBuilder sb, Map<K, V> m, String delimiter) {
        append(sb, m, delimiter, "=", true, null, null, ToStringFormatter.getInstance(), ToStringFormatter.getInstance());
    }

    public static <K, V> void append(StringBuilder sb, Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue) {
        append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, null, null, ToStringFormatter.getInstance(),
                ToStringFormatter.getInstance());
    }

    public static <K, V> void append(StringBuilder sb, Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue,
            String before, String after) {
        append(sb, m, delimiter, entryDelimiter, includeEntryDelimiterOnEmptyValue, before, after, ToStringFormatter.getInstance(),
                ToStringFormatter.getInstance());
    }

    public static <K, V> void append(StringBuilder sb, Map<K, V> m, String delimiter, String entryDelimiter, boolean includeEntryDelimiterOnEmptyValue,
            String before, String after, Formatter<? super K> keyFormatter, Formatter<? super V> valueFormatter) {
        if ( m.isEmpty() ) {
            return;
        }
        if ( before != null ) {
            sb.append(before);
        }
        boolean first = true;
        for ( Entry<K, V> entry : m.entrySet() ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(keyFormatter.format(entry.getKey()));
            String valueString = valueFormatter.format(entry.getValue());
            if ( includeEntryDelimiterOnEmptyValue || !valueString.equals("") ) {
                sb.append(entryDelimiter);
                sb.append(valueString);
            }
        }
        if ( after != null ) {
            sb.append(after);
        }
    }

    /**
     * Simple split method that preserves empty-string tokens. Java's split
     * method strips out empty tokens.
     */
    public static List<String> split(String s, char delimiter) {
        List<String> tokens = new ArrayList<String>();
        for ( int start = 0, index = 0;; start = index + 1 ) {
            index = s.indexOf(delimiter, start);
            if ( index != -1 ) {
                tokens.add(s.substring(start, index));
            } else {
                tokens.add(s.substring(start));
                break;
            }
        }
        return tokens;
    }

    public static String leftPad(String s, char pad, int length) {
        int padLength = s.length() - length;
        if ( padLength > 0 ) {
            StringBuilder sb = new StringBuilder();
            for ( ; padLength > 0; padLength-- ) {
                sb.append(pad);
            }
            sb.append(s);
            return sb.toString();
        }
        return s;
    }
}
