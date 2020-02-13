package org.litesoft.alleviative;

import java.util.function.Supplier;

import org.litesoft.alleviative.validation.Significant;

public class ToString {
  private final StringBuilder sb = new StringBuilder();
  private final String mClassName;
  private boolean mPretty;
  private int mAttributes;

  private ToString( String pClassName ) {
    sb.append( mClassName = pClassName ).append( ' ' );
  }

  public static ToString of( Class c ) {
    if ( c == null ) {
      throw new IllegalArgumentException( "ToString.of requires a non-null parameter" );
    }
    return new ToString( c.getSimpleName() );
  }

  public static ToString of( Object o ) {
    return of( (o == null) ? null : o.getClass() );
  }

  public ToString pretty() {
    mPretty = true;
    return this;
  }

  public String build() {
    if (0 == mAttributes) {
      sb.append( '{' );
    } else {
      sb.append( mPretty ? '\n' : ' ' );
    }
    sb.append(  '}' );
    return sb.toString();
  }

  public ToString with( String pFieldName, Object pValue ) {
    pFieldName = Significant.orNull( pFieldName );
    if (pFieldName == null) {
      throw new IllegalArgumentException( "No FieldName provided on: " + mClassName );
    }
    sb.append( (0 == mAttributes++) ? '{' : ',' );

    if (mPretty) {
      sb.append( "\n  " );
    } else {
      sb.append( ' ' );
    }

    sb.append( pFieldName ).append( '=' );

    if (pValue == null) {
      sb.append( "null" );
    } else {
      String zValue = pValue.toString(); // If String will return identity
      if ( zValue == pValue ) { // same instances means it was already a String
        sb.append( '\'' ).append( zValue ).append( '\'' );
      } else {
        sb.append( zValue );
      }
    }
    return this;
  }

  public ToString with( Supplier<?> pSupplier, String pFieldName ) {
    if ( pSupplier == null ) {
      throw new IllegalArgumentException( "Supplier required for field '" +
                                          Significant.or( pFieldName, "Not Provided" ) + "' on: " + mClassName );
    }
    return with( pFieldName, pSupplier.get() );
  }
}
