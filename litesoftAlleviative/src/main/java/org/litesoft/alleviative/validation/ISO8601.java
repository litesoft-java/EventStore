package org.litesoft.alleviative.validation;

import org.litesoft.alleviative.beans.IntRange;
import org.litesoft.alleviative.beans.LeapYear;
import org.litesoft.alleviative.beans.Months;

/**
 * Only supports years from 0000 to 9999!
 * <p>
 * Example Form: 2020[-02[-29[T23[:59[:59[.1[2[3[4[5[6[7[8[9]]]... and a 'Z' at the end!
 * <li> 2020-02-29T23:59:59.123456789 </li>
 * <li> 01234567890123456789012345678 </li>
 * <li> ..........1.........2........ </li>
 * <li> ....4..7..0..3..6..90.2345678 </li>
 */
@SuppressWarnings("unused")
public class ISO8601 {
    public static final String EXAMPLE = "2020-02-29T23:59:59.123456789Z";

    private final String mISO8601str, mError;
    private final Resolution mResolution;

    private ISO8601( String pISO8601str, Resolution pResolution, String pError ) {
        mISO8601str = pISO8601str;
        mResolution = pResolution;
        mError = pError;
    }

    public boolean hasError() {
        return (mError != null);
    }

    public String getError() {
        return mError;
    }

    public String getISO8601() {
        return mISO8601str;
    }

    public Resolution getResolution() {
        return mResolution;
    }

    @SuppressWarnings("unused")
    public static ISO8601 validateOptional( String pISO8601str ) {
        String zISO8601str = Significant.orNull( pISO8601str );
        return (zISO8601str != null) ? validate( zISO8601str.toUpperCase() ) :
               new ISO8601( null, Resolution.None, null );
    }

    public static ISO8601 validateRequired( String pISO8601str ) {
        String zISO8601str = Significant.orNull( pISO8601str );
        return (zISO8601str != null) ? validate( zISO8601str.toUpperCase() ) :
               new ISO8601( null, Resolution.None, "is Required" );
    }

    private static ISO8601 validate( String pISO8601str ) {
        int zLength = pISO8601str.length();
        if ( zLength < 5 ) {
            return new ISO8601( pISO8601str, Resolution.None, "too short" );
        }
        pISO8601str = pISO8601str.toUpperCase();
        if ( 'Z' != pISO8601str.charAt( zLength - 1 ) ) {
            return new ISO8601( pISO8601str, Resolution.None, "no 'Z' terminator" );
        }
        if ( EXAMPLE.length() < zLength ) {
            return new ISO8601( pISO8601str, Resolution.None, "too long" );
        }
        zLength--; // ignore the 'Z'
        Resolution[] zResolutions = Resolution.values();
        for ( int i = 1; i < zResolutions.length; i++ ) { // Skip 'None'
            Resolution zRes = zResolutions[i];
            int zResLength = zRes.getLength();
            if ( zLength < zResLength ) {
                if ( !zRes.isPadable() ) {
                    return new ISO8601( pISO8601str, Resolution.None, zRes.name() + " field incomplete, (too short): " + pISO8601str );
                }
                StringBuilder sb = new StringBuilder( zResLength + 1 ).append( pISO8601str );
                sb.setCharAt( zLength++, '0' ); // Convert the 'Z' to a '0'
                for ( ; zLength < zResLength; zLength++ ) {
                    sb.append( '0' );
                }
                pISO8601str = sb.append( 'Z' ).toString(); // Add the 'Z' back!
            }
            String zError = zRes.checkAcceptability( pISO8601str );
            if ( zError != null ) {
                return new ISO8601( pISO8601str, Resolution.None, zError );
            }
            if ( zResLength == zLength ) {
                return new ISO8601( pISO8601str, zRes, null );
            }
        }
        throw new IllegalStateException( "Should not have reached this point w/ '" + pISO8601str + "'" );
    }

    public ValidationResult<String> toYear() {
        return toRes( Resolution.Year );
    }

    public ValidationResult<String> toMonth() {
        return toRes( Resolution.Month );
    }

    public ValidationResult<String> toDay() {
        return toRes( Resolution.Day );
    }

    public ValidationResult<String> toHour() {
        return toRes( Resolution.Hour );
    }

    public ValidationResult<String> toMin() {
        return toRes( Resolution.Min );
    }

