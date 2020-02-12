package org.litesoft.restish.support.exceptions;

public class RestishInvalidObjectException extends RestishException {
    public RestishInvalidObjectException() {
        this(400, "Error - invalid input, object invalid");
    }

    public RestishInvalidObjectException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
