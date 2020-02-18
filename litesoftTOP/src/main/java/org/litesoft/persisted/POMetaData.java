package org.litesoft.persisted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.litesoft.alleviative.functionalinterfaces.FluentSetter;

@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})
public class POMetaData<T extends IPersistedObject, B extends POBuilder<T>> {
    private static final String ID_NAME = "id";
    private static final String ROW_VERSION_NAME = "version";

    private final Map<String, POField<T, B, ?>> mFieldsByName = new LinkedHashMap<>(); // Retain Add Order
    private final Class<T> mClassT;
    private final POField<T, B, ?> mIdField;
    private final POField<T, B, ?> mVersionField;
    private final List<POField<T, B, ?>> mOtherFields;
    private Function<T, B> mPOBuilderFactory;

    public void builderFactory( Function<T, B> pPOBuilderFactory ) {
        mPOBuilderFactory = pPOBuilderFactory;
    }

    private POMetaData( Builder<T, B> pBuilder ) {
        this.mClassT = pBuilder.mClassT;
        this.mIdField = pBuilder.mIdField;
        this.mVersionField = pBuilder.mVersionField;
        this.mOtherFields = Collections.unmodifiableList( new ArrayList<>( pBuilder.mOtherFields ) );
        addField( mIdField );
        addField( mVersionField );
        for ( POField<T, B, ?> zField : mOtherFields ) {
            addField( zField );
        }
    }

    private void addField( POField<T, B, ?> pField ) {
        if ( pField != null ) {
            POField<T, B, ?> zPreviousField = mFieldsByName.put( pField.getName(), pField );
            if ( zPreviousField != null ) {
                throw new Error( "Duplicate Field Registration on '" + getTypeSimpleName() + "':"
                                 + "\n   1st: " + zPreviousField
                                 + "\n   2nd: " + pField );
            }
        }
    }

    public static <T extends IPersistedObject,
            B extends POBuilder<T>> Builder<T, B> builder( Class<T> pClassT, Class<B> pClassB ) {
        return new Builder<>( pClassT, pClassB );
    }

    public String getTypeSimpleName() {
        return mClassT.getSimpleName();
    }

    public POField<T, B, ?> getIdField() {
        return mIdField;
    }

    public POField<T, B, ?> getVersionField() {
        return mVersionField;
    }

    public List<POField<T, B, ?>> getOtherFields() {
        return mOtherFields;
    }

    public B createPOBuilder() {
        return createPOBuilder( null );
    }

    public B createPOBuilder( T pEntity ) {
        Function<T, B> zFactory = mPOBuilderFactory;
        if ( zFactory != null ) {
            return zFactory.apply( pEntity );
        }
        throw new IllegalStateException( "No concrete '" + getTypeSimpleName() + "' Builder set" );
    }

    public String toString( T pPO ) {
        StringBuilder sb = new StringBuilder().append( getTypeSimpleName() ).append( " = " );
        char zPrefix = '{';
        for ( POField<T, B, ?> zField : mFieldsByName.values() ) {
            Object zValue = zField.getGetter().apply( pPO );
            sb.append( zPrefix ).append( "\n   " ).append( zField.getName() ).append( '=' );
            if ( !(zValue instanceof String) ) {
                sb.append( zValue );
            } else {
                String zString = (String)zValue;
                if ( !zString.isEmpty() && zField.isSecure() ) {
                    appendSecure( sb, zString );
                } else {
                    appendQuoted( sb, zString );
                }
            }
            zPrefix = ',';
        }
        if ( zPrefix == '{' ) {
            sb.append( "{}" );
        } else {
            sb.append( "\n}" );
        }
        return sb.toString();
    }

    protected void appendSecure( StringBuilder pSB, String pStringNotEmpty ) {
        for ( int i = pStringNotEmpty.length(); i > 0; i-- ) {
            pSB.append( '*' );
        }
    }

    protected void appendQuoted( StringBuilder pSB, String pStringNotNull ) {
        pSB.append( "'" ).append( pStringNotNull ).append( "'" );
    }

    public boolean areEqual( T pLeft, T pRight ) {
        return areEqualForFields( pLeft, pRight, mFieldsByName.values() );
    }

    public boolean areEquivalent( T pLeft, T pRight ) {
        return areEqualForFields( pLeft, pRight, mOtherFields );
    }

    private boolean areEqualForFields( T pLeft, T pRight, Collection<POField<T, B, ?>> pFields ) {
        if ( pLeft == pRight ) {
            return true;
        }
        if ( (pLeft == null) || (pRight == null) ) {
            return false;
        }
        if ( pLeft.getClass() != pRight.getClass() ) {
            return false;
        }
        for ( POField<T, B, ?> zField : pFields ) {
            if ( !Objects.equals( zField.getGetter().apply( pLeft ),
                                  zField.getGetter().apply( pRight ) ) ) {
                return false;
            }
        }
        return true;
    }

    public T copyAll( T pEntity ) {
        return (pEntity == null) ? null : createPOBuilder( pEntity ).build();
    }

    public B copyInsecureForInsert( T pEntity ) {
        if ( pEntity == null ) {
            return null;
        }
        B zBuilder = createPOBuilder();
        for ( POField<T, B, ?> zField : mOtherFields ) {
            if ( !zField.isSecure() ) {
                zField.getSetter().apply( zBuilder, cast( zField.getGetter().apply( pEntity ) ) );
            }
        }
        return zBuilder;
    }

    @SuppressWarnings("unchecked")
    private <FT> FT cast( Object pValue ) {
        return (FT)pValue;
    }

    public static class Builder<T extends IPersistedObject, B extends POBuilder<T>> {
        private final Class<T> mClassT;
        private final Class<B> mClassB;
        private POField<T, B, ?> mIdField;
        private POField<T, B, ?> mVersionField;
        private final List<POField<T, B, ?>> mOtherFields = new ArrayList<>();

        private Builder( Class<T> pClassT, Class<B> pClassB ) {
            mClassT = pClassT;
            mClassB = pClassB;
        }

        public <FT> Builder<T, B> id( Class<FT> pClass, Function<T, FT> pGetter ) {
            mIdField = POField.with( ID_NAME, pClass, pGetter, null );
            return this;
        }

        public <FT> Builder<T, B> version( Class<FT> pClass, Function<T, FT> pGetter ) {
            mVersionField = POField.with( ROW_VERSION_NAME, pClass, pGetter, null );
            return this;
        }

        public <FT> Builder<T, B> field( String pName, Class<FT> pClass, Function<T, FT> pGetter, FluentSetter<B, FT> pSetter ) {
            mOtherFields.add( POField.with( pName, pClass, pGetter, pSetter ) );
            return this;
        }

        public Builder<T, B> secureField( String pName, Function<T, String> pGetter, FluentSetter<B, String> pSetter ) {
            mOtherFields.add( POField.withSecure( pName, pGetter, pSetter ) );
            return this;
        }

        public POMetaData<T, B> build() {
            return new POMetaData<>( this );
        }
    }
}
