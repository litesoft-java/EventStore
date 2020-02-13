package org.litesoft.bean;

import org.litesoft.alleviative.White;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.alleviative.validation.ValidationResult;

@SuppressWarnings("unused")
public interface NameAccessor extends Accessor {
  String getName();

  default boolean hasName() {
    return Significant.check( getName() );
  }

  static NameAccessor deNull( NameAccessor it ) {
    return (it != null) ? it : () -> null;
  }

  default ValidationResult<String> validateName() {
    return validateName( null );
  }

  default ValidationResult<String> validateName( String pExistingError ) {
    return validateUpdateName( getName(), pExistingError, null );
  }

  static ValidationResult<String> validateUpdateName( String pName, String pExistingError, String pPreexistingValue ) {
    pName = normalizeName( pName );
    ValidationResult.Builder<String> zBuilder = ValidationResult.withValues( Significant.orNull( pPreexistingValue ),
                                                                             Significant.orNull( pName ) );
    if ( pName.length() < 2 ) {
      return zBuilder.error( pExistingError, "Name '" + pName + "' must be at least 2 characters" );
    }

    return zBuilder.ok( pExistingError );
  }

  static String normalizeName( String pName ) {
    return White.Space.replaceLeadingTrailingAndAllInnerWithSingleFrom( pName );
  }
}
