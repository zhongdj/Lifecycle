package net.madz.common;

/**
 * Debugging mechanism in which an object writes details of its state to a
 * PrintWriter.
 */
public interface Dumpable {

    /**
     * Generate detailed debug information.
     * 
     * @param out
     *            PrintWriter to write to
     * @param prefix
     *            Prefix to write to the beginning of every line.
     */
    /**
     * Dump out the contents using the given indentation. Any subelements should
     * be additionally indented.
     * 
     * @param out
     */
    void dump(Dumper dumper);
}
