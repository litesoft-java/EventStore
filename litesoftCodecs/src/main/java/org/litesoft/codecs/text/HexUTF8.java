package org.litesoft.codecs.text;

@SuppressWarnings("WeakerAccess")
public abstract class HexUTF8 extends StringCodecs {
  private static final String SHORT_NAME = "HU8";

  protected static final char CHAR_FOR_NULL = '!';

  /**
   * @param pVersion Positive!
   */
  protected HexUTF8( boolean pRegister, int pVersion ) {
    super( pRegister, SHORT_NAME, pVersion );
  }

  protected boolean validArrayEntryPrefixChar( char pPossibleArrayEntryPrefixChar ) {
    return is7BitButNot_Control_Alpha_Numeric( pPossibleArrayEntryPrefixChar )
           && (pPossibleArrayEntryPrefixChar != CHAR_FOR_NULL);
  }

  /**
   * Merge the Hi & Low Nibbles (either of which could be -1)
   *
   * @param pHiNibble  -1 if not a Nibble, otherwise the bottom 4 bits are the Nibble
   * @param pLowNibble -1 if not a Nibble, otherwise the bottom 4 bits are the Nibble
   *
   * @return -1 or the low 8 bits are the 'byte'
   */
  protected static int mergeNibbles( int pHiNibble, int pLowNibble ) {
    return (255 & (((pHiNibble & 15) << 4) | (pLowNibble & 15)));
  }

  protected static void appendHex( StringBuilder pSB, byte pByte ) {
    pSB
            .append( toHexChar( pByte >> 4 ) )
            .append( toHexChar( pByte ) );
  }

  private static final int CAP_A_MINUS_10 = 'A' - 10;
  private static final int LOW_A_MINUS_10 = 'a' - 10;

  protected static int fromHex( int pValue ) {
    if ( inRangeInclusive( pValue, '0', '9' ) ) {
      return pValue - '0';
    }
    if ( inRangeInclusive( pValue, 'A', 'F' ) ) {
      return pValue - CAP_A_MINUS_10;
    }
    if ( inRangeInclusive( pValue, 'a', 'f' ) ) {
      return pValue - LOW_A_MINUS_10;
    }
    return -1; //
  }

  protected static char toHexChar( int pValue ) {
    pValue &= 15;
    return (char)((pValue < 10) ? ('0' + pValue) : (CAP_A_MINUS_10 + pValue));
  }
}
