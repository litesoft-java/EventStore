package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishForbiddenException extends RestishException {
    public RestishForbiddenException() {
        this(403, "Error - Forbidden to change another user's Events");
    }

    public RestishForbiddenException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
