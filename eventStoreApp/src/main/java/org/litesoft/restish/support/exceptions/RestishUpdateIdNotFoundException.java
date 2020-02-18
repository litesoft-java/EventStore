package org.litesoft.restish.support.exceptions;

public class RestishUpdateIdNotFoundException extends RestishException {
    public RestishUpdateIdNotFoundException() {
        this( "not found to update" );
    }

    public RestishUpdateIdNotFoundException( String pMessageExtension ) {
        this( 404, "Error - ID " + pMessageExtension );
    }

    public RestishUpdateIdNotFoundException( int pStatusCode, String message ) {
        super( pStatusCode, message );
    }
}
