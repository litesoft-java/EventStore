package org.litesoft.bean;

import org.litesoft.alleviative.Cast;
import org.litesoft.alleviative.validation.ValidationResult;

@SuppressWarnings("unused")
public interface EmailMutator<T extends EmailMutator<T>> extends EmailAccessor {
    void setEmail( String pEmail );

    default T withEmail( String pEmail ) {
        setEmail( pEmail );
        return Cast.it( this );
    }

    default T withEmail( EmailAccessor pAccessor ) {
        return withEmail( EmailAccessor.deNull( pAccessor ).getEmail() );
    }

    default String validateUpdateEmail( String pExistingError ) {
        return validateUpdateEmail( pExistingError, null );
    }

    default String validateUpdateEmail( String pExistingError, String pPreexistingValue ) {
        ValidationResult<String> zResult = EmailAccessor.validateUpdateEmail( getEmail(), pExistingError, pPreexistingValue );
        if ( zResult.hasUpdatedValue() ) {
            setEmail( zResult.getNewValue() );
        }
        return zResult.getError();
    }
}
