package org.litesoft.events.exceptions;

import org.litesoft.restish.support.exceptions.RestishException;

public class RestishEventForbiddenException extends RestishException {
    public RestishEventForbiddenException() {
        this( 403, "Error - Forbidden to change another user's Events" );
    }

    public RestishEventForbiddenException( int pStatusCode, String message ) {
        super( pStatusCode, message );
    }
}
