package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface VersionMutator<T extends VersionMutator<T>> extends VersionAccessor {
    void setVersion( Integer pVersion );

    default T withVersion( Integer pVersion ) {
        setVersion( pVersion );
        return Cast.it( this );
    }

    default T withVersion( VersionAccessor pAccessor ) {
        return withVersion( VersionAccessor.deNull( pAccessor ).getVersion() );
    }
}
