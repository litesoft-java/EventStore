package org.litesoft.alleviative;

import java.io.IOException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Exceptions {
    public static void ignore( Exception pException ) {
        // Ignore
    }

    public static RuntimeException optionallyWrap( Exception pException ) {
        if ( pException instanceof RuntimeException ) {
            return Cast.it( pException );
        }
        if ( pException instanceof IOException ) {
            return wrap( Cast.it( pException ) );
        }
        return new RuntimeException( pException );
    }

    public static RuntimeIOException wrap( IOException pException ) {
        return new RuntimeIOException( pException );
    }

    public static class RuntimeIOException extends RuntimeException {
        public RuntimeIOException( IOException cause ) {
            super( cause );
        }

        public RuntimeIOException( String message, IOException cause ) {
            super( message, cause );
        }
    }
}
