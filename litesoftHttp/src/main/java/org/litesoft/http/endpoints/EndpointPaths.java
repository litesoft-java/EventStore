package org.litesoft.http.endpoints;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EndpointPaths {
    private static final Map<String, PathMetaData> REGISTRY_BY_NAMES = new HashMap<>();
    private static final Map<String, PathMetaData> REGISTRY_BY_PATHS = new HashMap<>();

    public static PathMetaData register( PathMetaData pMetaData ) {
        String zName = Objects.requireNonNull( pMetaData, "PathMetaData" ).name();
        checkAddDup( REGISTRY_BY_NAMES, "name", pMetaData, zName );
        checkAddDup( REGISTRY_BY_PATHS, "path", pMetaData, pMetaData.path() );
        return pMetaData;
    }

    private static void checkAddDup( Map<String, PathMetaData> pRegistry, String pWhat, PathMetaData pMetaData, String pKey ) {
        PathMetaData zDuplicate = pRegistry.put( pMetaData.path(), pMetaData );
        if ( zDuplicate != null ) {
            throw new Error( "Duplicate PathMetaData registered for '" + pWhat + "': " + pKey );
        }
    }
}
