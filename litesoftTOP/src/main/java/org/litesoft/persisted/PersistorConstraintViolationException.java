package org.litesoft.persisted;

@SuppressWarnings("unused")
public class PersistorConstraintViolationException extends RuntimeException {
    public PersistorConstraintViolationException( String message ) {
        super( message );
    }

    public PersistorConstraintViolationException( String message, Throwable cause ) {
        super( message, cause );
    }
}
