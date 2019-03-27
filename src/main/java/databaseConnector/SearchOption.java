package databaseConnector;

/**
 * This enum can be used to indicate the attribute to use when creating a query.
 *
 * @author Cas van Rijbroek
 * @version 1.0
 * 27-03-2019
 */
public enum SearchOption {
    NAME("name"), ID("header_id");

    /**
     * The current value of the instance.
     */
    private final String val;

    /**
     * A constructor to set the value.
     *
     * @param val the value to set
     */
    SearchOption(String val) {
        this.val = val;
    }

    /**
     * Override to make to enum display the value when used as a string.
     *
     * @return the current value of the enum
     */
    @Override
    public String toString() {
        return val;
    }
}
