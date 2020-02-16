package org.litesoft.alleviative.beans;

/**
 * The 2 Letter month codes are from: https://www.eventguide.com/topics/two_digit_month_abbreviations.html
 */
@SuppressWarnings("unused")
public class Months {
  public static class MonthData {
    private final int mMonthNumber, mNominalDays;
    private final String m2Code, m3Code, mName;

    private MonthData( int pMonthNumber, int pNominalDays, String p2Code, String p3Code, String pName ) {
      mMonthNumber = pMonthNumber;
      mNominalDays = pNominalDays;
      m2Code = p2Code;
      m3Code = p3Code;
      mName = pName;
    }

    public int getMonthNumber() {
      return mMonthNumber;
    }

    public int getNominalDays() {
      return mNominalDays;
    }

    public String get2Code() {
      return m2Code;
    }

    public String get3Code() {
      return m3Code;
    }

    public String getName() {
      return mName;
    }

    private boolean is( String pCodeOrName ) {
      return m2Code.equalsIgnoreCase( pCodeOrName ) ||
             m3Code.equalsIgnoreCase( pCodeOrName ) ||
             mName.equalsIgnoreCase( pCodeOrName );
    }
  }

  private static MonthData md( int pMonthNumber, int pNominalDays, String p2Code, String p3Code, String pName ) {
    return new MonthData( pMonthNumber, pNominalDays, p2Code, p3Code, pName );
  }

  public static final MonthData JAN = md( 1, 31, "JA", "Jan", "January" );
  public static final MonthData FEB = md( 2, 28, "FE", "Feb", "February" );
  public static final MonthData MAR = md( 3, 31, "MR", "Mar", "March" );
  public static final MonthData APR = md( 4, 30, "AP", "Apr", "April" );
  public static final MonthData MAY = md( 5, 31, "MY", "May", "May" );
  public static final MonthData JUN = md( 6, 30, "JN", "Jun", "June" );
  public static final MonthData JUL = md( 7, 31, "JL", "Jul", "July" );
  public static final MonthData AUG = md( 8, 31, "AU", "Aug", "August" );
  public static final MonthData SEP = md( 9, 30, "SE", "Sep", "September" );
  public static final MonthData OCT = md( 10, 31, "OC", "Oct", "October" );
  public static final MonthData NOV = md( 11, 30, "NV", "Nov", "November" );
  public static final MonthData DEC = md( 12, 31, "DE", "Dec", "December" );

  private static MonthData[] MONTH_DATAS = {
          md( 0, -1, "--", "Nop", "NoOp" ),
          JAN,
          FEB,
          MAR,
          APR,
          MAY,
          JUN,
          JUL,
          AUG,
          SEP,
          OCT,
          NOV,
          DEC,
          };

  public static MonthData getMonthDataForMonth( int p1basedMonth ) {
    if ( (p1basedMonth < 0) || (12 < p1basedMonth) ) {
      p1basedMonth = 0;
    }
    return MONTH_DATAS[p1basedMonth];
  }

  public static int getNominalDaysForMonth( int p1basedMonth ) {
    return getMonthDataForMonth( p1basedMonth ).getNominalDays();
  }

  public static boolean is31dayMonth( int p1basedMonth ) {
    return getNominalDaysForMonth( p1basedMonth ) == 31;
  }

  public static boolean is30dayMonth( int p1basedMonth ) {
    return getNominalDaysForMonth( p1basedMonth ) == 30;
  }

  public static MonthData getMonthDataForMonth( String pCodeOrName ) {
    if ( pCodeOrName != null ) {
      pCodeOrName = pCodeOrName.trim();
      if ( pCodeOrName.length() > 1 ) {
        for ( int i = 1; i < MONTH_DATAS.length; i++ ) {
          MonthData zData = MONTH_DATAS[i];
          if ( zData.is( pCodeOrName ) ) {
            return zData;
          }
        }
      }
    }
    return MONTH_DATAS[0];
  }

  public static int getNominalDaysForMonth( String pCodeOrName ) {
    return getMonthDataForMonth( pCodeOrName ).getNominalDays();
  }

  public static boolean is31dayMonth( String pCodeOrName ) {
    return getNominalDaysForMonth( pCodeOrName ) == 31;
  }

  public static boolean is30dayMonth( String pCodeOrName ) {
    return getNominalDaysForMonth( pCodeOrName ) == 30;
  }
}
