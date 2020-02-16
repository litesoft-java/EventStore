package org.litesoft.alleviative.validation;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.litesoft.alleviative.validation.ISO8601.Resolution.*;

public class ISO8601Test {

  @Test
  public void validateRequired() {
    assertOK( ISO8601.validateRequired( ISO8601.EXAMPLE ), ISO8601.EXAMPLE );
    assertError( ISO8601.validateRequired( null ), "is Required" );
    assertError( ISO8601.validateRequired( "  " ), "is Required" );
    assertError( ISO8601.validateRequired( "202" ), "too short" );
    assertError( ISO8601.validateRequired( "2020-02" ), "no 'Z' terminator" );
    assertError( ISO8601.validateRequired( "2020-02-29T23:59:58.1234567890Z" ), "too long" );

    assertTooShort( "2020-2Z", "Month" );
    assertTooShort( "2020-02-9Z", "Day" );
    assertTooShort( "2020-02-29T3Z", "Hour" );
    assertTooShort( "2020-02-29T23:9Z", "Min" );
    assertTooShort( "2020-02-29T23:59:8Z", "Second" );

    assertBadPrefix( "2020_02Z", "Month", '-' );
    assertBadPrefix( "2020-02_09Z", "Day", '-' );
    assertBadPrefix( "2020-02-29_03Z", "Hour", 'T' );
    assertBadPrefix( "2020-02-29T23_09Z", "Min", ':' );
    assertBadPrefix( "2020-02-29T23:59_08Z", "Second", ':' );
    assertBadPrefix( "2020-02-29T23:59:58_0Z", "FracSecs3", '.' );

    assertFieldTooShort( "202-02-29T23:59:58.123456789Z", "Year", '-', 3 );
    assertFieldTooShort( "2020-2-29T23:59:58.123456789Z", "Month", '-', 6 );
    assertFieldTooShort( "2020-02-9T23:59:58.123456789Z", "Day", 'T', 9 );
    assertFieldTooShort( "2020-02-29T3:59:58.123456789Z", "Hour", ':', 12 );
    assertFieldTooShort( "2020-02-29T23:9:58.123456789Z", "Min", ':', 15 );
    assertFieldTooShort( "2020-02-29T23:59:8.123456789Z", "Second", '.', 18 );

    assertPadding( "2020-02-29T23:59:58.1Z", "2020-02-29T23:59:58.100Z" );
    assertPadding( "2020-02-29T23:59:58.1234Z", "2020-02-29T23:59:58.123400Z" );
    assertPadding( "2020-02-29T23:59:58.1234567Z", "2020-02-29T23:59:58.123456700Z" );

    assertRangesOK( "0000-01-01T00:00:00.000000000Z" ); // Lower
    assertRangesOK( "9999-12-31T23:59:59.999999999Z" ); // Upper
    assertRangesOK( "9999-06-30T23:59:60.999999999Z" ); // Upper (Leap Sec)
    assertRangesOK( "9999-12-31T23:59:60.999999999Z" ); // Upper (Leap Sec)

    assertRangeFromError( "2020-00-29T23:59:58Z", "Month", 1, 0 );
    assertRangeFromError( "2020-01-00T23:59:58Z", "Day", 1, 0 );

    assertRangeThruError( "2021-13-29T23:59:58Z", "Month", 12, 13 );
    assertRangeThruError( "2021-01-29T24:59:58Z", "Hour", 23, 24 );
    assertRangeThruError( "2021-01-29T23:60:58Z", "Min", 59, 60 );
    // Seconds
    assertRangeThruError( "2021-01-31T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-02-28T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-03-31T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-04-30T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-05-31T23:59:60Z", "Second", 59, 60 );
    // ertRangeThruError( "2021-06-30T23:59:60Z", "Second", 59, 60 ); // Leap Sec
    assertRangeThruError( "2021-07-31T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-08-31T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-09-30T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-10-31T23:59:60Z", "Second", 59, 60 );
    assertRangeThruError( "2021-11-30T23:59:60Z", "Second", 59, 60 );
    // ertRangeThruError( "2021-12-31T23:59:60Z", "Second", 59, 60 ); // Leap Sec

    // Leap Secs
    assertError( ISO8601.validateRequired( "2021-06-30T23:59:61Z" ),
                 "30 Jun in the last minute (23:59) may have a leap sec (23:59:60), but definitely can't have: 61" );
    assertError( ISO8601.validateRequired( "2021-12-31T23:59:61Z" ),
                 "31 Dec in the last minute (23:59) may have a leap sec (23:59:60), but definitely can't have: 61" );

    // Day
    assertDayThruError( "2021-01-32T23:59:58Z", "Jan", 31, 32 );
    // sertRangeThruError( "2021-02-31T23:59:58Z", "Day", 28, 32 );
    assertDayThruError( "2021-03-32T23:59:58Z", "Mar", 31, 32 );
    assertDayThruError( "2021-04-31T23:59:58Z", "Apr", 30, 31 );
    assertDayThruError( "2021-05-32T23:59:58Z", "May", 31, 32 );
    assertDayThruError( "2021-06-31T23:59:58Z", "Jun", 30, 31 );
    assertDayThruError( "2021-07-32T23:59:58Z", "Jul", 31, 32 );
    assertDayThruError( "2021-08-32T23:59:58Z", "Aug", 31, 32 );
    assertDayThruError( "2021-09-31T23:59:58Z", "Sep", 30, 31 );
    assertDayThruError( "2021-10-32T23:59:58Z", "Oct", 31, 32 );
    assertDayThruError( "2021-11-31T23:59:58Z", "Nov", 30, 31 );
    assertDayThruError( "2021-12-32T23:59:58Z", "Dec", 31, 32 );
    // Day Feb
    assertError( ISO8601.validateRequired( "1900-02-29Z" ), "Feb 1900 only has 28 days, but found: 29" );
    assertError( ISO8601.validateRequired( "2000-02-30Z" ), "Feb 2000 only has 29 days, but found: 30" );
    assertError( ISO8601.validateRequired( "2020-02-30Z" ), "Feb 2020 only has 29 days, but found: 30" );

//    assertError( ISO8601.validateRequired( "2020-02" ), "XXX" );
  }

