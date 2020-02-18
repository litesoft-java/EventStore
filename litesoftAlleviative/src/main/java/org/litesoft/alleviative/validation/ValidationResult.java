package org.litesoft.alleviative.validation;

import java.util.Objects;

@SuppressWarnings("unused")
public class ValidationResult<T> {
    private final String mError;
    private final T mExistingValue, mNewValue, mValue;
    private final boolean mValueUpdated;

    public T getExistingValue() {
        return mExistingValue;
    }

    public T getNewValue() {
        return mNewValue;
    }

    public boolean hasUpdatedValue() {
        return mValueUpdated;
    }

    public T getValue() {
        return mValue;
    }

    public boolean hasError() {
        return (mError != null);
    }

    public String getError() {
        return mError;
    }

    private ValidationResult( String pExistingError, // Existing Error unrelated to Existing Value!
                              T pExistingValue, T pNewValue, String pNewError ) {
        mError = Significant.or( pExistingError, pNewError );
        mExistingValue = pExistingValue;
        mNewValue = pNewValue;
        if ( Objects.equals( pExistingValue, pNewValue ) ) {
            mValue = pNewValue;
            mValueUpdated = false;
        } else if ( pNewError == null ) {
            mValue = pNewValue;
            mValueUpdated = true;
        } else { // Is NEW Error
            mValue = pExistingValue;
            mValueUpdated = false;
        }
    }

    public static <T> ValidationResult.Builder<T> withValue( T pNormalizedNewValue ) {
        return withValues( null, pNormalizedNewValue );
    }

    public static <T> ValidationResult.Builder<T> withValues( T pExistingValue, T pNormalizedNewValue ) {
        return new ValidationResult.Builder<>( pExistingValue, pNormalizedNewValue );
    }

    public static class Builder<T> {
        private final T mExistingValue, mNewValue;

        private Builder( T pExistingValue, T pNormalizedNewValue ) {
            mExistingValue = pExistingValue;
            mNewValue = pNormalizedNewValue;
        }

        public ValidationResult<T> ok() {
            return ok( null );
        }

        public ValidationResult<T> ok( String pExistingError ) {
            return error( pExistingError, null );
        }

        /**
         * Return a ValidationResult with an Error IF it is significant!
         *
         * @param pNewError Nullable
         */
        public ValidationResult<T> error( String pNewError ) {
            return error( null, pNewError );
        }

        /**
         * Return a ValidationResult with an Error IF it is significant!
         *
         * @param pExistingError Nullable
         * @param pNewError      Nullable
         */
        public ValidationResult<T> error( String pExistingError, String pNewError ) {
            return new ValidationResult<>( Significant.orNull( pExistingError ),
                                           mExistingValue, mNewValue, Significant.orNull( pNewError ) );
        }
    }
}
