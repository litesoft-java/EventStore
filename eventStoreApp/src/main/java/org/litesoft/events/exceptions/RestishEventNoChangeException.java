package org.litesoft.events.exceptions;

import org.litesoft.restish.support.exceptions.RestishException;

public class RestishEventNoChangeException extends RestishException {
    public RestishEventNoChangeException() {
        this(204, "Success - Current Event properties already match requested changes (No Content)");
    }

    public RestishEventNoChangeException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
