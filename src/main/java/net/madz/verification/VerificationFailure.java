package net.madz.verification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.madz.common.DottedPath;
import net.madz.common.Dumpable;
import net.madz.common.Dumper;
import net.madz.meta.MetaData;
import net.madz.util.MetaDataUtil;
import net.madz.util.StringPrintWriter;
import net.madz.util.json.io.JsonWriter;

public class VerificationFailure implements Dumpable, Cloneable {

    private final Object source;
    private final DottedPath errorKey;
    private final String defaultErrorMessage;
    private final Object[] details;
    private final Throwable cause;
    private final StackTraceElement[] stack;
    private String errorCode;

    public void writeJson(JsonWriter writer) throws IOException {
        writer.startObject("Error");
        try {
            writer.printString("code", errorKey.toString());
            writer.printString("message", getErrorMessage(null));
        } finally {
            writer.endObject();
        }
    }

    public VerificationFailure(Throwable cause, Object source, String errorKey, String errorCode, String defaultErrorMessage, Object... details) {
        this(cause, source, errorKey, defaultErrorMessage, details);
        this.errorCode = errorCode;
    }

    /**
     * Constructor
     * 
     * @param metaData
     *            Meta definition of item with an error
     * @param errorKey
     *            Key for the error message, will have fully-qualified meta name
     *            prepended to it
     * @param defaultErrorMessage
     *            Error message to display if the error resource is not defined
     * @param details
     *            Error parameters
     */
    public VerificationFailure(Throwable cause, Object source, String errorKey, String defaultErrorMessage, Object... details) {
        this.cause = cause;
        this.source = source;
        List<String> segements = new ArrayList<>();
        for ( String segement : errorKey.split("\\.") ) {
            segements.add(segement);
        }
        this.errorKey = source instanceof MetaData ? ( (MetaData) source ).getDottedPath().append(segements) : DottedPath.parse(errorKey);
        this.defaultErrorMessage = defaultErrorMessage;
        this.details = details;
        this.stack = Thread.currentThread().getStackTrace();
    }

    /**
     * Constructor
     * 
     * @param metaData
     *            Meta definition of item with an error
     * @param errorKey
     *            Key for the error message, will have fully-qualified meta name
     *            prepended to it
     * @param defaultErrorMessage
     *            Error message to display if the error resource is not defined
     * @param details
     *            Error parameters
     */
    public VerificationFailure(Object source, String errorKey, String defaultErrorMessage, Object... details) {
        this(null, source, errorKey, defaultErrorMessage, details);
    }

    public VerificationFailure(Object source, String errorKey, String errorCode, String defaultErrorMessage, Object... details) {
        this(null, source, errorKey, errorCode, defaultErrorMessage, details);
    }

    /** Clone constructor */
    private VerificationFailure(VerificationFailure clone) {
        this.cause = clone.cause;
        this.source = clone.source;
        this.errorKey = clone.errorKey;
        this.defaultErrorMessage = clone.defaultErrorMessage;
        this.details = clone.details;
        this.stack = clone.stack;
    }

    @Override
    public VerificationFailure clone() {
        return new VerificationFailure(this);
    }

    /**
     * Meta data of object/field that is in error
     */
    public Object getSource() {
        return this.source;
    }

    /**
     * Madz error key for verification failure
     */
    public DottedPath getErrorKey() {
        return this.errorKey;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof VerificationFailure ) {
            VerificationFailure comp = (VerificationFailure) obj;
            return comp.source.equals(this.source) && comp.errorKey.equals(this.errorKey);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ( source.hashCode() * 7 ) + errorKey.hashCode();
    }

    @Override
    public String toString() {
        return errorCode + "(" + String.format(defaultErrorMessage, details) + ")";
    }

    public String getErrorMessage(String overrideErrorMessage) {
        return String.format(MetaDataUtil.coalesce(overrideErrorMessage, defaultErrorMessage), details);
    }

    @Override
    public void dump(Dumper dumper) {
        dumper.print(errorCode).print(": ").println(String.format(defaultErrorMessage, details));
        if ( null != cause ) {
            StringPrintWriter str = new StringPrintWriter();
            this.cause.printStackTrace(str);
            dumper.indent().println(str);
        } else {
            dumper.indent().dump(stack);
        }
    }
}