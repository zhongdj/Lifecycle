package net.madz.util.json.io;

import java.io.PrintWriter;
import java.math.BigDecimal;

import net.madz.util.StringPrintWriter;

/**
 * Writes JSON formatted names and values to a Writer.
 */
public class JsonWriter {

    private Formatter formatter;
    private final PrintWriter out;

    /**
     * Create a new JsonWriter that writes to a StringPrintWriter.
     * 
     * <p>
     * The String contents can be obtained by calling <tt>toString</tt> on this
     * object.
     * </p>
     */
    public JsonWriter() {
        this(new StringPrintWriter(), JsonTextType.object, false);
    }

    /**
     * Create a new JsonWriter wrapped around a PrintWriter
     */
    public JsonWriter(PrintWriter out) {
        this(out, JsonTextType.object, false);
    }

    /**
     * Create a new JsonWriter wrapped around a PrintWriter
     */
    public JsonWriter(PrintWriter out, JsonTextType jsonText, boolean anonymous) {
        assert null != out;
        this.out = out;
        this.formatter = new Formatter(null, out, "", true, null).startSection(null, jsonText, anonymous);
    }

    private static class Formatter {

        private final Formatter parent;
        private final PrintWriter out;
        private final String prefix;
        private boolean firstElement = true;
        private boolean skipLabels;
        private final JsonTextType jsonText;

        public Formatter(Formatter parent, PrintWriter out, String prefix, boolean skipLabels, JsonTextType jsonText) {
            this.parent = parent;
            this.out = out;
            this.prefix = prefix;
            this.skipLabels = skipLabels;
            this.jsonText = jsonText;
        }

        public Formatter startSection(String label, final JsonTextType jsonText, boolean anonymous) {
            startElement(label);
            print(jsonText.getBeginString());
            return new Formatter(this, out, this.prefix + "\t", anonymous, jsonText);
        }

        private Formatter doEndSection(String endString) {
            if ( !firstElement ) {
                out.println();
                out.print(this.prefix);
            }
            print(endString);
            return this;
        }

        public Formatter endSection() {
            return parent.doEndSection(jsonText.getEndString());
        }

        public void startElement(String name) {
            if ( !firstElement ) {
                out.print(",");
            }
            firstElement = false;
            if ( null != this.parent ) {
                out.println();
            }
            out.print(this.prefix);
            if ( null != name && !skipLabels ) {
                printString(name);
                out.print(": ");
            }
        }

        public void print(String str) {
            out.print(str);
        }

        public void printString(String str) {
            if ( null == str ) {
                out.print("null");
            } else {
                out.print("\"");
                for ( char ch : str.toCharArray() ) {
                    switch (ch) {
                        case '"':
                            out.print("\\\"");
                            break;
                        case '\b':
                            out.print("\\b");
                            break;
                        case '\f':
                            out.print("\\f");
                            break;
                        case '\n':
                            out.print("\\n");
                            break;
                        case '\r':
                            out.print("\\r");
                            break;
                        case '\t':
                            out.print("\\t");
                            break;
                        case '\\':
                            out.print("\\\\");
                            break;
                        default:
                            if ( Character.isISOControl(ch) || ch > 127 ) {
                                out.print(String.format("\\u+%04x", ( (int) ch ) & 0x0ffff));
                            } else {
                                out.print(ch);
                            }
                    }
                }
                out.print("\"");
            }
        }
    }

    /**
     * Start a JSON Object element.
     * 
     * <p>
     * The
     * 
     * @param label
     */
    public void startObject(String label) {
        this.formatter = this.formatter.startSection(label, JsonTextType.object, false);
    }

    public void startAnonymousObject(String label) {
        this.formatter = this.formatter.startSection(label, JsonTextType.object, true);
    }

    public void endObject() {
        formatter = this.formatter.endSection();
    }

    public void startArray(String label) {
        this.formatter = this.formatter.startSection(label, JsonTextType.array, false);
    }

    public void startAnonymousArray(String label) {
        this.formatter = this.formatter.startSection(label, JsonTextType.array, true);
    }

    public void endArray() {
        formatter = this.formatter.endSection();
    }

    /**
     * Write a named number in JSON format.
     * 
     * @param name
     *            Name written as a JSON String.
     * @param value
     *            Value (may be null) as a simple JSON decimal.
     */
    public void printNumber(String name, BigDecimal value) {
        formatter.startElement(name);
        if ( null == value ) {
            formatter.print("null");
        } else {
            formatter.print(value.toPlainString());
        }
    }

    /**
     * Write a named number in JSON format.
     * 
     * @param name
     *            Name written as a JSON String.
     * @param value
     *            Value (may be null) as a simple JSON decimal.
     */
    public void printNumber(String name, Number value) {
        formatter.startElement(name);
        if ( null == value ) {
            formatter.print("null");
        } else {
            formatter.print(value.toString());
        }
    }

    /**
     * Write a named boolean in JSON format.
     * 
     * @param name
     *            Name written as a JSON String.
     * @param value
     *            Value (may be null) as a JSON boolean.
     */
    public void printBoolean(String name, Boolean value) {
        formatter.startElement(name);
        if ( null == value ) {
            formatter.print("null");
        } else {
            formatter.print(value.toString());
        }
    }

    /**
     * Write a named decimal in JSON format.
     * 
     * @param name
     *            Name written as a JSON String.
     * @param value
     *            Value (may be null) as a JSON decimal.
     */
    public void printEngineeringNumber(String name, BigDecimal value) {
        formatter.startElement(name);
        if ( null == value ) {
            formatter.print("null");
        } else {
            formatter.print(value.toEngineeringString());
        }
    }

    /**
     * Write a named string in JSON format.
     * 
     * @param name
     *            Name written as a JSON String.
     * @param value
     *            Value (may be null) as a JSON String.
     */
    public void printString(String name, String str) {
        formatter.startElement(name);
        formatter.printString(str);
    }

    public void finish() {
        while ( this.formatter.parent != null ) {
            this.formatter = this.formatter.endSection();
        }
    }

    public void close() {
        finish();
        out.close();
    }

    @Override
    public String toString() {
        return out.toString();
    }

    public static void main(String[] parms) {
        JsonWriter writer = new JsonWriter(new PrintWriter(System.out));
        writer.startObject("Bob");
        writer.printString("name", "Larry Has a \"Quote\"");
        writer.printString("type", "Object");
        writer.printString("iso", "This has a tab(\t), a backslash (\\), a newline(\n), a null (\0), as well as some \u2341 stuff");
        writer.printNumber("value", 1243);
        writer.startArray("numberArray");
        try {
            for ( int idx = 1; idx < 10; idx++ ) {
                writer.printNumber(null, idx);
            }
        } finally {
            writer.endArray();
        }
        writer.endObject();
        writer.close();
    }
}
