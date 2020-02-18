package org.litesoft.codecs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AbstractCodecsTest {

    @Test
    public void consistentMutation() {
        assertEquals( 0x55, AbstractCodecs.makeConsistentMutation( 256 ) );
        assertEquals( 0x15, AbstractCodecs.makeConsistentMutation( 32 ) );
        try {
            int zResult = AbstractCodecs.makeConsistentMutation( 33 );
            fail( "Unexpected Result (" + zResult + ") from non-power of 2: 33" );
        }
        catch ( Error expected ) {
            // Ignore...
        }
    }

    @Test
    public void rtAdjust256() {
        check( 256 );
    }

    @Test
    public void rtAdjust32() {
        check( 32 );
    }

    private void check( int pCeiling ) {
        for ( int i = 0; i < pCeiling; i++ ) {
            for ( int j = 0; j < pCeiling; j++ ) {
                check( pCeiling, i, j );
            }
        }
    }

    private void check( int pCeiling, int pLast, int pNext ) {
        int zAdjusted = AbstractCodecs.toAdjusted( pCeiling, pLast, pNext );
        int zNext = AbstractCodecs.toNext( pCeiling, pLast, zAdjusted );
        if ( zNext != pNext ) {
            fail( "toAdjusted (Orig Next " + pNext + " != " + zNext + " Calculated Next) \n" +
                  " toAdjusted( Last: " + pLast + ",     Next: " + pNext + " ) -> " + zAdjusted + "\n" +
                  "     toNext( Last: " + pLast + ", Adjusted: " + zAdjusted + " ) -> " + zNext );
        }
    }
}