package org.litesoft.bean;

@SuppressWarnings({"SameParameterValue", "unused"})
public class DataObjectWithRequiredFields {
  protected String significantOr( String pValue, String pDefault ) {
    if ( pValue != null ) {
      pValue = pValue.trim();
      if ( !pValue.isEmpty() ) {
        return pValue;
      }
    }
    return pDefault;
  }

  protected int requiredPositive( String pWhat, Integer pValue ) {
    if ( pValue != null ) {
      int zValue = pValue;
      if ( zValue > 0 ) {
        return pValue;
      }
    }
    throw newIllegalStateException( pWhat, " not a positive number, but was: " + pValue );
  }

  protected <T> T requiredNotNull( String pWhat, T pValue ) {
    if ( pValue != null ) {
      return pValue;
    }
    throw requiredIllegalStateException( pWhat, "null" );
  }

  protected String requiredNotEmpty( String pWhat, String pValue ) {
    if ( pValue != null ) {
      if ( !pValue.isEmpty() ) {
        return pValue;
      }
    }
    throw requiredIllegalStateException( pWhat, "string with no significance" );
  }

  protected String requiredSignificant( String pWhat, String pValue ) {
    if ( pValue != null ) {
      pValue = pValue.trim();
      if ( !pValue.isEmpty() ) {
        return pValue;
      }
    }
    throw requiredIllegalStateException( pWhat, "string with no significance" );
  }

  private IllegalStateException requiredIllegalStateException( String pWhat, String pWhy ) {
    return newIllegalStateException( pWhat, " was either not set or it was set to a " + pWhy );
  }

  private IllegalStateException newIllegalStateException( String pWhat, String pWhy ) {
    return new IllegalStateException( pWhat + " on " + getClass().getSimpleName() + pWhy );
  }
}
