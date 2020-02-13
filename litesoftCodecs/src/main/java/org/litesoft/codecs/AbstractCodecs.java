package org.litesoft.codecs;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings({"unused", "WeakerAccess", "UnnecessaryLocalVariable"})
public abstract class AbstractCodecs<CodecType extends Codec, Codecs extends AbstractCodecs<CodecType, Codecs>> implements Codec {
  protected static final Charset UTF8 = Charset.forName( "UTF8" );

  private final String mShortName;
  private final int mVersion;
  private final String mEncodedMethodVersionPrefix;

  /**
   * @param pShortName must start with a 7 Bit Ascii Letter, followed by a few 7 Bit Ascii Letters or Digits!
   * @param pVersion   Positive!
   */
  protected AbstractCodecs( Class<CodecType> pCodecTypeClass,
                            boolean pRegister, String pShortName, int pVersion,
                            Map<String, Map<Integer, CodecType>> pInstancesByVersionByShortName ) {
    mShortName = pShortName;
    mVersion = pVersion;
    mEncodedMethodVersionPrefix = mShortName + SHORT_NAME_VERSION_SEP + mVersion;
    if ( pRegister ) {
      Map<Integer, CodecType> zVersionMap = pInstancesByVersionByShortName.computeIfAbsent( mShortName, pS -> new HashMap<>() );
      CodecType zPreviousInstance = zVersionMap.put( pVersion, cast( pCodecTypeClass, this ) ); // Mild 'this' leakage (due to Constructor synchronization)!
      if ( zPreviousInstance != null ) {
        throw new IllegalStateException( "Double Registration of " + pCodecTypeClass.getSimpleName() +
                                         " with: " + mShortName + SHORT_NAME_VERSION_SEP + pVersion );
      }
    }
  }

  private static <CodecType extends Codec> CodecType cast( Class<CodecType> pCodecTypeClass, AbstractCodecs<CodecType, ?> pToCast ) {
    Class<? extends AbstractCodecs> zSourceClass = pToCast.getClass();
    if ( pCodecTypeClass.isAssignableFrom( zSourceClass ) ) {
      return Cast.it( pToCast );
    }
    throw new IllegalStateException( zSourceClass.getSimpleName() + " does NOT extend " + pCodecTypeClass.getSimpleName() );
  }

  /**
   * Get a registered Codec or throw UnacceptableEncodingException
   *
   * @param pToDecode to extract <code>Encoded Method & Version</code>
   *
   * @return the appropriate Codec
   *
   * @throws UnacceptableEncodingException if for any reason no codex could be found
   */
  protected static <CodecType extends Codec> CodecType findCodec( String pToDecode,
                                                                  Map<String, Map<Integer, CodecType>> pInstancesByVersionByShortName )
          throws UnacceptableEncodingException {
    if ( pToDecode == null ) {
      throw new UnacceptableEncodingException( "Decode String was null" );
    }
    int zAt = pToDecode.indexOf( SHORT_NAME_VERSION_SEP );
    String zShortName = (zAt == -1) ? "" : pToDecode.substring( 0, zAt++ ); // Move pointer to just beyond Sep
    if ( zShortName.isEmpty() ) {
      throw new UnacceptableEncodingException( "No Method/ShortName in Decode String: " + pToDecode );
    }
    Integer zVersion = parseVersionAt( pToDecode, zAt );
    if ( zVersion == null ) {
      throw new UnacceptableEncodingException( "No Version after '" + zShortName + SHORT_NAME_VERSION_SEP + "' in Decode String: " + pToDecode );
    }
    Map<Integer, CodecType> zCodecMap = pInstancesByVersionByShortName.get( zShortName );
    if ( zCodecMap == null ) {
      throw new UnacceptableEncodingException( "No Codec(s) registered for '" + zShortName + "' in Decode String: " + pToDecode );
    }
    CodecType zCodec = zCodecMap.get( zVersion );
    if ( zCodec == null ) {
      throw new UnacceptableEncodingException( "No Codec registered version " +
                                               " for '" + zShortName + "' in Decode String: " + pToDecode );
    }
    return zCodec;
  }

