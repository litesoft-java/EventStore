package org.litesoft.alleviative;

import java.util.HashSet;
import java.util.Set;

import org.litesoft.alleviative.validation.Significant;

public class FileExtension {
    public static String extractFromOrEmpty( String pPath ) {
        if ( pPath != null ) {
            int zNameExtensionSepAt = pPath.lastIndexOf( '.' );
            if ( zNameExtensionSepAt != -1 ) {
                return pPath.substring( zNameExtensionSepAt + 1 ).trim();
            }
        }
        return "";
    }

    public static class Matcher {
        private final Set<String> mExtensionsToMutate = new HashSet<>();
        private final boolean mIgnoreExtensionCase;

        public Matcher( boolean pIgnoreExtensionCase, String... pExtensionsToMutate ) {
            mIgnoreExtensionCase = pIgnoreExtensionCase;
            if ( pExtensionsToMutate != null ) {
                for ( String zExtension : pExtensionsToMutate ) {
                    zExtension = Significant.orNull( zExtension );
                    if ( zExtension != null ) {
                        mExtensionsToMutate.add( normalize( zExtension ) );
                    }
                }
            }
        }

        /**
         * Determine if the File Extension of the <code>Path</code> matches (possibly case insensitively).
         *
         * @param pPath Nullable
         * @return null means No Match, !null means the extension matched (optionally lower cased)
         */
        public String match( String pPath ) {
            String zExtension = normalize( FileExtension.extractFromOrEmpty( pPath ) );
            return mExtensionsToMutate.contains( zExtension ) ? zExtension : null;
        }

        private String normalize( String pExtensionNotNull ) {
            return mIgnoreExtensionCase ? pExtensionNotNull.toLowerCase() : pExtensionNotNull;
        }
    }
}