    public ValidationResult<String> toSecond() {
        return toRes( Resolution.Second );
    }

    public ValidationResult<String> toFracSecs3() {
        return toRes( Resolution.FracSecs3 );
    }

    public ValidationResult<String> toFracSecs6() {
        return toRes( Resolution.FracSecs6 );
    }

    public ValidationResult<String> toFracSecs9() {
        return toRes( Resolution.FracSecs9 );
    }

    ValidationResult<String> toRes( Resolution pTarget ) {
        if ( mError != null ) {
            return ValidationResult.withValue( mISO8601str ).error( mError );
        }
        return ValidationResult.withValue( mResolution.adjustTo( mISO8601str, pTarget ) ).ok();
    }

    @SuppressWarnings("unused")
    public enum Resolution {
        None {
            @Override
            int getLength() {
                throw new UnsupportedOperationException( "'None' has no Length" );
            }

            @Override
            public int getValue( String pISO8601str ) {
                throw new UnsupportedOperationException( "'None' has no Value" );
            }

            @Override
            String adjustTo( String pISO8601str, Resolution pTargetRes ) {
                throw new UnsupportedOperationException( "'None' can not be adjusted to: " + pTargetRes );
            }

            @Override
            void shrink( StringBuilder sb ) {
                throw new UnsupportedOperationException( "'None' can not be an adjusted to target" );
            }
        },
        Year( 0, 4 ),
        Month( 4, '-', 7, new IntRange( 1, 12 ) ),
        Day( 7, '-', 10, new IntRange( 1, 31 ) ) {
            @Override
            protected String checkRangeThru( String pISO8601str, int pDay, int pNominalThruLimit ) {
                int zMonth = Month.getValue( pISO8601str );
                if ( zMonth != 2 ) {
                    int zMaxDays = Months.is31dayMonth( zMonth ) ? 31 : 30;
                    return (pDay <= zMaxDays) ? null :
                           Months.getMonthDataForMonth( zMonth ).get3Code() + " only has " + zMaxDays + " days, but found: " + pDay;
                }
                // February
                int zYear = Year.getValue( pISO8601str );
                int zMaxDay = LeapYear.check( zYear ) ? 29 : 28;
                return (pDay <= zMaxDay) ? null :
                       Months.getMonthDataForMonth( zMonth ).get3Code() + " " + zYear + " only has " + zMaxDay + " days, but found: " + pDay;
            }
        },
        Hour( 10, 'T', 13, new IntRange( 0, 23 ) ),
        Min( 13, ':', 16, new IntRange( 0, 59 ) ),
        Second( 16, ':', 19, new IntRange( 0, 59 ) ) {
            @Override
            protected String checkRangeThru( String pISO8601str, int pSecond, int pNominalThruLimit ) {
                if ( pSecond < 60 ) {
                    return null;
                }
                // Check Leap Secs
                String zHrAndMin = pISO8601str.substring( Hour.mFrom, Min.mLength );
                if ( !"T23:59".equals( zHrAndMin ) ) { // Not Last Minute
                    return super.checkRangeThru( pISO8601str, pSecond, pNominalThruLimit );
                }
                int zMonth = Month.getValue( pISO8601str );
                if ( (zMonth != 6) && (zMonth != 12) ) { // Not appropriate Months
                    return super.checkRangeThru( pISO8601str, pSecond, pNominalThruLimit );
                }
                int zDay = Day.getValue( pISO8601str );
                if ( zDay != Months.getMonthDataForMonth( zMonth ).getNominalDays() ) { // Not Last Day
                    return super.checkRangeThru( pISO8601str, pSecond, pNominalThruLimit );
                }
                if ( pSecond == 60 ) {
                    return null; // Leap Second OK!
                }
                return zDay + " " + Months.getMonthDataForMonth( zMonth ).get3Code() +
                       " in the last minute (23:59) may have a leap sec (23:59:60), but definitely can't have: " + pSecond;
            }
        },
        FracSecs3( 19, '.', 23, true ),
        FracSecs6( 23, 26, true ),
        FracSecs9( 26, 29, true );

        private final int mFrom;
        private final int mLength;
        private final Character mPrefixChar;
        private final boolean mPadable;
        private final IntRange mRange;