  /**
   * @return Decode Offset or -1 if there are no more characters
   */
  protected int getPostPrefixOffset( int pFieldsExpected, String pToDecode ) {
    if ( pToDecode == null ) {
      throw new UnacceptableEncodingException( createExpected( pFieldsExpected ) + "Decode String was null" );
    }
    String zPrefix = encodedMethodVersionPrefix();
    if ( !pToDecode.startsWith( zPrefix ) ) {
      throw new UnacceptableEncodingException( "Decode String did not start with '" + zPrefix + "', but was: " + pToDecode );
    }
    int zPrefixLength = zPrefix.length();
    return (pToDecode.length() == zPrefixLength) ? -1 : zPrefixLength;
  }

  @Override
  public String encodedMethodVersionPrefix() {
    return mEncodedMethodVersionPrefix;
  }

  /**
   * A Short Name which must start with a 7 Bit Ascii Letter, followed by a few 7 Bit Ascii Letters or Digits!
   *
   * @return !Null
   */
  @Override
  public final String shortName() {
    return mShortName;
  }

  /**
   * Version Number - Positive!
   *
   * @return Positive integer
   */
  @Override
  public final int version() {
    return mVersion;
  }

  protected boolean is7BitButNot_Control_Alpha_Numeric( char pChar ) {
    return inRangeExclusive( pChar, ' ', 127 )
           && !inRangeInclusive( pChar, '0', '9' )
           && !inRangeInclusive( pChar, 'A', 'Z' )
           && !inRangeInclusive( pChar, 'a', 'z' );
  }

  /**
   * @param pFieldsExpected 0 means No minimum, positive number means need the exact number, negative number means at least abs(<coded>pFieldsExpected</coded>).
   */
  protected void checkExpected( int pFieldsExpected, int pFieldsActual, String pToDecode ) {
    if ( !((pFieldsExpected == 0) // Don't care
           || (pFieldsExpected == pFieldsActual) // Positive and equal == Contract for Positive
           || ((pFieldsExpected < 0) && (-pFieldsExpected <= pFieldsActual))) ) { // Negative and Actual is at least as many as ABS(Expected)
      throw new UnacceptableEncodingException( createExpected( pFieldsExpected ) + "got " + pFieldsActual + " with Decode String: " + pToDecode );
    }
  }

  /**
   * @param pFieldsExpected 0 means No minimum, positive number means need the exact number, negative number means at least abs(<coded>pFieldsExpected</coded>).
   */
  protected String createExpected( int pFieldsExpected ) {
    if ( pFieldsExpected == 0 ) {
      return "";
    }
    String zCollector = "Expected ";
    if ( pFieldsExpected < 0 ) {
      zCollector += "at least ";
      pFieldsExpected = -pFieldsExpected;
    }
    return zCollector + pFieldsExpected + " fields, but ";
  }

  protected interface ArrayEncoder {
    /**
     * Not reusable - Single call only!
     *
     * @return !null
     */
    String encode();
  }

  protected static Integer parseVersionAt( String pToDecode, int pAt ) {
    if ( pToDecode.length() <= pAt ) {
      return null;
    }
    char z1stChar = pToDecode.charAt( pAt++ );
    if ( (z1stChar < '1') || ('9' < z1stChar) ) {
      return null;
    }
    int rv = z1stChar - '0';
    while ( pAt < pToDecode.length() ) {
      char zNextChar = pToDecode.charAt( pAt++ );
      if ( (zNextChar < '0') || ('9' < zNextChar) ) {
        return rv;
      }
      rv = (rv * 10) + (zNextChar - '0');
    }
    return rv;
  }

