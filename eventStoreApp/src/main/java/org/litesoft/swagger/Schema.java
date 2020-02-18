package org.litesoft.swagger;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.validation.Validatable;
import org.litesoft.validation.Validation;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class Schema<T extends Schema<T>> implements Validatable {

    protected abstract SchemaMD<T> schemaMD();

    @SuppressWarnings("unchecked")
    protected T us() {
        return (T)this;
    }

    protected T them( Object o ) {
        if ( (o != null) && (this.getClass() == o.getClass()) ) { // Left to Right!
            return (T)o;
        }
        return null;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public final boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        T them = them( o ); // Checks parameter's Class
        if ( them == null ) {
            return false;
        }
        List<NamedFunction<T>> zEqualNFs = schemaMD().getEqualsSuppliers();
        if ( !zEqualNFs.isEmpty() ) {
            T us = us();
            for ( NamedFunction<T> zNF : zEqualNFs ) {
                Function<T, Object> zFunction = zNF.getFunction();
                if ( !Objects.equals( zFunction.apply( us ), zFunction.apply( them ) ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        List<NamedFunction<T>> zHashNFs = schemaMD().getHashCodeSuppliers();
        int zHash = 0;
        if ( !zHashNFs.isEmpty() ) {
            T us = us();
            for ( NamedFunction<T> zNF : zHashNFs ) {
                Object element = zNF.getFunction().apply( us );
                zHash = 31 * zHash + (element == null ? 0 : element.hashCode());
            }
        }
        return zHash;
    }

    protected Validation customValidate( Validation pValidation ) {
        return pValidation;
    }

    /**
     * Validate this Schema object.
     *
     * @return Validation object (never Null)
     */
    @Override
    public final Validation validate() {
        Validation zValidation = Validation.NO_ERRORS;
        List<NamedFunction<T>> zRequiredNFs = schemaMD().getRequiredSuppliers();
        if ( !zRequiredNFs.isEmpty() ) {
            T us = us();
            for ( NamedFunction<T> zNF : zRequiredNFs ) {
                Object zNullable = requiredNormalization( zNF.getFunction().apply( us ) );
                if ( zNullable == null ) {
                    zValidation = zValidation.addError( zNF.getName(), "Required!" );
                } else if ( zNullable instanceof Schema ) {
                    zValidation = zValidation.addError( zNF.getName(), ((Schema<?>)zNullable).validate() );
                }
            }
        }
        zValidation = customValidate( zValidation );
        return zValidation;
    }

    protected static Object requiredNormalization( Object o ) {
        if ( o instanceof String ) {
            String zTrimmed = o.toString().trim();
            if ( zTrimmed.isEmpty() ) {
                return null;
            }
        }
        return o;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder().append( "class " ).append( this.getClass().getSimpleName() ).append( " {" );

        List<NamedFunction<T>> zToStringNFs = schemaMD().getToStringSuppliers();
        if ( !zToStringNFs.isEmpty() ) {
            T us = us();
            sb.append( '\n' );
            for ( NamedFunction<T> zNF : zToStringNFs ) {
                sb.append( "    " ).append( zNF.getName() ).append( ": " )
                        .append( toIndentedString( zNF.getFunction().apply( us ) ) )
                        .append( '\n' );
            }
        }
        return sb.append( '}' ).toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    protected String toIndentedString( Object o ) {
        if ( o == null ) {
            return "null";
        }
        String string = o.toString();
        if ( string == null ) {
            return "?WTH?";
        }
        string = string.replace( "\n", "\n    " );
        if ( o instanceof String ) {
            string = "\"" + string.replace( "\"", "\\\"" ) + "\"";
        }
        return string;
    }
}
