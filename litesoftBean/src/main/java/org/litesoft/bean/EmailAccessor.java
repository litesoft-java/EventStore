package org.litesoft.bean;

import org.litesoft.alleviative.Characters;
import org.litesoft.alleviative.White;
import org.litesoft.alleviative.validation.NotNull;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.alleviative.validation.ValidationResult;

@SuppressWarnings("unused")
public interface EmailAccessor extends Accessor {
  String EXPECTED_EMAIL_FORM = "local-part@sub-domains.tld";
  Characters DOTS = new Characters( '.' );

  String getEmail();

  default boolean hasEmail() {
    return Significant.check( getEmail() );
  }

  static EmailAccessor deNull( EmailAccessor it ) {
    return (it != null) ? it : () -> null;
  }

  default ValidationResult<String> validateEmail() {
    return validateEmail( null );
  }

  default ValidationResult<String> validateEmail( String pExistingError ) {
    return validateUpdateEmail( getEmail(), pExistingError, null );
  }

  static ValidationResult<String> validateUpdateEmail( String pEmail, String pExistingError, String pPreexistingValue ) {
    pEmail = normalizeEmail( pEmail );
    ValidationResult.Builder<String> zBuilder = ValidationResult.withValues( Significant.orNull( pPreexistingValue ),
                                                                             Significant.orNull( pEmail ) );
    int zAt = pEmail.indexOf( '@' );

    if ( zAt < 1 ) {
      return zBuilder.error( pExistingError, notConformingError( 1 ) ); // must be at least ONE character in front of the '@'
    }

    if ( -1 != pEmail.indexOf( '@', zAt + 1 ) ) {
      return zBuilder.error( pExistingError, notConformingError( 2 ) ); // More than ONE '@'
    }

    int zLastDot = pEmail.lastIndexOf( '.' );

    if ( zLastDot < zAt ) {
      return zBuilder.error( pExistingError, notConformingError( 3 ) ); // must be at least ONE '.' in the domain
    }

    if ( (pEmail.length() - 2) <= zLastDot ) {
      return zBuilder.error( pExistingError, notConformingError( 4 ) ); // the tld must be at least 2 characters long
    }

    return zBuilder.ok( pExistingError );
  }

  static String normalizeEmail( String pEmail ) {
    pEmail = NotNull.or( pEmail, "" ).trim();
    int zAt = pEmail.indexOf( '@' );
    if ( zAt != -1 ) { // Normalize if there is an '@'
      pEmail = normalizeEmailLocalPart( pEmail.substring( 0, zAt ) ) +
               "@" + normalizeEmailDomain( pEmail.substring( zAt + 1 ) );
    }
    return pEmail;
  }

  static String normalizeEmailLocalPart( String pLocalPart ) {
    return White.Space.removeAllFrom( pLocalPart );
  }

  static String normalizeEmailDomain( String pDomain ) {
    pDomain = White.Space.removeAllFrom( pDomain );
    pDomain = DOTS.replaceLeadingTrailingAndAllInnerWithSingleFrom( pDomain );
    return pDomain.toLowerCase();
  }

  static String notConformingError( int pCode ) {
    return "Email Error (" + pCode + ") - does not conform to: " + EXPECTED_EMAIL_FORM;
  }
}