  protected static void singleUse( AtomicBoolean pUsed, String pWhat ) {
    boolean zAlreadyUsed = !pUsed.compareAndSet( false, true );
    if ( zAlreadyUsed ) {
      throw new IllegalStateException( pWhat + " has already been used and the specification is that it is a single use object" );
    }
  }

  protected static boolean isEven( int pInt ) {
    return (pInt & 1) == 0;
  }

  @SuppressWarnings("SameParameterValue")
  protected static boolean inRangeExclusive( int pToCheck, int pRangeStart, int pRangeEnd ) {
    return (pRangeStart < pToCheck) && (pToCheck < pRangeEnd);
  }

  protected static boolean inRangeInclusive( int pToCheck, int pRangeStart, int pRangeEnd ) {
    return (pRangeStart <= pToCheck) && (pToCheck <= pRangeEnd);
  }

  protected interface ArrayEntryPrefixValidator {
    boolean test( char pChar );
  }

  protected interface Entry {
    /**
     * @return 0-n where 0 indicates the first entry!
     */
    int getEntryOffset();

    /**
     * @return Original character length of the Sting of this Entry
     */
    int length();

    /**
     * @return Current remaining (after what ever has been read) character length of the Sting of this Entry
     */
    int available();

    /**
     * @return Original character length of the Sting of this Entry
     */
    default boolean isEmpty() {
      return length() == 0;
    }

    /**
     * @return true if the entry is a single character and it is equal to <coed>pCheckForEquals</coed>
     */
    boolean is( char pCheckForEquals );

    /**
     * return the next character without consuming it
     *
     * @return next char or -1 if no more available
     */
    int peekChar();

    /**
     * Reads (and consumes) the next character
     *
     * @return next char or -1 if no more available
     */
    int readChar();

    UnacceptableEncodingException error( Integer pReadOffsetAdjust, String pError );
  }

  protected static class EntryProvider {
    private final String mSource;
    private final char mEntryPrefixChar;
    private int mNextEntryReadOffset;
    private int mReadOffset = -1;
    private int mEntryOffset = -1; // 0 indicating the first Entry

    public EntryProvider( String pSource, int pReadOffset, ArrayEntryPrefixValidator pPrefixValidator ) {
      mSource = pSource;
      mEntryPrefixChar = pSource.charAt( pReadOffset );
      if ( !pPrefixValidator.test( mEntryPrefixChar ) ) {
        throw new UnacceptableEncodingException( "Unacceptable Array Entry Prefix Character ('" + mEntryPrefixChar + "') at (offset) " + pReadOffset +
                                                 " in Decode String: " + pSource );
      }
      mNextEntryReadOffset = pReadOffset + 1; // just past the Array Entry Prefix Character
    }

    public Entry next() {
      if ( mNextEntryReadOffset == -1 ) {
        return null;
      }
      mEntryOffset++;
      mReadOffset = mNextEntryReadOffset;
      int zEntryLength;
      int zNextEntryPrefixAt = mSource.indexOf( mEntryPrefixChar, mReadOffset );
      if ( zNextEntryPrefixAt == -1 ) {
        mNextEntryReadOffset = -1;
        zEntryLength = mSource.length() - mReadOffset;
      } else {
        mNextEntryReadOffset = zNextEntryPrefixAt + 1;
        zEntryLength = zNextEntryPrefixAt - mReadOffset;
      }
      return new EntryImpl( mReadOffset, zEntryLength );
    }

    private char nextChar() {
      return mSource.charAt( mReadOffset++ );
    }

    private char peekNextChar() {
      return mSource.charAt( mReadOffset );
    }

    private class EntryImpl implements Entry {
      private final int mInitialReadOffset;
      private final int mLength;
      private int mAvailable;

      private EntryImpl( int pInitialReadOffset, int pLength ) {
        mInitialReadOffset = pInitialReadOffset;
        mAvailable = mLength = pLength;
      }

