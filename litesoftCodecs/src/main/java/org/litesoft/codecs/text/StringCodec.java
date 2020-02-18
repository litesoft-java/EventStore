package org.litesoft.codecs.text;

import org.litesoft.codecs.Codec;
import org.litesoft.codecs.UnacceptableEncodingException;

@SuppressWarnings("unused")
public interface StringCodec extends Codec {
    /**
     * Encode the <code>pToEncode</>.
     *
     * @param pToEncode Nullable
     * @return !Null
     */
    default String encodeSingle( String pToEncode ) {
        return encodeMultiple( pToEncode );
    }

    /**
     * Decode the <code>pToDecode</code>, which is the result of a previous call to <code>encode</code>.
     *
     * @param pToDecode !Null
     * @return Nullable
     * @throws UnacceptableEncodingException should the <code>pDecode</code> not be able to be Decoded
     */
    default String decodeSingle( String pToDecode )
            throws UnacceptableEncodingException {
        return decodeMultiple( 1, pToDecode )[0];
    }

    /**
     * Encode the <code>pToEncode</> (possibly Multiple Strings).
     *
     * @param pToEncode can be an empty array or Null which will be treated like an empty array
     * @return !Null
     */
    String encodeMultiple( String... pToEncode );

    /**
     * Decode the <code>pToDecode</code>, which is the result of a previous call to <code>encode</code>.
     *
     * @param pFieldsExpected 0 means No minimum, positive number means need the exact number, negative number means at least abs(<coded>pFieldsExpected</coded>).
     * @param pToDecode       !Null
     * @return !Null (could be an empty array)
     * @throws UnacceptableEncodingException should the <code>pDecode</code> not be able to be Decoded
     */
    String[] decodeMultiple( int pFieldsExpected, String pToDecode )
            throws UnacceptableEncodingException;
}
