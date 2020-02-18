package org.litesoft.restish.support.exceptions;

public class RestishInvalidObjectException extends RestishException {
    public RestishInvalidObjectException() {
        this( "invalid" );
    }

    public RestishInvalidObjectException( String pMessageExtension ) {
        this( 400, "Error - invalid input, object " + pMessageExtension );
    }

    public RestishInvalidObjectException( int pStatusCode, String message ) {
        super( pStatusCode, message );
    }
}
