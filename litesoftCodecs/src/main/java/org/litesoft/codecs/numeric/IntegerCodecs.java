package org.litesoft.codecs.numeric;

import java.util.HashMap;
import java.util.Map;

import org.litesoft.codecs.AbstractCodecs;
import org.litesoft.codecs.UnacceptableEncodingException;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class IntegerCodecs extends AbstractCodecs<IntegerCodec, IntegerCodecs> implements IntegerCodec {
  protected static final Integer[] EMPTY_ARRAY = new Integer[0];

  private static final Map<String, Map<Integer, IntegerCodec>> INSTANCES_BY_VERSION_BY_SHORT_NAME = new HashMap<>();

  /**
   * @param pShortName must start with a 7 Bit Ascii Letter, followed by a few 7 Bit Ascii Letters or Digits!
   * @param pVersion   Positive!
   */
  protected IntegerCodecs( boolean pRegister, String pShortName, int pVersion ) {
    super( IntegerCodec.class, pRegister, pShortName, pVersion, INSTANCES_BY_VERSION_BY_SHORT_NAME );
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
  public static IntegerCodec get( String pToDecode )
          throws UnacceptableEncodingException {
    return findCodec( pToDecode, INSTANCES_BY_VERSION_BY_SHORT_NAME );
  }

  /**
   * Encode the <code>pToEncode</> (possibly Multiple Strings).
   *
   * @param pToEncode can be an empty array or Null which will be treated like an empty array
   *
   * @return !Null
   */
  @Override
  public final String encodeMultiple( Integer... pToEncode ) {
    return createArrayEncoder( encodedMethodVersionPrefix(),
                               (pToEncode != null) ? pToEncode : EMPTY_ARRAY )
            .encode();
  }

  /**
   * Decode the <code>pToDecode</code>, which is the result of a previous call to <code>encode</code>.
   *
   * @param pFieldsExpected 0 means No minimum, positive number means need the exact number, negative number means at least abs(<coded>pFieldsExpected</coded>).
   * @param pToDecode       !Null
   *
   * @return !Null (could be an empty array)
   *
   * @throws UnacceptableEncodingException should the <code>pDecode</code> not be able to be Decoded
   */
  @Override
  public Integer[] decodeMultiple( int pFieldsExpected, String pToDecode )
          throws UnacceptableEncodingException {
    int zDecodeAt = getPostPrefixOffset( pFieldsExpected, pToDecode );
    Integer[] zFields = (zDecodeAt == -1) ?
                        EMPTY_ARRAY :
                        createArrayDecoder( pToDecode, zDecodeAt ).decode();

    checkExpected( pFieldsExpected, zFields.length, pToDecode );

    return zFields;
  }

  /**
   * @param pEncodedMethodVersionPrefix !Empty (assumed correct)
   * @param pToEncode                   !null, but could contain nulls or be an empty array
   *
   * @return !null
   */
  abstract protected ArrayEncoder createArrayEncoder( String pEncodedMethodVersionPrefix, Integer[] pToEncode );

  protected interface ArrayDecoder {
    /**
     * Not reusable - Single call only!
     *
     * @return !null, could be empty and the entries can be empty or null
     */
    Integer[] decode()
            throws UnacceptableEncodingException;
  }

  /**
   * Only called if there are characters after the prefix!
   *
   * @param pDecodeAt points to the first character after the prefix
   *
   * @return !null
   */
  abstract protected ArrayDecoder createArrayDecoder( String pSource, int pDecodeAt );
}
