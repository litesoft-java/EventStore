package org.litesoft.codecs.numeric;

import org.litesoft.codecs.Codec;
import org.litesoft.codecs.UnacceptableEncodingException;

@SuppressWarnings("unused")
public interface IntegerCodec extends Codec {
  /**
   * Encode the <code>pToEncode</>.
   *
   * @param pToEncode Nullable
   *
   * @return !Null
   */
  default String encodeSingle( Integer pToEncode ) {
    return encodeMultiple( pToEncode );
  }

  /**
   * Decode the <code>pToDecode</code>, which is the result of a previous call to <code>encode</code>.
   *
   * @param pToDecode !Null
   *
   * @return Nullable
   *
   * @throws UnacceptableEncodingException should the <code>pDecode</code> not be able to be Decoded
   */
  default Integer decodeSingle( String pToDecode )
          throws UnacceptableEncodingException {
    return decodeMultiple( 1, pToDecode )[0];
  }

  /**
   * Encode the <code>pToEncode</> (possibly Multiple Strings).
   *
   * @param pToEncode can be an empty array or Null which will be treated like an empty array
   *
   * @return !Null
   */
  String encodeMultiple( Integer... pToEncode );

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
  Integer[] decodeMultiple( int pFieldsExpected, String pToDecode )
          throws UnacceptableEncodingException;
}
