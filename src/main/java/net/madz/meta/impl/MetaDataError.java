package net.madz.meta.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.meta.FlavorMetaData;
import net.madz.meta.KeySet;
import net.madz.meta.MetaData;
import net.madz.meta.MetaDataFilter;

/**
 * Special Handler that records errors encountered during meta-data processing.
 * 
 * This class is instantiated automatically by the <tt>MetaDataHandlerMap</tt>
 * class.
 */
public class MetaDataError<M extends MetaData> implements FlavorMetaData<M> {

    private final LinkedList<String> errors = new LinkedList<String>();
    private final static KeySet KEY_SET = new KeySet(MetaDataError.class);

    /**
     * Default constructor
     */
    public MetaDataError() {}

    @Override
    public KeySet getKeySet() {
        return KEY_SET;
    }

    /**
     * Add an error to the error list
     */
    public void addError(Throwable e) {
        // Flatten exceptions caused by reflection
        while ( e instanceof InvocationTargetException ) {
            e = ( (InvocationTargetException) e ).getTargetException();
        }
        if ( e instanceof IllegalStateException ) {
            errors.add(e.getMessage());
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            e.printStackTrace(pout);
            errors.add(out.toString());
        }
    }

    /**
     * List of all error messages
     * 
     * @return List of all error strings
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    @Override
    public String toString() {
        return "Errors" + errors;
    }

    @Override
    public MetaDataError<M> filter(MetaData owner, MetaDataFilter filter, boolean lazyFilter) {
        return this;
    }
}
