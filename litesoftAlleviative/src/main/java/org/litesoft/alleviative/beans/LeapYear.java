package org.litesoft.alleviative.beans;

public class LeapYear {
    /**
     * return true if the Year passed is a Leap Year (it uses the common Gregorian calculation so may be inaccurate
     * for some religions, periods, and/or countries that were (or are still) not using the Gregorian calendar for the year passed in).
     */
    public static boolean check( int pYear ) {
        if ( 0 != (pYear % 4) ) {
            return false;
        }
        if ( 0 == (pYear % 400) ) {
            return true;
        }
        return (0 != (pYear % 100));
    }
}
