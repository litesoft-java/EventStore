package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishInvalidObjectException extends RestishException {
    public RestishInvalidObjectException() {
        this(400, "Error - invalid input, object invalid");
    }

    public RestishInvalidObjectException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
