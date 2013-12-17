package net.madz.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TestFormatter extends Formatter {

    private String sourceClassName;

    @Override
    public String format(LogRecord record) {
        try {
            if ( record.getSourceClassName().equals(sourceClassName) ) {
                return String.format("\n[%s]: %s", record.getLevel(), record.getMessage());
            } else {
                return String.format("\n\n[%s]: %s", record.getLevel(), record.getMessage());
            }
        } finally {
            sourceClassName = record.getSourceClassName();
        }
    }
}
