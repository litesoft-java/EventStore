package org.litesoft.codecs.numeric;

@SuppressWarnings("WeakerAccess")
public abstract class Base32A extends IntegerCodecs {
  private static final String SHORT_NAME = "B32A";

  protected static final int NON_ABS_ABLE_NEGATIVE_VALUE = Integer.MIN_VALUE;

  protected static final char CHAR_FOR_NON_ABS_ABLE_NEGATIVE_VALUE = '_';

  protected static final char CHAR_FOR_NULL = '!';

  /**
   * @param pVersion Positive!
   */
  protected Base32A( boolean pRegister, int pVersion ) {
    super( pRegister, SHORT_NAME, pVersion );
  }

  protected boolean validArrayEntryPrefixChar( char pPossibleArrayEntryPrefixChar ) {
    return is7BitButNot_Control_Alpha_Numeric( pPossibleArrayEntryPrefixChar )
           && (pPossibleArrayEntryPrefixChar != CHAR_FOR_NON_ABS_ABLE_NEGATIVE_VALUE)
           && (pPossibleArrayEntryPrefixChar != CHAR_FOR_NULL)
           && (pPossibleArrayEntryPrefixChar != '-'); // And Minus sign!
  }

  protected static void appendBase32( StringBuilder pSB, byte pByte ) {
    pSB
            .append( toBase32Char( pByte ) );
  }

  private static final int FIRST_CAP_LETTER = 'A';
  private static final int LAST_CAP_LETTER = FIRST_CAP_LETTER + (31 - 10);
  private static final int FIRST_LOW_LETTER = 'a';
  private static final int LAST_LOW_LETTER = FIRST_LOW_LETTER + (31 - 10);

  private static final int CAP_A_MINUS_10 = FIRST_CAP_LETTER - 10;
  private static final int LOW_A_MINUS_10 = FIRST_LOW_LETTER - 10;

  protected static int fromBase32Char( int pValue ) {
    if ( inRangeInclusive( pValue, '0', '9' ) ) {
      return pValue - '0';
    }
    if ( inRangeInclusive( pValue, FIRST_CAP_LETTER, LAST_CAP_LETTER ) ) {
      return pValue - CAP_A_MINUS_10;
    }
    if ( inRangeInclusive( pValue, FIRST_LOW_LETTER, LAST_LOW_LETTER ) ) {
      return pValue - LOW_A_MINUS_10;
    }
    return -1; //
  }

  protected static char toBase32Char( int pValue ) {
    pValue &= 31;
    return (char)((pValue < 10) ? ('0' + pValue) : (CAP_A_MINUS_10 + pValue));
  }
}
