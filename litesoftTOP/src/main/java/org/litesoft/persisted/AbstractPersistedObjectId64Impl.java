package org.litesoft.persisted;

import org.litesoft.bean.DataObjectWithRequiredFields;

import java.util.Objects;

public abstract class AbstractPersistedObjectId64Impl extends DataObjectWithRequiredFields implements IPersistedObjectId64 {
  @Override
  public final int hashCode() {
    return Objects.hash( getId() );
  }

  @Override
  public final boolean equals( Object o ) {
    return (this == o) ||
           ((o instanceof IPersistedObjectId64) && equals( (IPersistedObjectId64)o )); // Left to Right
  }

  public final boolean equals( IPersistedObjectId64 them ) {
    return (this == them) ||
           ((them != null) && (this.getClass() == them.getClass()) // Left to Right
            && Objects.equals( this.getId(), them.getId() ));
  }
}