        Resolution( int pFrom, int pLength, Character pPrefixChar, boolean pPadable, IntRange pRange ) {
            mFrom = pFrom;
            mLength = pLength;
            mPrefixChar = pPrefixChar;
            mPadable = pPadable;
            mRange = pRange;
        }

        Resolution() {
            this( -1, -1, null, false, null );
        }

        Resolution( int pFrom, int pLength ) {
            this( pFrom, pLength, null, false, null );
        }

        Resolution( int pFrom, char pPrefixChar, int pLength, IntRange pRange ) {
            this( pFrom, pLength, pPrefixChar, false, pRange );
        }

        Resolution( int pFrom, char pPrefixChar, int pLength, boolean pPadable ) {
            this( pFrom, pLength, pPrefixChar, pPadable, null );
        }

        Resolution( int pFrom, int pLength, boolean pPadable ) {
            this( pFrom, pLength, null, pPadable, null );
        }

        private boolean hasRange() {
            return (mRange != null);
        }

        public boolean hasPrefixChar() {
            return (mPrefixChar != null);
        }

        public Character getPrefixChar() {
            return mPrefixChar;
        }

        int getValue( String pISO8601str ) {
            int zFrom = mFrom + ((mPrefixChar != null) ? 1 : 0);
            return Integer.parseUnsignedInt( pISO8601str.substring( zFrom, mLength ) );
        }

        int getLength() {
            return mLength;
        }

        boolean isPadable() {
            return mPadable;
        }

        String checkAcceptability( String pISO8601str ) {
            Character zPrefixChar = getPrefixChar();
            int zFrom = mFrom;
            if ( zPrefixChar != null ) {
                if ( pISO8601str.charAt( zFrom ) != zPrefixChar ) {
                    return name() + " was not preceded by a '" + zPrefixChar + "'";
                }
                zFrom++;
            }
            int zValue = 0;
            for ( int zLength = getLength(); zFrom < zLength; zFrom++ ) {
                char c = pISO8601str.charAt( zFrom );
                if ( (c < '0') || ('9' < c) ) {
                    return name() + " parse error: expected digit, but got '" + c + "' at offset: " + zFrom;
                }
                zValue = (zValue * 10) + (c - '0');
            }
            if ( !hasRange() ) {
                return null;
            }
            if ( zValue < mRange.getFrom() ) {
                return name() + " value must be at least (" + mRange.getFrom() + "), but was: " + zValue;
            }
            return checkRangeThru( pISO8601str, zValue, mRange.getThru() );
        }

        protected String checkRangeThru( String pISO8601str, int pValue, int pNominalThruLimit ) {
            return (pValue <= pNominalThruLimit) ? null : (name() + " value must not be greater than (" + pNominalThruLimit + "), but was: " + pValue);
        }

        String adjustTo( String pISO8601str, Resolution pTargetRes ) {
            if ( this == pTargetRes ) {
                return pISO8601str;
            }
            int zCurrentOrd = this.ordinal();
            int zTargetOrd = pTargetRes.ordinal();
            StringBuilder sb = new StringBuilder().append( pISO8601str ); // with 'Z'
            if ( zTargetOrd < zCurrentOrd ) {
                pTargetRes.shrink( sb );
            } else { // Target Longer (Can't be None)
                sb.setLength( getLength() );
                getByOrdinal( zCurrentOrd + 1 ).stretch( sb, pTargetRes );
            }
            return sb.append( 'Z' ).toString();
        }

        void stretch( StringBuilder sb, Resolution pTargetRes ) {
            if ( hasPrefixChar() ) {
                sb.append( getPrefixChar() );
            }
            int zValue = hasRange() ? mRange.getFrom() : 0;
            String zAdd = Integer.toString( zValue );
            for ( int z0sToAdd = (getLength() - sb.length()) - zAdd.length(); z0sToAdd > 0; z0sToAdd-- ) {
                sb.append( '0' );
            }
            sb.append( zAdd );
            if ( this != pTargetRes ) {
                getByOrdinal( ordinal() + 1 ).stretch( sb, pTargetRes );
            }
        }

        void shrink( StringBuilder sb ) {
            sb.setLength( getLength() );
        }

        private static Resolution getByOrdinal( int pOrdinal ) {
            return Resolution.values()[pOrdinal];
        }
    }
}
