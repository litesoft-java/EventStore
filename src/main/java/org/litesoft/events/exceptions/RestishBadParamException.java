package org.litesoft.events.exceptions;

import org.litesoft.exceptions.RestishException;

public class RestishBadParamException extends RestishException {
    public RestishBadParamException() {
        this(404, "Error - bad input parameter");
    }

    public RestishBadParamException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
