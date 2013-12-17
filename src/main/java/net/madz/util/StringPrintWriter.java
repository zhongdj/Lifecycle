package net.madz.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * PrintWriter that can return its parent writer.
 * 
 * This class is typically used to print to strings, and therefore prefers being
 * wrapper by a StringWriter.
 * 
 * <h3>Usage</h3>
 * 
 * <pre>
 *    Exception e = ...;
 *    
 *    StringPrintWriter strWriter= new StringPrintWriter();
 *    e.printStackTrace( strWriter );
 *    System.out.println( "Exception is: " + strWriter );
 * </pre>
 * 
 */
public class StringPrintWriter extends PrintWriter {

    private final Writer base;

    /**
     * Default constructor
     */
    public StringPrintWriter() {
        this(new StringWriter());
    }

    /**
     * Constructor that wraps any arbitrary Writer.
     */
    public StringPrintWriter(Writer base) {
        super(base);
        this.base = base;
    }

    /**
     * Underlying Writer that this StringPrintWriter is writing to.
     * 
     * @return Writer based in to constructor, or the StringWriter created by
     *         the default constructor
     */
    public Writer getBaseWriter() {
        return this.base;
    }

    /**
     * Returns the String representation of the base writer
     * 
     * @return base.toString()
     */
    @Override
    public String toString() {
        return base.toString();
    }
}
