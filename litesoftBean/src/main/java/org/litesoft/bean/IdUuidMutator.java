package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface IdUuidMutator<T extends IdUuidMutator<T>> extends IdUuidAccessor {
    void setId( String pId );

    default T withId( String pId ) {
        setId( pId );
        return Cast.it( this );
    }

    default T withId( IdUuidAccessor pAccessor ) {
        return withId( IdUuidAccessor.deNull( pAccessor ).getId() );
    }
}
