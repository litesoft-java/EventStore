package org.litesoft.swagger;

import java.util.HashMap;
import java.util.Map;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.alleviative.Cast;
import org.litesoft.alleviative.beans.SetValue;

public abstract class MappedSchema<T extends MappedSchema<T>> extends Schema<T> {
    private final Map<String, Object> mFields = new HashMap<>();

    private Object rawGet( NamedFunction<T> pField ) {
        return mFields.get( pField.getName() );
    }

    public final <FT> SetValue<FT> fieldGetSetValue( NamedFunction<T> pField ) {
        FT zValue = fieldGet( pField );
        if ( (zValue == null) && !mFields.containsKey( pField.getName() ) ) {
            return null;
        }
        return () -> zValue;
    }

    public final <FT> FT fieldGet( NamedFunction<T> pField ) {
        return Cast.it( rawGet( pField ) );
    }

    public final <FT> void fieldSet( NamedFunction<T> pField, FT pValue ) {
        // if ( allowSet( pField, pValue ) ) {
        mFields.put( pField.getName(), pValue );
        // }
    }
}
