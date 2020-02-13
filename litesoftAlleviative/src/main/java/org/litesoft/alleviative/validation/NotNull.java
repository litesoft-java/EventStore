package org.litesoft.alleviative.validation;

public class NotNull {
  public static boolean check( Object o ) {
    return (o != null);
  }

  public static <T> T or( T pToCheck, T pDefault ) {
    return (pToCheck != null) ? pToCheck : pDefault;
  }

  public static String requiredMessage( String pWhat, Object o ) {
    return check( o ) ? null : (pWhat + " is Required");
  }
}
