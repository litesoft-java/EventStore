package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishNoChangeException extends RestishException {
    public RestishNoChangeException() {
        this(204, "Success - Current Event properties already match requested changes (No Content)");
    }

    public RestishNoChangeException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
