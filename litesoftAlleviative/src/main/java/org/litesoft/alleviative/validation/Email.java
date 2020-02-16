package org.litesoft.alleviative.validation;

/**
 * Email Validator.
 * Rules from the following:
 * <li>http://rumkin.com/software/email/rules.php</li>
 * <li>https://help.returnpath.com/hc/en-us/articles/220560587-What-are-the-rules-for-email-address-syntax-</li>
 * <li>https://en.wikipedia.org/wiki/Email_address</li>
 * <p>
 * From the above, in addition to the Constants in the code, here are some additional rules enforced in the code:
 * <ul>for both sections:
 * <li>spaces are not accepted!</li>
 * <li>adjacent dots are not accepted!</li>
 * <li>non-Ascii currently not accepted</li>
 * </ul>
 * <ul>for Local Part section:
 * <li>Special characters may not start or end a Label (dot separated)</li>
 * </ul>
 * <ul>for Domain section:
 * <li>Labels must not start with a hyphen, end with a hyphen, or contain two successive hyphens.</li>
 * <li>The right-most label must be all alphabetic.</li>
 * <li>Special characters may not start or end a Label (dot separated)</li>
 * </ul>
 * Note: We are not supporting Quoted Local Parts or Bracketed Domains (or IP addresses)!
 */
public class Email {
  public static final String SPECIAL_CHARS_LOCAL_PART = "!#$%&'*+-/=?^_`{|}~";
  public static final int MIN_LABELS_LOCAL_PART = 1;
  public static final int MAX_LENGTH_LOCAL_PART = 64;

  public static final String SPECIAL_CHARACTERS_DOMAIN = "-";
  public static final int MIN_LABELS_DOMAIN = 2;
  public static final int MAX_LENGTH_DOMAIN = 253;
  public static final int MAX_LENGTH_DOMAIN_LABEL = 63;

  private static final char DOT = '.';
  private static final String TWO_DOTS = "..";

  public static ValidationResult<String> validateOptional( String pEmail ) {
    String zEmail = Significant.orNull( pEmail );
    return (zEmail != null) ? validate( zEmail ) :
           builder( pEmail, null ).ok();
  }

  public static ValidationResult<String> validateRequired( String pEmail ) {
    String zEmail = Significant.orNull( pEmail );
    return (zEmail != null) ? validate( zEmail ) :
           error( builder( pEmail, null ), "is Required" );
  }

  private static ValidationResult<String> validate( String pEmail ) {
    int zAt = pEmail.indexOf( '@' );
    return (zAt != -1) ? new Email( pEmail, zAt ).validate() :
           error( builder( pEmail, null ), "no '@'", pEmail );
  }

  private static ValidationResult.Builder<String> builder( String pOrigEmail, String pNormalizedEmail ) {
    return ValidationResult.withValues( pOrigEmail, pNormalizedEmail );
  }

  private static ValidationResult<String> error( ValidationResult.Builder<String> pBuilder, String pError, String pInEmail ) {
    return error( pBuilder, pError + " in: " + pInEmail );
  }

  private static ValidationResult<String> error( ValidationResult.Builder<String> pBuilder, String pError ) {
    return pBuilder.error( pError );
  }

  private final String mOrig, mLocalPart, mDomain;

  private Email( String pEmail, int pAt ) {
    mOrig = pEmail;
    String zEmail = pEmail.toLowerCase();
    mLocalPart = zEmail.substring( 0, pAt ).trim();
    mDomain = zEmail.substring( pAt + 1 ).trim();
  }

  private ValidationResult.Builder<String> builder() {
    return builder( mOrig, mLocalPart + "@" + mDomain );
  }

  private ValidationResult<String> error( String pError ) {
    return error( builder(), pError, mOrig );
  }

  private ValidationResult<String> validate() {
    if ( mLocalPart.isEmpty() ) {
      return error( "nothing before the '@'" );
    }
    if ( mDomain.isEmpty() ) {
      return error( "nothing after the '@'" );
    }
    String zError = validateSection( "Local Part", mLocalPart, SPECIAL_CHARS_LOCAL_PART, MIN_LABELS_LOCAL_PART,
                                     MAX_LENGTH_LOCAL_PART, MAX_LENGTH_LOCAL_PART, this::validateLabel );
    if ( zError != null ) {
      return error( zError );
    }
    zError = validateSection( "Domain", mDomain, SPECIAL_CHARACTERS_DOMAIN, MIN_LABELS_DOMAIN,
                              MAX_LENGTH_DOMAIN, MAX_LENGTH_DOMAIN_LABEL, this::validateLastDomainLabel );
    if ( zError != null ) {
      return error( zError );
    }
    return builder().ok();
  }

