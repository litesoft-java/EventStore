package org.litesoft.bean;

import org.litesoft.alleviative.Cast;
import org.litesoft.alleviative.validation.ValidationResult;

@SuppressWarnings("unused")
public interface NameMutator<T extends NameMutator<T>> extends NameAccessor {
  void setName( String pName );

  default T withName( String pName ) {
    setName( pName );
    return Cast.it( this );
  }

  default T withName( NameAccessor pAccessor ) {
    return withName( NameAccessor.deNull( pAccessor ).getName() );
  }

  default String validateUpdateName( String pExistingError ) {
    return validateUpdateName( pExistingError, null );
  }

  default String validateUpdateName( String pExistingError, String pPreexistingValue ) {
    ValidationResult<String> zResult = NameAccessor.validateUpdateName( getName(), pExistingError, pPreexistingValue );
    if ( zResult.hasUpdatedValue() ) {
      setName( zResult.getNewValue() );
    }
    return zResult.getError();
  }
}
