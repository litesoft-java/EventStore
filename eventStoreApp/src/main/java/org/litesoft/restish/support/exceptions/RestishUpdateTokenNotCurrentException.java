package org.litesoft.restish.support.exceptions;

@SuppressWarnings("unused")
public class RestishUpdateTokenNotCurrentException extends RestishException {
    public RestishUpdateTokenNotCurrentException() {
        this("");
    }

    public RestishUpdateTokenNotCurrentException( String pMessageExtension) {
        this(428, "Error - 'updateToken' not current" + pMessageExtension);
    }

    public RestishUpdateTokenNotCurrentException( int pStatusCode, String message) {
        super(pStatusCode, message);
    }
}
