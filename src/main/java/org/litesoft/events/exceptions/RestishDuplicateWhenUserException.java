package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishDuplicateWhenUserException extends RestishException {
    public RestishDuplicateWhenUserException() {
        this(409, "Error - existing Event with the same User & When");
    }

    public RestishDuplicateWhenUserException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
