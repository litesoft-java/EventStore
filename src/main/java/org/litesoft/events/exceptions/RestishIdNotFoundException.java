package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishIdNotFoundException extends RestishException {
    public RestishIdNotFoundException() {
        this(404, "Error - Event with 'id' not found");
    }

    public RestishIdNotFoundException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
