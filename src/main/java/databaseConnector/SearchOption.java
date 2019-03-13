package databaseConnector;

public enum SearchOption {
    NAME("name"), ID("header_id");

    private final String val;

    SearchOption(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