  private void assertBadPrefix( String pISO8601, String pField, char pExpectedPrefix ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pField + " was not preceded by a '" + pExpectedPrefix + "'" );
  }

  private void assertDayThruError( String pISO8601, String pMonth, int pExpected, int pActual ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pMonth + " only has " + pExpected + " days, but found: " + pActual );
  }

  private void assertRangeThruError( String pISO8601, String pField, int pExpected, int pActual ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pField + " value must not be greater than (" + pExpected + "), but was: " + pActual );
  }

  @SuppressWarnings("SameParameterValue")
  private void assertRangeFromError( String pISO8601, String pField, int pExpected, int pActual ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pField + " value must be at least (" + pExpected + "), but was: " + pActual );
  }

  private void assertTooShort( String pISO8601, String pField ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pField + " field incomplete, (too short): " + pISO8601 );
  }

  private void assertFieldTooShort( String pISO8601, String pField, char pBadChar, int pOffset ) {
    assertError( ISO8601.validateRequired( pISO8601 ),
                 pField + " parse error: expected digit, but got '" + pBadChar + "' at offset: " + pOffset );
  }

  private void assertPadding( String pInISO8601, String pOutISO8601 ) {
    assertOK( ISO8601.validateRequired( pInISO8601 ), pOutISO8601 );
  }

  private void assertRangesOK( String pISO8601 ) {
    assertOK( ISO8601.validateRequired( pISO8601 ), pISO8601 );
  }

  @Test
  public void validateOptional() {
    assertOK( ISO8601.validateOptional( null ), null );
    assertOK( ISO8601.validateOptional( "  " ), null );
    assertOK( ISO8601.validateOptional( ISO8601.EXAMPLE ), ISO8601.EXAMPLE );
  }

  private static class RD {
    private final ISO8601.Resolution mRes;
    private final String mExtension;
    private final String mZlessValue;

    private RD( ISO8601.Resolution pRes, String pExtension, String pZlessValue ) {
      mRes = pRes;
      mExtension = pExtension;
      mZlessValue = pZlessValue;
    }
  }

  private static RD rd( ISO8601.Resolution pRes, String pExtension, String pZlessValue ) {
    return new RD( pRes, pExtension, pZlessValue );
  }

  private static RD[] RDs = {
          rd( Year, /* ........ */ null, "2020" ),
          rd( Month, /* ...... */ "-01", "2020-02" ),
          rd( Day, /* ........ */ "-01", "2020-02-29" ),
          rd( Hour, /* ....... */ "T00", "2020-02-29T23" ),
          rd( Min, /* ........ */ ":00", "2020-02-29T23:59" ),
          rd( Second, /* ..... */ ":00", "2020-02-29T23:59:58" ),
          rd( FracSecs3, /* . */ ".000", "2020-02-29T23:59:58.123" ),
          rd( FracSecs6, /* .. */ "000", "2020-02-29T23:59:58.123456" ),
          rd( FracSecs9, /* .. */ "000", "2020-02-29T23:59:58.123456789" ),
          };

  @Test
  public void adjustTo() {
    for ( int i = 0; i < RDs.length; i++ ) {
      RD zSource = RDs[i];
      roundTrip( zSource );
      for ( int j = 0; j < i; j++ ) {
        shorter( zSource, RDs[j] );
      }
      String zAdd = "";
      for ( int j = i + 1; j < RDs.length; j++ ) {
        RD zRD = RDs[j];
        zAdd += zRD.mExtension;
        longer( zSource, zRD, zAdd );
      }
    }
  }

  private void roundTrip( RD pSource ) {
    String zValue = pSource.mZlessValue + "Z";

    ISO8601 zResult = ISO8601.validateRequired( zValue );
    assertOK( zResult, zValue );

    ValidationResult<String> zValidationResult = zResult.toRes( pSource.mRes );
    assertOK( zValidationResult, zValue );
  }

  private void shorter( RD pSource, RD pTarget ) {
    checkAdjusted( pSource, pSource.mZlessValue + "Z", pTarget, pTarget.mZlessValue + "Z" );
  }

  private void longer( RD pSource, RD pTarget, String pAdd ) {
    checkAdjusted( pSource, pSource.mZlessValue + "Z", pTarget, pSource.mZlessValue + pAdd + "Z" );
  }

  private void checkAdjusted( RD pSource, String pSourceValue, RD pTarget, String pTargetValue ) {
    ValidationResult<String> zResult = ISO8601.validateRequired( pSourceValue ).toRes( pTarget.mRes );

    if ( zResult.hasError() ) {
      fail( "Expected (" + pTargetValue + "), but got error: " + zResult.getError() );
    }
    assertEquals( "OK (" + pSource.mRes + " -> " + pTarget.mRes + ")but...", pTargetValue, zResult.getValue() );
  }

  private void assertError( ISO8601 pResult, String pExpectedError ) {
    if ( !pResult.hasError() ) {
      fail( "No Error, ISO8601 (" + pResult.getISO8601() + "), Expected error was: " + pExpectedError );
    }
    assertEquals( "Error but...", pExpectedError, pResult.getError() );
  }

  @SuppressWarnings("unused")
  private void assertError( ValidationResult<String> pResult, String pExpectedError ) {
    if ( !pResult.hasError() ) {
      fail( "No Error, ISO8601 (" + pResult.getValue() + "), Expected error was: " + pExpectedError );
    }
    assertEquals( "Error but...", pExpectedError, pResult.getError() );
  }

  private void assertOK( ISO8601 pResult, String pExpectedISO8601 ) {
    if ( pResult.hasError() ) {
      fail( "Expected (" + pExpectedISO8601 + "), but got error: " + pResult.getError() );
    }
    assertEquals( "OK but...", pExpectedISO8601, pResult.getISO8601() );
  }

  private void assertOK( ValidationResult<String> pResult, String pExpectedISO8601 ) {
    if ( pResult.hasError() ) {
      fail( "Expected (" + pExpectedISO8601 + "), but got error: " + pResult.getError() );
    }
    assertEquals( "OK but...", pExpectedISO8601, pResult.getValue() );
  }
}