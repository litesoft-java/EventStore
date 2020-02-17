package org.litesoft.events.exceptions;

import org.litesoft.restish.support.exceptions.RestishException;

public class RestishDuplicateWhenUserException extends RestishException {
    public RestishDuplicateWhenUserException() {
        this("");
    }

    public RestishDuplicateWhenUserException( String pMessageExtension ) {
        this(409, "Error - existing Event with the same User & When" + pMessageExtension);
    }

    public RestishDuplicateWhenUserException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
