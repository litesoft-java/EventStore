package org.litesoft.restish.support.exceptions;

public class RestishBadParamException extends RestishException {
    public RestishBadParamException() {
        this(404, "Error - bad input parameter");
    }

    public RestishBadParamException(int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
