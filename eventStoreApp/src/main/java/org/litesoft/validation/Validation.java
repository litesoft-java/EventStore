package org.litesoft.validation;

public class Validation {
    public static final String NO_VALIDATION_ERRORS = "No Validation Errors";
    public static final Validation NO_ERRORS = new Validation();

    private static final int INDENT_DEPTH = 4;

    public boolean hasErrors() {
        return false;
    }

    public boolean hasErrorForAttribute( String pAttributeName ) {
        return false;
    }

    @Override
    public String toString() {
        return NO_VALIDATION_ERRORS;
    }

    public Validation addError( String pAttributeName, String pError ) {
        pAttributeName = assertAttributeNameSignificant( pAttributeName );
        pError = significantOrNull( pError );
        return (pError != null) ?
               new WithTextErrors( this, pAttributeName, pError ) :
               this;
    }

    public Validation addError( String pAttributeName, Validation pError ) {
        pAttributeName = assertAttributeNameSignificant( pAttributeName );
        return ((pError != null) && pError.hasErrors()) ?
               new WithValidationErrors( this, pAttributeName, pError ) :
               this;
    }

    public Validation addErrorsOnClass( Object pAttributeOwner ) {
        return (hasErrors() && (pAttributeOwner != null)) ?
               new ErroredClass( this, pAttributeOwner.getClass().getSimpleName() ) :
               this;
    }

    protected void appendError( StringBuilder sb, int pIndent ) {
    }

    protected String assertAttributeNameSignificant( String pAttributeName ) {
        pAttributeName = significantOrNull( pAttributeName );
        if ( pAttributeName == null ) {
            throw new IllegalArgumentException( "No Attribute Name provided" );
        }
        return pAttributeName;
    }

    protected String significantOrNull( String pStr ) {
        if ( pStr != null ) {
            pStr = pStr.trim();
            if ( !pStr.isEmpty() ) {
                return pStr;
            }
        }
        return null;
    }

    protected static abstract class AbstractWithErrors extends Validation {
        protected final Validation mNext;

        private AbstractWithErrors( Validation pNext ) {
            mNext = pNext;
        }

        @Override
        public boolean hasErrors() {
            return true;
        }

        @Override
        public boolean hasErrorForAttribute( String pAttributeName ) {
            return mNext.hasErrorForAttribute( pAttributeName );
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            appendError( sb, 0 );
            return sb.toString();
        }

        protected StringBuilder indent( StringBuilder sb, int pIndent ) {
            while ( pIndent-- > 0 ) {
                sb.append( ' ' );
            }
            return sb;
        }

        protected StringBuilder addNewLine( StringBuilder sb ) {
            if ( sb.length() != 0 ) {
                sb.append( '\n' );
            }
            return sb;
        }
    }

    protected static abstract class AbstractWithErroredAttribute extends AbstractWithErrors {
        private final String mAttributeName;

        private AbstractWithErroredAttribute( Validation pNext, String pAttributeName ) {
            super( pNext );
            mAttributeName = pAttributeName;
        }

        @Override
        public boolean hasErrorForAttribute( String pAttributeName ) {
            return (mAttributeName.equals( pAttributeName )) || super.hasErrorForAttribute( pAttributeName );
        }

        protected int addAttributeName( StringBuilder sb, int pIndent ) {
            indent( addNewLine( sb ), pIndent ).append( mAttributeName ).append( " - " );
            return mAttributeName.length() + 3;
        }
    }

    private static class WithTextErrors extends AbstractWithErroredAttribute {
        private final String mError;

        private WithTextErrors( Validation pNext, String pAttributeName, String pError ) {
            super( pNext, pAttributeName );
            this.mError = pError;
        }

        @Override
        protected void appendError( StringBuilder sb, int pIndent ) {
            addAttributeName( sb, pIndent );
            mNext.appendError( sb.append( mError ), pIndent );
        }
    }

    private static class WithValidationErrors extends AbstractWithErroredAttribute {
        private final Validation mValidationError;

        private WithValidationErrors( Validation pNext, String pAttributeName, Validation pValidationError ) {
            super( pNext, pAttributeName );
            mValidationError = pValidationError;
        }

        @Override
        protected void appendError( StringBuilder sb, int pIndent ) {
            int zChildIndent = pIndent + addAttributeName( sb, pIndent );

            mValidationError.appendError( sb, zChildIndent );

            mNext.appendError( sb, pIndent );
        }
    }

    private static class ErroredClass extends AbstractWithErrors {
        private final String mClassName;

        private ErroredClass( Validation pNext, String pClassName ) {
            super( pNext );
            mClassName = pClassName;
        }

        @Override
        protected void appendError( StringBuilder sb, int pIndent ) {
            sb.append( mClassName ).append( " errors:" );
            mNext.appendError( sb, pIndent + INDENT_DEPTH );
        }
    }
}