  private String validateSection( String pWhatSection, String pSection, String pSpecialCharacters, int pMinLabels,
                                  int pMaxSectionLength, int pMaxLabelLength, LastLabelProcessor pLastLabelProcessor ) {
    if ( pSection.length() > pMaxSectionLength ) {
      return pWhatSection + " too long (max length: " + pMaxSectionLength + ")";
    }
    if ( pSection.indexOf( ' ' ) != -1 ) {
      return pWhatSection + " contains space(s)";
    }
    if ( pSection.contains( TWO_DOTS ) ) {
      return pWhatSection + " contains two dots (..)";
    }
    if ( pSection.charAt( 0 ) == DOT ) {
      return pWhatSection + " starts with a dot (.)";
    }
    if ( pSection.charAt( pSection.length() - 1 ) == DOT ) {
      return pWhatSection + " ends with a dot (.)";
    }
    String zError = null;
    int zLabels = 0;
    String zLastLabel = pSection;
    String zLabelLabel = "";
    int zDotAt = zLastLabel.indexOf( DOT );
    if ( zDotAt != -1 ) {
      int zLabelID = 0;
      do {
        zLabelLabel = "Label-" + zLabelID + " ";
        String zLabel = zLastLabel.substring( 0, zDotAt );
        zLastLabel = zLastLabel.substring( zDotAt + 1 );
        zError = validateLabel( zLabel, zLabelLabel, pSpecialCharacters, pMaxLabelLength );
        zLabels++;
        zDotAt = zLastLabel.indexOf( DOT );
      } while ( (zDotAt != -1) && (zError == null) );
      zLabelLabel = "Last Label ";
    }
    if ( zError == null ) {
      zError = pLastLabelProcessor.process( zLastLabel, zLabelLabel, pSpecialCharacters, pMaxLabelLength );
      zLabels++;
      if ( zError == null ) {
        if ( pMinLabels <= zLabels ) {
          return null;
        }
        zError = "too few Labels (min: " + pMinLabels + ")";
      }
    }
    return pWhatSection + " " + zError;
  }

  private String validateLabel( String pLabel, String pLabelLabel, String pSpecialCharacters, int pMaxLabelLength ) {
    return validateLabelCommon( pLabel, pLabelLabel, pSpecialCharacters, pMaxLabelLength, true );
  }

  private String validateLastDomainLabel( String pLabel, String pLabelLabel, String pSpecialCharacters, int pMaxLabelLength ) {
    return validateLabelCommon( pLabel, pLabelLabel, pSpecialCharacters, pMaxLabelLength, false );
  }

  private String validateLabelCommon( String pLabel, String pLabelLabel, String pSpecialCharacters, int pMaxLabelLength, boolean pAllowAllNumeric ) {
    if ( pLabel.length() > pMaxLabelLength ) {
      return pLabelLabel + "too long (max length: " + pMaxLabelLength + ")";
    }
    CharGroup zCG = checkLabelChar( pLabel.charAt( pLabel.length() - 1 ), pSpecialCharacters );
    if ( zCG == CharGroup.Special ) {
      return pLabelLabel + "last character must only be Alpha-Numeric";
    }
    int zAt = 0;
    char c = pLabel.charAt( zAt );
    zCG = checkLabelChar( c, pSpecialCharacters );
    if ( (zCG == CharGroup.Special) || (zCG == CharGroup.Bad) ) {
      return pLabelLabel + "first character must only be Alpha-Numeric";
    }
    boolean zAllDigits = (zCG == CharGroup.Digit);
    char zLastchar = c;
    while ( ++zAt < pLabel.length() ) {
      c = pLabel.charAt( zAt );
      zCG = checkLabelChar( c, pSpecialCharacters );
      zAllDigits &= (zCG == CharGroup.Digit);
      if ( zCG == CharGroup.Bad ) {
        return pLabelLabel + "character (at offset: " + zAt + ") is unacceptable";
      }
      if ( (zCG == CharGroup.Special) && (zLastchar == c) ) {
        return pLabelLabel + "adjacent 'special' characters (at offset: " + zAt + ") is unacceptable";
      }
      zLastchar = c;
    }
    if ( !zAllDigits ) {
      return null;
    }
    return pAllowAllNumeric ? null : (pLabelLabel + "may NOT be all Digits");
  }

  private CharGroup checkLabelChar( char pChar, String pSpecialCharacters ) {
    if ( Character.isLetter( pChar ) ) {
      return CharGroup.Letter;
    }
    if ( Character.isDigit( pChar ) ) {
      return CharGroup.Digit;
    }
    if ( pSpecialCharacters.indexOf( pChar ) != -1 ) {
      return CharGroup.Special;
    }
    return CharGroup.Bad;
  }

  private interface LastLabelProcessor {
    String process( String pLabel, String pLabelLabel, String pSpecialCharacters, int pMaxLabelLength );
  }

  enum CharGroup {
    Letter, Digit, Special, Bad
  }
}
