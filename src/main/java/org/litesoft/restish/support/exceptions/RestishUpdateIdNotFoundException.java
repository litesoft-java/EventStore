package org.litesoft.restish.support.exceptions;

public class RestishUpdateIdNotFoundException extends RestishException {
    public RestishUpdateIdNotFoundException() {
        this(404, "Error - ID not found to update");
    }

    public RestishUpdateIdNotFoundException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