      @Override
      public int getEntryOffset() {
        return mEntryOffset;
      }

      @Override
      public int length() {
        return mLength;
      }

      @Override
      public int available() {
        return mAvailable;
      }

      @Override
      public boolean is( char pCheckForEquals ) {
        return (mLength == 1) && (pCheckForEquals == mSource.charAt( mInitialReadOffset )); // Left to Right!
      }

      @Override
      public int peekChar() {
        return (mAvailable <= 0) ? -1 : peekNextChar();
      }

      @Override
      public int readChar() {
        if ( mAvailable <= 0 ) {
          return -1;
        }
        mAvailable--;
        return nextChar();
      }

      @Override
      public UnacceptableEncodingException error( Integer pReadOffsetAdjust, String pError ) {
        StringBuilder sb = new StringBuilder()
                .append( "Field[" ).append( mEntryOffset ).append( "] offset " );
        int zAt = mReadOffset - mInitialReadOffset;
        if ( pReadOffsetAdjust != null ) {
          zAt += pReadOffsetAdjust;
          if ( zAt < 0 ) {
            zAt = 0;
          }
        }
        sb.append( zAt ).append( " in '" );
        if ( mLength != 0 ) {
          sb.append( mSource, mInitialReadOffset, mInitialReadOffset + mLength );
        }
        sb.append( "'" ).append( pError ).append( ", from Decode String: " ).append( mSource );
        return new UnacceptableEncodingException( sb.toString() );
      }
    }
  }

  protected interface EntryDecoder<T> {
    EntryDecoder<T> decodeEntry( Entry pEntry );
  }

  protected static abstract class AbstractArrayDecoder<T> {
    private final AtomicBoolean mUsed = new AtomicBoolean( false );
    protected final List<T> mCollector = new ArrayList<>();
    private final EntryProvider mEntryProvider;

    public AbstractArrayDecoder( String pSource, int pToDecodeOffset, ArrayEntryPrefixValidator pPrefixValidator ) {
      mEntryProvider = new EntryProvider( pSource, pToDecodeOffset, pPrefixValidator );
    }

    protected List<T> decodeEntries() {
      singleUse( mUsed, "ArrayDecoder" );
      EntryDecoder<T> zDecoder = createInitialDecoder();
      for ( Entry zEntry = mEntryProvider.next(); zEntry != null; zEntry = mEntryProvider.next() ) {
        zDecoder = zDecoder.decodeEntry( zEntry );
      }
      return mCollector;
    }

    abstract protected EntryDecoder<T> createInitialDecoder();
  }

  protected interface EntryEncoder<T> {
    EntryEncoder<T> add( T zEntry );
  }

  protected static abstract class AbstractArrayEncoder<T> {
    private final AtomicBoolean mUsed = new AtomicBoolean( false );
    protected final StringBuilder mCollector = new StringBuilder();
    private final Iterator<T> mToEncode;

    protected AbstractArrayEncoder( String pEncodedMethodVersionPrefix, List<T> pToEncode ) {
      mCollector.append( pEncodedMethodVersionPrefix );
      mToEncode = pToEncode.iterator();
    }

    public String encode() {
      singleUse( mUsed, "ArrayEncoder" );
      for ( EntryEncoder<T> zProcessor = createInitialEncoder(); mToEncode.hasNext(); ) {
        zProcessor = zProcessor.add( mToEncode.next() );
      }
      return mCollector.toString();
    }

    abstract protected EntryEncoder<T> createInitialEncoder();
  }

