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
public class ISO8601 {
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
    },
    Year( 4 ),
    Month( '-', 7, new IntRange( 1, 12 ) ),
    Day( '-', 10, new IntRange( 1, 31 ) ) {
      @Override
      protected String checkAboveNominalRange( String pISO8601str, int pDay, int pNominalThruLimit ) {
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
    Hour( 'T', 13, new IntRange( 0, 23 ) ),
    Min( ':', 16, new IntRange( 0, 59 ) ),
    Second( ':', 19, new IntRange( 0, 60 ) ), // Leap Sec
    FracSecs3( '.', 23, true ),
    FracSecs6( 26, true ),
    FracSecs9( 29, true );

    private final int mFrom;
    private final Integer mLength;
    private final Character mPrefixChar;
    private final boolean mPadable;
    private final IntRange mRange;

    Resolution( Integer pLength, Character pPrefixChar, boolean pPadable, IntRange pRange ) {
      mLength = pLength;
      mPrefixChar = pPrefixChar;
      mPadable = pPadable;
      mRange = pRange;
      int zOrdinal = ordinal();
      int zFrom = 0;
      if ( zOrdinal > 1 ) { // All BUT None & Year
        zFrom = Resolution.values()[zOrdinal - 1].mLength;
      }
      mFrom = zFrom;
    }

    Resolution() {
      this( null, null, false, null );
    }

    Resolution( int pLength ) {
      this( pLength, null, false, null );
    }

    Resolution( char pPrefixChar, int pLength, IntRange pRange ) {
      this( pLength, pPrefixChar, false, pRange );
    }

    Resolution( char pPrefixChar, int pLength, boolean pPadable ) {
      this( pLength, pPrefixChar, pPadable, null );
    }

    Resolution( int pLength, boolean pPadable ) {
      this( pLength, null, pPadable, null );
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
      if ( mRange == null ) { // Left to Right!
        return null;
      }
      if ( zValue < mRange.getFrom() ) {
        return name() + " value must be at least (" + mRange.getFrom() + "), but was: " + zValue;
      }
      return checkAboveNominalRange( pISO8601str, zValue, mRange.getThru() );
    }

    protected String checkAboveNominalRange( String pISO8601str, int pValue, int pNominalThruLimit ) {
      return (pValue <= pNominalThruLimit) ? null : (name() + " value must be not be greater than (" + pNominalThruLimit + "), but was: " + pValue);
    }

    String adjustTo( String pISO8601str, Resolution pTargetRes ) {
      // XXX
      return null;
    }
  }

  private final String mISO8601str, mError;
  private final Resolution mResolution;

  private ISO8601( String pISO8601str, Resolution pResolution, String pError ) {
    mISO8601str = pISO8601str;
    mResolution = pResolution;
    mError = pError;
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
      return new ISO8601( pISO8601str, Resolution.None, "Too short" );
    }
    pISO8601str = pISO8601str.toUpperCase();
    if ( 'Z' != pISO8601str.charAt( zLength - 1 ) ) {
      return new ISO8601( pISO8601str, Resolution.None, "no 'Z' terminator" );
    }
    if ( 30 < zLength ) {
      return new ISO8601( pISO8601str, Resolution.None, "Too long" );
    }
    zLength--; // ignore the 'Z'
    Resolution[] zResolutions = Resolution.values();
    for ( int i = 1; i < zResolutions.length; i++ ) { // Skip 'None'
      Resolution zRes = zResolutions[i];
      int zResLength = zRes.getLength();
      if ( zLength < zResLength ) {
        if ( !zRes.isPadable() ) {
          return new ISO8601( pISO8601str, Resolution.None, zRes.name() + " field incomplete,  (too short): " + pISO8601str );
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

  public ValidationResult<String> toMin() {
    if ( mError != null ) {
      return ValidationResult.withValue( mISO8601str ).error( mError );
    }
    return ValidationResult.withValue( mResolution.adjustTo( mISO8601str, Resolution.Min ) ).ok();
  }
}
