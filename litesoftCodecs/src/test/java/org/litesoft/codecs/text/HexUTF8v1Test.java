package org.litesoft.codecs.text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HexUTF8v1Test {

    private static final String NULL = null; // ................. 0
    private static final String EMPTY = ""; // .................. 1
    private static final String TEST = "Test"; // ............... 2
    private static final String NON_ASCII_FEAR = "恐れ = Fear"; // 3

    private static final String[] TO_TEST = {NULL, EMPTY, TEST, NON_ASCII_FEAR};

    @Test
    public void rt() {
        StringCodec zCodec = HexUTF8v1.CONSISTENT;

        for ( int i = 0; i < TO_TEST.length; i++ ) {
            String zToTest = TO_TEST[i];
            String zEncoded = zCodec.encodeSingle( zToTest );
            String zRTed = zCodec.decodeSingle( zEncoded );
            assertEquals( "single To_TEST[" + i + "]: " + zEncoded, zToTest, zRTed );
            System.out.println( zEncoded + " > " + zToTest );
        }

        String zEncoded = zCodec.encodeMultiple( TO_TEST );
        String[] zRTed = zCodec.decodeMultiple( TO_TEST.length, zEncoded );

        System.out.println( "HexUTF8v1Test.rt: " + zEncoded );

        for ( int i = 0; i < TO_TEST.length; i++ ) {
            assertEquals( "multiple To_TEST[" + i + "]", TO_TEST[i], zRTed[i] );
        }
    }
}