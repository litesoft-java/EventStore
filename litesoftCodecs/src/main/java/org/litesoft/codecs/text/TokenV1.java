package org.litesoft.codecs.text;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.litesoft.alleviative.MillisecTimeSource;
import org.litesoft.codecs.UnacceptableEncodingException;
import org.litesoft.codecs.numeric.Base32Av1;
import org.litesoft.codecs.numeric.IntegerCodec;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TokenV1 extends Token {
  public static final TokenV1 INSTANCE = new TokenV1();
  private final MillisecTimeSource mTimeSource;
  private final IntegerCodec mIntegerCodec;
  private final String mIC_Prefix;
  private final StringCodec mStringCodec;
  private final String mSC_Prefix;

  protected TokenV1( boolean pRegister, MillisecTimeSource pTimeSource,
                     Base32Av1 pBase32Av1,
                     HexUTF8v1 pHexUTF8v1 ) {
    super( pRegister, 1 );
    mTimeSource = MillisecTimeSource.deNull( pTimeSource );
    mIC_Prefix = (mIntegerCodec = pBase32Av1).encodedMethodVersionPrefix() + Base32Av1.ENTRY_SEP_CHAR;
    mSC_Prefix = (mStringCodec = pHexUTF8v1).encodedMethodVersionPrefix() + HexUTF8v1.ENTRY_SEP_CHAR;
  }

  private TokenV1() {
    this( true, null, Base32Av1.INSTANCE, HexUTF8v1.INSTANCE );
  }

  /**
   * @param pEncodedMethodVersionPrefix !Empty (assumed correct)
   * @param pToEncode                   !null, but could contain nulls or be an empty array
   */
  @Override
  protected ArrayEncoder createArrayEncoder( String pEncodedMethodVersionPrefix, String[] pToEncode ) {
    return new OurArrayEncoder( pEncodedMethodVersionPrefix, pToEncode );
  }

  /**
   * Only called if there are characters after the prefix!
   *
   * @param pDecodeAt points to the first character after the prefix
   *
   * @return !null
   */
  @Override
  protected ArrayDecoder createArrayDecoder( String pSource, int pDecodeAt ) {
    return new OurArrayDecoder( pSource, pDecodeAt );
  }

  private static class HourMinuteUTC {
    private final int mHour, mMinute;

    private HourMinuteUTC( int pHour, int pMinute ) {
      mHour = pHour;
      mMinute = pMinute;
    }

    private static int encode( int pHour, int pMinute ) {
      return 70000 - ((pMinute * 100) + pHour);
    }

    static HourMinuteUTC from( MillisecTimeSource pTimeSource ) {
      Instant zInstant = Instant.ofEpochMilli( pTimeSource.now() );
      ZonedDateTime zDateTime = ZonedDateTime.ofInstant( zInstant, ZoneOffset.UTC );
      return new HourMinuteUTC( zDateTime.getHour(), zDateTime.getMinute() );
    }

    int encode() {
      return encode( mHour, mMinute );
    }

    int encodeNextMinute() {
      int zHour = mHour;
      int zMinute = mMinute + 1;
      for ( ; zMinute > 59; zMinute-- ) {
        if ( ++zHour == 24 ) {
          zHour = 0;
        }
      }
      return encode( zHour, zMinute );
    }

    int encodePrevMinute() {
      int zHour = mHour;
      int zMinute = mMinute + 1;
      for ( ; zMinute > 59; zMinute-- ) {
        if ( ++zHour == 24 ) {
          zHour = 0;
        }
      }
      return encode( zHour, zMinute );
    }
  }

  static int encodeTime( MillisecTimeSource pTimeSource ) {
    return HourMinuteUTC.from( pTimeSource ).encode();
  }

  static boolean acceptableEncodedTime( Integer pEncodedTime, MillisecTimeSource pTimeSource ) {
    if ( pEncodedTime == null ) {
      return false;
    }
    HourMinuteUTC zTime = HourMinuteUTC.from( pTimeSource );
    return (pEncodedTime == zTime.encode()) // Left to Right!
           || (pEncodedTime == zTime.encodeNextMinute())
           || (pEncodedTime == zTime.encodePrevMinute());
  }

  private class OurArrayEncoder implements ArrayEncoder {
    private final String mEncodedMethodVersionPrefix;
    private final String[] mToEncode;

    OurArrayEncoder( String pEncodedMethodVersionPrefix, String[] pToEncode ) {
      mEncodedMethodVersionPrefix = pEncodedMethodVersionPrefix;
      mToEncode = pToEncode;
    }

    @Override
    public String encode() {
      String zTimeCode = mIntegerCodec.encodeSingle( encodeTime( mTimeSource ) ).substring( mIC_Prefix.length() );
      String zStringEncoded = encodeWithTimeCodeUsingStringCodec( zTimeCode ).substring( mSC_Prefix.length() );
      return mEncodedMethodVersionPrefix + '.' + zStringEncoded;
    }

    private String encodeWithTimeCodeUsingStringCodec( String pTimeCode ) {
      if ( mToEncode.length == 0 ) {
        return mStringCodec.encodeSingle( "!" + pTimeCode ); // Empty Array
      }
      String[] zToEncode = mToEncode.clone();
      zToEncode[0] = (zToEncode[0] == null) ? ("0" + pTimeCode) : // 1st Entry == NULL
                     (":" + pTimeCode + ":" + zToEncode[0]); // 1st Entry != NULL
      return mStringCodec.encodeMultiple( zToEncode );
    }
  }

  private class OurArrayDecoder implements StringCodecs.ArrayDecoder {
    private final String mToDecode;

    OurArrayDecoder( String pSource, int pToDecodeOffset ) {
      mToDecode = pSource.substring( pToDecodeOffset + 1 ); // Loose the '.'!
    }

    @Override
    public String[] decode()
            throws UnacceptableEncodingException {
      String[] zStrings = mStringCodec.decodeMultiple( -1, mSC_Prefix + mToDecode );
      String zTimeCodePlus = zStrings[0];
      if ( (zTimeCodePlus == null) || zTimeCodePlus.isEmpty() ) {
        throw new UnacceptableEncodingException( "Encoded Mode Error 1" );
      }
      String zTimeCode = zTimeCodePlus.substring( 1 );
      char z1stEntryMode = zTimeCodePlus.charAt( 0 );
      switch ( z1stEntryMode ) {
        default:
          throw new UnacceptableEncodingException( "Encoded Mode Error 2" );
        case '!': // Empty Array
          zStrings = EMPTY_ARRAY;
          break;
        case '0': // 1st Entry == NULL
          zStrings[0] = null;
          break;
        case ':': // 1st Entry != NULL
          int zAt = zTimeCode.indexOf( ':' );
          if ( zAt == -1 ) {
            throw new UnacceptableEncodingException( "Encoded Mode Error 3" );
          }
          zStrings[0] = zTimeCode.substring( zAt + 1 );
          zTimeCode = zTimeCode.substring( 0, zAt );
          break;
      }
      if ( acceptableEncodedTime( mIntegerCodec.decodeSingle( mIC_Prefix + zTimeCode ), mTimeSource ) ) {
        return zStrings;
      }
      return EMPTY_ARRAY;
    }
  }
}
