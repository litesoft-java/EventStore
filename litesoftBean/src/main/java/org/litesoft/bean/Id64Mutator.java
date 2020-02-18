package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface Id64Mutator<T extends Id64Mutator<T>> extends Id64Accessor {
    void setId( Long pId );

    default T withId( Long pId ) {
        setId( pId );
        return Cast.it( this );
    }

    default T withId( Id64Accessor pAccessor ) {
        return withId( Id64Accessor.deNull( pAccessor ).getId() );
    }
}
