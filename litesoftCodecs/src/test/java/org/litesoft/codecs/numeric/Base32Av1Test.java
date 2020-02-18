package org.litesoft.codecs.numeric;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base32Av1Test {

    private static final Integer NULL = null; // ................. 0
    private static final Integer ZERO = 0; // .................... 1
    private static final Integer ONE_PLUS = 1; // ................ 2
    private static final Integer ONE_MINUS = -1; // .............. 3
    private static final Integer FORTY_PLUS = 40; // ............. 4
    private static final Integer FORTY_MINUS = -40; // ........... 5
    private static final Integer MAX = Integer.MAX_VALUE; // ..... 6
    private static final Integer MIN = Integer.MIN_VALUE; // ..... 7
    private static final Integer BITS_16_PLUS = 0xFFFF; // ....... 8
    private static final Integer BITS_16_MINUS = -BITS_16_PLUS; // 9

    private static final Integer[] TO_TEST = {NULL, ZERO, ONE_PLUS, ONE_MINUS, FORTY_PLUS, FORTY_MINUS, MAX, MIN, BITS_16_PLUS, BITS_16_MINUS};

    @Test
    public void rt() {
        IntegerCodec zCodec = Base32Av1.CONSISTENT;

        for ( int i = 0; i < TO_TEST.length; i++ ) {
            Integer zToTest = TO_TEST[i];
            String zEncoded = zCodec.encodeSingle( zToTest );
            Integer zRTed = zCodec.decodeSingle( zEncoded );
            assertEquals( "single To_TEST[" + i + "]: " + zEncoded, zToTest, zRTed );
            // System.out.println( zEncoded + " > " + zToTest );
        }

        String zEncoded = zCodec.encodeMultiple( TO_TEST );
        Integer[] zRTed = zCodec.decodeMultiple( TO_TEST.length, zEncoded );

        System.out.println( "Base32Av1Test.rt: " + zEncoded );

        for ( int i = 0; i < TO_TEST.length; i++ ) {
            assertEquals( "multiple To_TEST[" + i + "]", TO_TEST[i], zRTed[i] );
        }
    }
}