  protected static int makeConsistentMutation( int pCeiling ) {
    int zAcceptable = 1;
    if ( pCeiling <= zAcceptable ) {
      throw new Error( "Ceiling (" + pCeiling + ") too small, must be a power of 2 and at least 2" );
    }
    int zCeilingMatcher = 1;
    do {
      zCeilingMatcher = zCeilingMatcher << 1;
      if ( zCeilingMatcher > pCeiling ) {
        throw new Error( "Ceiling (" + pCeiling + ") NOT a power of 2" );
      }
      if ( zCeilingMatcher > zAcceptable ) {
        int zCandidate = (zAcceptable << 2) + 1;
        if ( zCeilingMatcher > zCandidate ) {
          zAcceptable = zCandidate;
        }
      }
    } while ( zCeilingMatcher != pCeiling );
    return zAcceptable;
  }

  protected interface ByteStreamMutator {
    default byte getKeyByte() {
      return 0;
    }

    default byte encode( byte pByte ) {
      return pByte;
    }

    default byte decode( byte pByte ) {
      return pByte;
    }

    ByteStreamMutator NO_OP = new ByteStreamMutator() {
    };
  }

  protected static class ByteStreamMutatorImpl implements ByteStreamMutator {
    private final int mCeiling;
    private final int mMask;
    private final byte mMutator;
    private byte mLastByte;

    public ByteStreamMutatorImpl( int pCeiling, byte pMutator ) {
      mCeiling = pCeiling;
      mMask = pCeiling - 1;
      mLastByte = mMutator = (byte)(pMutator & mMask); // Masking just for Safety
    }

    @Override
    public byte getKeyByte() {
      return mMutator;
    }

    @Override
    public byte encode( byte pByte ) {
      byte zAdjusted = encodeAdjustWithLastByte( pByte ); // Masks
      byte zMutated = (byte)(zAdjusted ^ mMutator); // After Adjust
      return zMutated;
    }

    @Override
    public byte decode( byte pByte ) {
      byte zMutated = (byte)((pByte & mMask) ^ mMutator); // Before Adjust
      byte zAdjusted = decodeAdjustWithLastByte( zMutated );
      return zAdjusted;
    }

    private byte encodeAdjustWithLastByte( byte pNextByte ) {
      int zLast = (mLastByte & mMask);
      int zNext = (pNextByte & mMask);
      byte zAdjusted = (byte)toAdjusted( mCeiling, zLast, zNext );
      mLastByte = zAdjusted;
      return zAdjusted;
    }

    private byte decodeAdjustWithLastByte( byte pAdjustedByte ) {
      int zLast = (mLastByte & mMask);
      int zAdjusted = (pAdjustedByte & mMask);
      byte zNext = (byte)toNext( mCeiling, zLast, zAdjusted );
      mLastByte = pAdjustedByte;
      return zNext;
    }
  }

  /**
   * @param pCeiling Power of 2, to indicate (and adjust for) overflow
   * @param pLast    0-(<code>Ceiling</code> - 1)
   * @param pNext    0-(<code>Ceiling</code> - 1)
   *
   * @return 0-(<code>Ceiling</code> - 1)
   */
  @SuppressWarnings("StatementWithEmptyBody")
  static int toAdjusted( int pCeiling, int pLast, int pNext ) {
    int zAdjusted = pLast + pNext;
    if ( pCeiling <= zAdjusted ) {
      zAdjusted -= pCeiling; // Overflowed, adjust down -> Now: Adjusted < pLast -> Next = (Adjusted + Ceiling) - Last
    } else {
      // No Overflow -> Adjusted < Ceiling -> Next = Adjusted - Last
    }
    return zAdjusted;
  }

  /**
   * @param pCeiling  Power of 2, to adjust for underflow
   * @param pLast     0-(<code>Ceiling</code> - 1)
   * @param pAdjusted 0-(<code>Ceiling</code> - 1)
   *
   * @return 0-(<code>Ceiling</code> - 1)
   */
  static int toNext( int pCeiling, int pLast, int pAdjusted ) {
    int zNext;
    if ( pAdjusted < pLast ) { // Must have overflowed
      zNext = (pAdjusted + pCeiling) - pLast;
    } else { // Adjusted >= Last
      zNext = pAdjusted - pLast;
    }
    return zNext;
  }
}
