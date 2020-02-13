package org.litesoft.alleviative.validation;

public class Significant {
  public static String or( String pString, String pOr ) {
    if ( pString != null ) {
      String zString = pString.trim();
      if ( !zString.isEmpty() ) {
        return pString;
      }
    }
    return pOr;
  }

  public static String orNull( String pString ) {
    return or( pString, null );
  }

  public static String orEmpty( String pString ) {
    return (pString == null) ? "" : pString.trim();
  }

  public static boolean check( String pString ) {
    return ((pString != null) && !pString.trim().isEmpty());
  }
}
