package org.litesoft.bean;

import java.time.Instant;
import java.util.function.Supplier;

import org.litesoft.alleviative.validation.Email;
import org.litesoft.alleviative.validation.ISO8601;
import org.litesoft.alleviative.validation.ValidationResult;

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
    throw requiredStringIllegalStateException( pWhat );
  }

  protected String requiredSignificant( String pWhat, String pValue ) {
    if ( pValue != null ) {
      pValue = pValue.trim();
      if ( !pValue.isEmpty() ) {
        return pValue;
      }
    }
    throw requiredStringIllegalStateException( pWhat );
  }

  protected String requiredEmail( String pWhat, String pValue ) {
    ValidationResult<String> zResult = Email.validateRequired( pValue );
    if ( !zResult.hasError() ) {
      return zResult.getValue();
    }
    throw requiredIllegalStateException( pWhat, zResult.getError() );
  }

  protected String requiredISO8601Min( String pWhat, String pValue, Supplier<Instant> pDefaultSupplier ) {
    if ( pValue != null ) {
      pValue = pValue.trim();
      if ( pValue.isEmpty() ) {
        pValue = null;
      }
    }
    if ( pValue == null ) {
      Instant zInstant = null;
      if ( pDefaultSupplier != null ) {
        zInstant = pDefaultSupplier.get();
      }
      if ( zInstant == null ) {
        throw requiredStringIllegalStateException( pWhat );
      }
      pValue = zInstant.toString();
    }
    ValidationResult<String> zResult = ISO8601.validateRequired( pValue ).toMin();
    if ( !zResult.hasError() ) {
      return zResult.getValue();
    }
    throw requiredIllegalStateException( pWhat, zResult.getError() );
  }

  private IllegalStateException requiredStringIllegalStateException( String pWhat ) {
    return requiredIllegalStateException( pWhat, "string with no significance" );
  }

  private IllegalStateException requiredIllegalStateException( String pWhat, String pWhy ) {
    return newIllegalStateException( pWhat, " was either not set or it was set to a " + pWhy );
  }

  private IllegalStateException newIllegalStateException( String pWhat, String pWhy ) {
    return new IllegalStateException( pWhat + " on " + getClass().getSimpleName() + pWhy );
  }
}
