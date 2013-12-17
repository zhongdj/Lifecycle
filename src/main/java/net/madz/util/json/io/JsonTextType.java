package net.madz.util.json.io;

/**
 * JsonText definition, per grammar.
 * 
 * @see http://tools.ietf.org/html/rfc4627
 */
public enum JsonTextType {
    /** JSON array text */
    array("[", "]"),
    /** JSON object text */
    object("{", "}");

    private final String beginString, endString;

    JsonTextType(String beginString, String endString) {
        this.beginString = beginString;
        this.endString = endString;
    }

    public String getBeginString() {
        return this.beginString;
    }

    public String getEndString() {
        return this.endString;
    }
}
