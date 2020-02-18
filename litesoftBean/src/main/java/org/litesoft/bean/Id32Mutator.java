package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface Id32Mutator<T extends Id32Mutator<T>> extends Id32Accessor {
    void setId( Integer pId );

    default T withId( Integer pId ) {
        setId( pId );
        return Cast.it( this );
    }

    default T withId( Id32Accessor pAccessor ) {
        return withId( Id32Accessor.deNull( pAccessor ).getId() );
    }
}
