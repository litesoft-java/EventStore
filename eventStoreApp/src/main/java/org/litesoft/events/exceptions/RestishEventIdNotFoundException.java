package org.litesoft.events.exceptions;

import org.litesoft.restish.support.exceptions.RestishException;

public class RestishEventIdNotFoundException extends RestishException {
    public RestishEventIdNotFoundException() {
        this( 404, "Error - Event with 'id' not found" );
    }

    public RestishEventIdNotFoundException( int pStatusCode, String message ) {
        super( pStatusCode, message );
    }
}
