package ch.mrnjec.swat.bus;

public enum Status {
    OK(200, "Success"),
    ERROR(500, "Internal error"),
    BAD_REQUEST(400, "Bad request"),
    PRODUCTS_NOT_AVAILABLE(413, "Products not available");

    private final int statusCode;
    private final String message;

    Status(final int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getValue() {
        return this.statusCode;
    }
}
