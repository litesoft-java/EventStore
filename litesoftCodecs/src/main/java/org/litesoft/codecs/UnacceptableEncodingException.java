package org.litesoft.codecs;

@SuppressWarnings({"unused", "WeakerAccess"})
public class UnacceptableEncodingException extends RuntimeException {
    public UnacceptableEncodingException( String message ) {
        super( message );
    }

    public UnacceptableEncodingException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UnacceptableEncodingException( Throwable cause ) {
        super( cause );
    }
}
