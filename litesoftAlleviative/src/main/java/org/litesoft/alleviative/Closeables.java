package org.litesoft.alleviative;

import java.io.Closeable;
import java.io.IOException;

public class Closeables {
    public static void closeQuietly( Closeable pCloseable ) {
        try {
            if ( pCloseable != null ) {
                pCloseable.close();
            }
        }
        catch ( IOException ignoreIt ) {
            Exceptions.ignore( ignoreIt );
        }
    }
}
